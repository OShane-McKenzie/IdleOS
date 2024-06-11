package osComponents

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import components.SimpleAnimator
import contentProvider
import kotlinx.coroutines.delay
import objects.AnimationStyle
import objects.ParentConfig
import percentOfParent

@Composable
fun OsContextMenu(
    modifier: Modifier = Modifier,
    onDismissRequest: (String) -> Unit={}
){
    var animate by remember {
        mutableStateOf(false)
    }
    LaunchedEffect(Unit){
        delay(10)
        animate = true
    }
    Box(
        modifier =
        modifier
            .width(8.percentOfParent(ParentConfig.WIDTH).dp)
            .height(10.percentOfParent(ParentConfig.HEIGHT).dp)
    ){
        if(animate){
            SimpleAnimator(AnimationStyle.SCALE_IN_CENTER){
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            contentProvider.globalColor.value.copy(alpha = contentProvider.globalTransparency.value),
                            shape = RoundedCornerShape(8)
                        )
                        .padding(5.dp),
                    verticalArrangement = Arrangement.spacedBy(3.dp),
                    horizontalAlignment = Alignment.Start
                ) {
                    Text("Change Wallpaper",
                        color = contentProvider.globalTextColor.value,
                        modifier = Modifier.fillMaxWidth().background(
                            shape = RoundedCornerShape(13),
                            color = contentProvider.globalColor.value.copy(alpha = 0.0f)
                        ).padding(3.dp).clickable {
                            onDismissRequest.invoke("Change Wallpaper")
                        }
                    )
                    Divider()
                    Text("Settings",
                        color = contentProvider.globalTextColor.value,
                        modifier = Modifier.fillMaxWidth().background(
                            shape = RoundedCornerShape(13),
                            color = contentProvider.globalColor.value.copy(alpha = 0.0f)
                        ).padding(3.dp).clickable {
                            onDismissRequest.invoke("Settings")
                        }
                    )
                    Divider()
                    Text("Properties",
                        color = contentProvider.globalTextColor.value,
                        modifier = Modifier.fillMaxWidth().background(
                            shape = RoundedCornerShape(13),
                            color = contentProvider.globalColor.value.copy(alpha = 0.0f)
                        ).padding(3.dp).clickable {
                            onDismissRequest.invoke("Shutdown")
                        }
                    )
                }
            }
        }
    }
}