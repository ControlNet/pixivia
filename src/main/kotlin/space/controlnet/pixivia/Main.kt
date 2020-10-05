package space.controlnet.pixivia

import space.controlnet.pixivia.app.Application
import space.controlnet.pixivia.module.PixivModule
import space.controlnet.pixivia.module.ReadmeModule
import space.controlnet.pixivia.module.TagModule
import space.controlnet.pixivia.module.WTDataModule

fun main() {
    Application.Builder.registerModules(
        TagModule,
        ReadmeModule,
        PixivModule,
        WTDataModule
    )
        .withFirstBot()
        .runApp()
}
