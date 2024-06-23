package components

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import contentProvider
import idleos.composeapp.generated.resources.*
import idleos.composeapp.generated.resources.Res
import idleos.composeapp.generated.resources.close
import idleos.composeapp.generated.resources.max
import idleos.composeapp.generated.resources.min
import kotlinx.coroutines.delay
import models.IdleAppModel
import objects.AnimationStyle
import objects.ParentConfig
import objects.Sizes
import org.jetbrains.compose.resources.painterResource
import percentOfParent


@Composable
fun IdleAppContainer(
    modifier: Modifier = Modifier,
    borderShape: Shape = RoundedCornerShape(Sizes.three),
    displayWindow: Boolean = true,
    isMoving:Boolean = false,
    onMinimize: () -> Unit = {},
    onMaximize: () -> Unit = {},
    onCloseRequest: () -> Unit = {},
    app:IdleAppModel = IdleAppModel()
) {
    val density = LocalDensity.current
    var animate by remember { mutableStateOf(false) }
    var windowHeight by remember { mutableStateOf(10.percentOfParent(ParentConfig.HEIGHT, density)) }
    var windowWidth by remember { mutableStateOf(10.percentOfParent(ParentConfig.WIDTH, density)) }
    var isDisplayWindow by remember { mutableStateOf(displayWindow) }

    var isMaximized by remember { mutableStateOf(false) }
    var isMinimized by remember { mutableStateOf(false) }

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
    LaunchedEffect(contentProvider.globalTransparency.value){
        transparency = contentProvider.globalTransparency.value
    }
    LaunchedEffect(callIsDisplayWindow){
        if(callIsDisplayWindow){
            windowWidth = 5.percentOfParent(ParentConfig.WIDTH, density)
            windowHeight = 5.percentOfParent(ParentConfig.HEIGHT, density)
            delay(190)
            onCloseRequest.invoke()
            isDisplayWindow = false
        }
    }
//    LaunchedEffect(isMoving){
//        if(isMaximized){
//            transparency = contentProvider.globalTransparency.value
//            windowHeight = 50.percentOfParent(ParentConfig.HEIGHT, density)
//            windowWidth = 55.percentOfParent(ParentConfig.WIDTH, density)
//            isMinimized = true
//            isMaximized = false
//        }
//    }
//    LaunchedEffect(isMaximized){
//        if(isMaximized){
//            delay(100)
//
//            transparency = 1.0f
//            windowHeight = 100.percentOfParent(ParentConfig.HEIGHT, density)
//            windowWidth = 100.percentOfParent(ParentConfig.WIDTH, density)
//        }
//    }
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
                            Row(
                                modifier = Modifier.fillMaxHeight().fillMaxWidth().padding(3.dp).weight(0.1f),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(5.dp)
                            ) {
                                Image(
                                    painter = painterResource(app.icon),
                                    contentDescription = "app icon",
                                    contentScale = ContentScale.FillBounds,
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(8))
                                        .height(Sizes.thirtyFour.dp)
                                        .width(Sizes.thirtyFour.dp)

                                )
                                Text(app.name, color = contentProvider.globalTextColor.value)
                            }
                            ImageButton(
                                image = Res.drawable.min,
                                modifier = Modifier
                                    .weight(0.1f)
                                    .padding(0.dp)
                                    .clip(CircleShape)
                                    .fillMaxHeight()
                                    .width(34.dp)
                            ) {
                                onMinimize.invoke()
                                transparency = contentProvider.globalTransparency.value
                                windowHeight = 50.percentOfParent(ParentConfig.HEIGHT, density)
                                windowWidth = 55.percentOfParent(ParentConfig.WIDTH, density)
                                isMinimized = true
                                isMaximized = false
                            }
                            ImageButton(
                                image = Res.drawable.max,
                                modifier = Modifier
                                    .weight(0.1f)
                                    .padding(0.dp)
                                    .clip(CircleShape)
                                    .fillMaxHeight()
                                    .width(34.dp)
                            ) {
                                onMaximize.invoke()
                                transparency = 1.0f
                                windowHeight = 100.percentOfParent(ParentConfig.HEIGHT, density)
                                windowWidth = 100.percentOfParent(ParentConfig.WIDTH, density)
                                isMaximized = true
                                isMinimized = false
                            }
                            ImageButton(
                                image = Res.drawable.close,
                                modifier = Modifier
                                    .weight(0.1f)
                                    .padding(0.dp)
                                    .clip(CircleShape)
                                    .fillMaxHeight()
                                    .width(32.dp)
                            ) {
                                callIsDisplayWindow = true
                            }
                        }
                        app.app.invoke()
                    }
                }
            }

            Image(
                painter = painterResource(Res.drawable.drag),
                contentDescription = "Drag",
                contentScale = ContentScale.FillBounds,
                modifier = Modifier
                    .clip(RoundedCornerShape(100))
                    .height(Sizes.twentyOne.dp)
                    .width(Sizes.twentyOne.dp)
                    .align(Alignment.BottomEnd)
                    .background(color = Color.Gray.copy(alpha = 0.5f), shape = RoundedCornerShape(100))
                    .clickable {

                    }
                    .pointerInput(Unit) {
                        detectDragGestures { change, dragAmount ->
                            change.consume()
                            windowWidth += dragAmount.x.toDp()
                            windowHeight += dragAmount.y.toDp()
                        }
                        detectTapGestures { }
                    }

            )
        }
    }
}
