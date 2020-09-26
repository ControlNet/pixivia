package space.controlnet.pixivia.utils

import java.nio.file.Path
import java.nio.file.Paths

fun Path.append(vararg more: String): Path = Paths.get(this.toString(), *more)