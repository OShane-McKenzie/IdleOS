package osComponents

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import components.SimpleAnimator
import contentProvider
import kotlinx.coroutines.*
import objects.AnimationStyle
import objects.ParentConfig
import percentOfParent
import turnOnWidget

/**
 * Renders the OS information component.
 *
 * @param modifier The modifier for the component. Default is an empty modifier.
 * @return The composable function that renders the OS information component.
 */
@Composable
fun OsInfo(modifier:Modifier = Modifier){
    val density = LocalDensity.current
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
            //.width(16.percentOfParent(ParentConfig.WIDTH, density))
            //.height(20.percentOfParent(ParentConfig.HEIGHT, density))
    ){
        if(animate){
            SimpleAnimator(AnimationStyle.DOWN){
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            contentProvider.globalColor.value.copy(alpha = contentProvider.globalTransparency.value),
                            shape = RoundedCornerShape(8)
                        )
                        .padding(5.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    horizontalAlignment = Alignment.Start
                ) {
                    Text("About IdleOS",
                        color = contentProvider.globalTextColor.value,
                        modifier = Modifier.fillMaxWidth().background(
                            shape = RoundedCornerShape(13),
                            color = contentProvider.globalColor.value.copy(alpha = 0.0f)
                        ).padding(3.dp).clickable {
                            contentProvider.showAbout.value = !contentProvider.showAbout.value
                            turnOnWidget("none")
                        }
                    )
                    Text("Logout",
                        color = contentProvider.globalTextColor.value,
                        modifier = Modifier.fillMaxWidth().background(
                            shape = RoundedCornerShape(13),
                            color = contentProvider.globalColor.value.copy(alpha = 0.0f)
                        ).padding(3.dp).clickable {
                            contentProvider.isLoggedOut.value = true
                        }
                    )
                    Text("Restart",
                        color = contentProvider.globalTextColor.value,
                        modifier = Modifier.fillMaxWidth().background(
                            shape = RoundedCornerShape(13),
                            color = contentProvider.globalColor.value.copy(alpha = 0.0f)
                        ).padding(3.dp).clickable {
                            CoroutineScope(Dispatchers.Default).launch {
                                withContext(Dispatchers.Main) {
                                    contentProvider.startApp.value = false
                                }
                                delay(200)
                                withContext(Dispatchers.Main) {
                                    contentProvider.startApp.value = true
                                    contentProvider.isLoggedOut.value = true
                                }
                            }
                        }
                    )
                }
            }
        }
    }
}