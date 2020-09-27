package space.controlnet.pixivia

import space.controlnet.pixivia.app.Application
import space.controlnet.pixivia.module.TagModule

fun main() {

    Application.Builder.registerModules(
        TagModule
    )
        .withFirstBot()
        .runApp()

}

