package models

import androidx.compose.runtime.Composable
import idleos.composeapp.generated.resources.Res
import idleos.composeapp.generated.resources.compose_multiplatform
import org.jetbrains.compose.resources.DrawableResource

data class IdleApp(
    var app: @Composable ()->Unit = {},
    var name:String = "",
    var icon:DrawableResource = Res.drawable.compose_multiplatform
)
