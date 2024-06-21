package osComponents

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import contentProvider
import models.IdleDirectory
import models.IdleFile
import objects.Permission
import objects.Sizes
import org.jetbrains.compose.resources.painterResource

@Composable
fun IdleFileContainer(modifier: Modifier, idleFile: IdleFile, onClick:(String)->Unit={}){
    var imageAlpha by remember { mutableStateOf(1.0f) }
    LaunchedEffect(Unit){
        if(idleFile.permission.value == Permission.HIDDEN){
            imageAlpha = 0.6f
        }
    }
    val painter:MutableState<Painter> = mutableStateOf(painterResource(idleFile.icon.value))
    Column(
        modifier = modifier.wrapContentSize(),
        verticalArrangement = Arrangement.spacedBy(1.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painter.value,
            contentDescription = "File icon",
            contentScale = ContentScale.FillBounds,
            modifier = Modifier
                .clickable {  }
                .pointerInput(Unit){
                    detectTapGestures (
                        onDoubleTap = {
                            onClick.invoke(idleFile.path)
                        }
                    )
                }
                .clip(RoundedCornerShape(8))
                .height(Sizes.eightyNine.dp)
                .width((Sizes.eightyNine).dp)
                .graphicsLayer(alpha = imageAlpha)


        )
        Text(idleFile.name.value, color = contentProvider.globalTextColor.value)
        Spacer(modifier = Modifier.height(5.dp))
    }
}