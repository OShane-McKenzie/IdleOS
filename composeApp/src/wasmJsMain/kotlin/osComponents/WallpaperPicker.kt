package osComponents

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Close
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import components.ImageButton
import components.SimpleAnimator
import contentProvider
import idleos.composeapp.generated.resources.Res
import kotlinx.coroutines.delay
import objects.AnimationStyle
import objects.ParentConfig
import org.jetbrains.compose.resources.DrawableResource
import percentOfParent
import wallPapers

@Composable
fun WallpaperPicker(modifier: Modifier = Modifier, onDismissRequest:()->Unit={},onItemSelected: (DrawableResource) -> Unit) {
    var animate by remember {
        mutableStateOf(false)
    }
    LaunchedEffect(Unit){
        delay(10)
        animate = true
    }
    val scrollState = rememberScrollState()
    Box(
        modifier =
        modifier.fillMaxWidth(0.98f).height(10.percentOfParent(ParentConfig.HEIGHT).dp)
    ){
        if(animate){
            SimpleAnimator(AnimationStyle.UP){
                Column(
                    verticalArrangement = Arrangement.spacedBy(3.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ){
                    Row(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(
                                contentProvider.globalColor.value.copy(alpha = contentProvider.globalTransparency.value),
                                shape = RoundedCornerShape(8)
                            )
                            .horizontalScroll(scrollState)
                            .padding(5.dp).weight(0.95f),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        wallPapers.forEach {
                            ImageButton(
                                wallpaper = it, modifier = Modifier
                                    .width(8.percentOfParent(ParentConfig.WIDTH).dp)
                                    .height(9.5.percentOfParent(ParentConfig.HEIGHT).dp)
                                    .weight(1f)
                            ) {
                                onItemSelected(it)
                            }
                        }
                    }
                    HorizontalScrollbar(
                        style = LocalScrollbarStyle.current.copy(
                            unhoverColor = contentProvider.globalColor.value.copy(
                                alpha = contentProvider.globalTransparency.value
                            )
                        ),
                        adapter = ScrollbarAdapter(scrollState),
                        modifier = Modifier.weight(0.05f)
                    )
                }
            }
        }
        IconButton(
            onClick = { onDismissRequest.invoke() },
            modifier = Modifier.align(Alignment.TopEnd)
        )
        {
            Icon(
                Icons.Rounded.Close,
                tint = contentProvider.globalTextColor.value,
                contentDescription = "Close",
                modifier = Modifier
                    .background(color = contentProvider.globalColor.value.copy(alpha = contentProvider.globalTransparency.value), shape = RoundedCornerShape(100))
            )
        }
    }
}