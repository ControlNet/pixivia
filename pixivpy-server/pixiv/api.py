import json
from json.decoder import JSONDecodeError

from pixivpy3 import PixivAPI, ByPassSniApi
from pixivpy3.api import BasePixivAPI
from pixivpy3.utils import JsonDict, PixivError

from pixiv.validation import handle_auth_validation


class API:
    def __init__(self, pixiv_account: str, pixiv_password: str, pixiv_id: str, token_path: str, image_saving_dir: str):
        self.api: PixivAPI = PixivAPI()  # depreciated
        self.aapi: ByPassSniApi = ByPassSniApi()
        self.aapi.require_appapi_hosts(hostname="public-api.secure.pixiv.net")
        self.pixiv_account: str = pixiv_account
        self.pixiv_password: str = pixiv_password
        self.id = int(pixiv_id)
        self.token_path: str = token_path
        self.image_saving_dir: str = image_saving_dir

    def login_or_load_token(self, try_token=True, app_api=False) -> JsonDict:
        api: BasePixivAPI = self.aapi if app_api else self.api

        if try_token:
            try:
                with open(self.token_path, "r", encoding="UTF-8") as token_file:
                    token = json.load(token_file)
            except (FileNotFoundError, JSONDecodeError):
                use_token = False
            else:
                try:
                    if token["refresh_token"] is None or token["access_token"] is None:
                        use_token = False
                    else:
                        use_token = True
                except KeyError:
                    use_token = False
        else:
            use_token = False

        if use_token:
            try:
                response = api.auth(refresh_token=token["refresh_token"])
            except PixivError as e:
                if str(e).split("\n")[0] == "[ERROR] auth() failed! check refresh_token.":
                    response = api.login(self.pixiv_account, self.pixiv_password)
                else:
                    raise e
        else:
            response = api.login(self.pixiv_account, self.pixiv_password)

        # save token
        self.save_token(app_api)
        return response

    def save_token(self, app_api) -> None:
        api: BasePixivAPI = self.aapi if app_api else self.api
        with open(self.token_path, "w", encoding="UTF-8") as token_file:
            token_file.write(json.dumps({
                "access_token": api.access_token,
                "refresh_token": api.refresh_token
            }))

    @handle_auth_validation(app_api=True)
    def works(self, illust_id: int) -> JsonDict:
        return self.aapi.illust_detail(illust_id=illust_id)

    def download(self, url, prefix="", name=None, replace=False, fname=None,
                 referer='https://app-api.pixiv.net/') -> bool:
        return self.api.download(url, prefix=prefix, path=self.image_saving_dir, name=name, replace=replace,
                                 fname=fname,
                                 referer=referer)

    @handle_auth_validation(app_api=True)
    def me_following_works(self, page=1) -> JsonDict:
        return self.aapi.illust_follow(offset=(page - 1) * 30)

    @handle_auth_validation(app_api=True)
    def me_following(self, page=1, publicity='public') -> JsonDict:
        return self.aapi.user_following(user_id=self.id, offset=(page - 1) * 30, restrict=publicity)

    @handle_auth_validation(app_api=True)
    def follow(self, user_id: int) -> str:
        return self.api.me_favorite_users_follow(user_id)["status"]

    @handle_auth_validation(app_api=False)
    def unfollow(self, user_id: int) -> str:
        return self.api.me_favorite_users_unfollow(user_id)["status"]

    @handle_auth_validation(app_api=True)
    def illust_recommended(self) -> JsonDict:
        return self.aapi.illust_recommended()

    @handle_auth_validation(app_api=False)
    def users(self, user_id: int) -> JsonDict:
        return self.aapi.user_detail(user_id=user_id)
