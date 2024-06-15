package components

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import contentProvider
import idleos.composeapp.generated.resources.Res
import idleos.composeapp.generated.resources.close
import idleos.composeapp.generated.resources.max
import idleos.composeapp.generated.resources.min
import kotlinx.coroutines.delay
import layoutConfigurator
import objects.AnimationStyle
import objects.ParentConfig
import objects.Sizes
import percentOfParent


@Composable
fun IdleAppContainer(
    modifier: Modifier = Modifier,
    borderShape: Shape = RoundedCornerShape(Sizes.three),
    displayWindow: Boolean = true,
    onMinimize: () -> Unit = {},
    onMaximize: () -> Unit = {},
    onCloseRequest: () -> Unit = {},
    content: @Composable () -> Unit = {}
) {
    val density = LocalDensity.current
    var animate by remember { mutableStateOf(false) }
    var windowHeight by remember { mutableStateOf(10.percentOfParent(ParentConfig.HEIGHT, density)) }
    var windowWidth by remember { mutableStateOf(10.percentOfParent(ParentConfig.WIDTH, density)) }
    var isDisplayWindow by remember { mutableStateOf(displayWindow) }

    var callIsDisplayWindow by remember { mutableStateOf(false) }

    var transparency by remember { mutableStateOf(contentProvider.globalTransparency.value) }

    val animatedHeight by animateDpAsState(
        targetValue = windowHeight,
        animationSpec = tween(durationMillis = 200, easing = FastOutSlowInEasing)
    )

    val animatedWidth by animateDpAsState(
        targetValue = windowWidth,
        animationSpec = tween(durationMillis = 200, easing = FastOutSlowInEasing)
    )

    LaunchedEffect(Unit) {
        delay(10)
        windowHeight =  50.percentOfParent(ParentConfig.HEIGHT, density)
        windowWidth =  55.percentOfParent(ParentConfig.WIDTH, density)
        animate = true
    }
    LaunchedEffect(callIsDisplayWindow){
        if(callIsDisplayWindow){
            windowWidth = 5.percentOfParent(ParentConfig.WIDTH, density)
            windowHeight = 5.percentOfParent(ParentConfig.HEIGHT, density)
            delay(190)
            isDisplayWindow = false
        }
    }

    if(isDisplayWindow){
        Box(
            modifier = modifier
                .width(animatedWidth)
                .height(animatedHeight)
                .border(width = 1.dp, color = contentProvider.globalColor.value, shape = borderShape)
                .padding(Sizes.three.dp)
        ) {
            if (animate) {
                SimpleAnimator(AnimationStyle.SCALE_IN_CENTER, modifier = Modifier.wrapContentSize()) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(
                                contentProvider.globalColor.value.copy(alpha = 0.0f),
                                shape = RoundedCornerShape(8)
                            )
                            .padding(3.dp),
                        verticalArrangement = Arrangement.spacedBy(1.dp),
                        horizontalAlignment = Alignment.Start
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(40.dp)
                                .padding(2.dp)
                                .background(
                                    contentProvider.globalColor.value.copy(
                                        alpha = transparency
                                    ),
                                    shape = RoundedCornerShape(8)
                                ),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.End
                        ) {
                            ImageButton(
                                wallpaper = Res.drawable.min,
                                modifier = Modifier
                                    .padding(0.dp)
                                    .clip(CircleShape)
                                    .fillMaxHeight()
                                    .width(34.dp)
                            ) {
                                onMinimize.invoke()
                                transparency = contentProvider.globalTransparency.value
                                windowHeight = 50.percentOfParent(ParentConfig.HEIGHT, density)
                                windowWidth = 50.percentOfParent(ParentConfig.WIDTH, density)
                            }
                            ImageButton(
                                wallpaper = Res.drawable.max,
                                modifier = Modifier
                                    .padding(0.dp)
                                    .clip(CircleShape)
                                    .fillMaxHeight()
                                    .width(34.dp)
                            ) {
                                onMaximize.invoke()
                                transparency = 1.0f
                                windowHeight = 100.percentOfParent(ParentConfig.HEIGHT, density)
                                windowWidth = 100.percentOfParent(ParentConfig.WIDTH, density)
                            }
                            ImageButton(
                                wallpaper = Res.drawable.close,
                                modifier = Modifier
                                    .padding(0.dp)
                                    .clip(CircleShape)
                                    .fillMaxHeight()
                                    .width(32.dp)
                            ) {
                                onCloseRequest.invoke()
                                callIsDisplayWindow = true
                            }
                        }
                        content()
                    }
                }
            }
        }
    }
}
