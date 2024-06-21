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
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import contentProvider
import fileSystemAppNavigator
import models.IdleDirectory
import objects.Permission
import objects.Sizes
import org.jetbrains.compose.resources.painterResource

@Composable
fun IdleFolder(modifier: Modifier=Modifier, idleDirectory: IdleDirectory, onClick:(String)->Unit={}){
    var imageAlpha by remember { mutableStateOf(1.0f) }
    LaunchedEffect(Unit){
        if(idleDirectory.permission.value == Permission.HIDDEN){
            imageAlpha = 0.6f
        }
    }
    Column(
        modifier = modifier.wrapContentSize(),
        verticalArrangement = Arrangement.spacedBy(1.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(idleDirectory.icon.value),
            contentDescription = "folder icon",
            contentScale = ContentScale.FillBounds,
            modifier = Modifier
                .clickable {  }
                .pointerInput(Unit){
                    detectTapGestures (
                        onDoubleTap = {
                            contentProvider.currentPath.value += idleDirectory.path
                            contentProvider.lastAddedToPath.value = idleDirectory.path
                            fileSystemAppNavigator.setViewState(idleDirectory.name.value)
                            onClick.invoke(idleDirectory.path)
                        }
                    )
                }
                .graphicsLayer(alpha = imageAlpha)
                .clip(RoundedCornerShape(8))
                .height(Sizes.eightyNine.dp)
                .width((Sizes.eightyNine*1.25f).dp)

        )
        Text(idleDirectory.name.value, color = contentProvider.globalTextColor.value)
        Spacer(modifier = Modifier.height(5.dp))
    }
}