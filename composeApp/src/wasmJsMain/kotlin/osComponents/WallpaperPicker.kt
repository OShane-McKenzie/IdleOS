package osComponents

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Close
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import components.ImageButton
import components.SimpleAnimator
import contentProvider
import kotlinx.coroutines.delay
import objects.AnimationStyle
import objects.ParentConfig
import org.jetbrains.compose.resources.DrawableResource
import percentOfParent
import wallPapers

@Composable
fun WallpaperPicker(modifier: Modifier = Modifier, onDismissRequest:()->Unit={},onItemSelected: (DrawableResource) -> Unit) {
    val density = LocalDensity.current
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
        modifier
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
                                image = it, modifier = Modifier
                                    .width(16.percentOfParent(ParentConfig.WIDTH, density))
                                    .height(19.percentOfParent(ParentConfig.HEIGHT, density))
                                    //.fillMaxWidth(0.16f)
                                    //.fillMaxHeight(0.19f)
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