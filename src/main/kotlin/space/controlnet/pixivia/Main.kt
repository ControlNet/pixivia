package space.controlnet.pixivia

import space.controlnet.pixivia.app.Application
import space.controlnet.pixivia.module.PixivModule
import space.controlnet.pixivia.module.ReadmeModule
import space.controlnet.pixivia.module.TagModule

fun main() {
    Application.Builder.registerModules(
        TagModule,
        ReadmeModule,
        PixivModule
    )
        .withFirstBot()
        .runApp()
}
