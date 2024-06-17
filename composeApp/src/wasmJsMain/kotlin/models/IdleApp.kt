package models

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import idleos.composeapp.generated.resources.Res
import idleos.composeapp.generated.resources.compose_multiplatform
import org.jetbrains.compose.resources.DrawableResource

data class IdleAppModel(
    var app: @Composable ()->Unit = {},
    var name:String = "",
    val offsetX:MutableState<Float> = mutableStateOf(0f),
    val offsetY:MutableState<Float> = mutableStateOf(0f),
    val alignment:MutableState<Alignment> = mutableStateOf(Alignment.Center),
    var icon:DrawableResource = Res.drawable.compose_multiplatform
)
