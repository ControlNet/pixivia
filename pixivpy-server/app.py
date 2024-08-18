import json
import os
import sys

from flask import Flask

from pixiv.api import API

app = Flask(__name__)
_, pixiv_account, pixiv_password, pixiv_id, token_path, image_saving_dir = sys.argv

api = API(pixiv_account, pixiv_password, pixiv_id, token_path, image_saving_dir)


@app.route('/')
def hello_world():
    return 'Pixivpy Server Running'


@app.route("/query/image/<image_id>")
def query_image(image_id: str):
    return json.dumps(api.works(illust_id=int(image_id)))


@app.route("/query/user/<user_id>")
def query_user(user_id: str):
    return api.users(int(user_id))["response"][0]


@app.route("/download/image/<image_id>")
def download_image(image_id: str):
    response = api.works(illust_id=int(image_id))
    urls = response["illust"]["image_urls"]
    if "large" in urls.keys():
        url = urls["large"]
    elif "medium" in urls.keys():
        url = urls["medium"]
    else:
        url = urls["small"]

    file_name = f"{image_id}.png"
    # existed
    if os.path.isfile(os.path.join(api.image_saving_dir, file_name)):
        return "TRUE"
    else:
        return "TRUE" if api.download(url=url, name=file_name) else "FALSE"


@app.route("/following/display")
def following_display():
    all_following = []
    terminate = False
    page = 1
    while not terminate:
        all_following += list(map(lambda x: x["user"], api.me_following(page=page)["user_previews"]))
    return json.dumps(all_following)


@app.route("/following/follow/<user_id>")
def follow_user(user_id: str):
    return api.follow(int(user_id))


@app.route("/following/unfollow/<user_id>")
def unfollow_user(user_id: str):
    return api.unfollow(int(user_id))


@app.route("/following/new")
def me_following_works():
    return json.dumps(api.me_following_works()["illusts"])


@app.route("/recommend/image")
def illust_recommended():
    return json.dumps(api.illust_recommended()["illusts"])


if __name__ == '__main__':
    app.run(host="127.0.0.1", port="5000")

