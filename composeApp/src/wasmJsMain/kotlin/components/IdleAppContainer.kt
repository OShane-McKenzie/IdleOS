package components

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import contentProvider
import kotlinx.coroutines.delay
import layoutConfigurator
import objects.AnimationStyle
import objects.ParentConfig
import percentOfParent
import kotlin.math.roundToInt

@Composable
fun IdleAppContiner(
    modifier: Modifier = Modifier,
    onMinimize:()->Unit={},
    onMaximize:()->Unit = {},
    onCloseRequest:()->Unit={},
    content: @Composable ()->Unit = {}
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
            .width((25.percentOfParent(ParentConfig.WIDTH)).dp)
            .height(30.percentOfParent(ParentConfig.HEIGHT).dp)
            .animateContentSize()

            .border(width = 5.dp, color = Color.Black)

    ){
        if(animate){
            SimpleAnimator(AnimationStyle.SCALE_IN_CENTER, modifier = Modifier.wrapContentSize()){
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            contentProvider.globalColor.value.copy(
                                alpha = 0.0f),
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
                            .background(
                                contentProvider.globalColor.value.copy(
                                    alpha = contentProvider.globalTransparency.value),
                                    shape = RoundedCornerShape(8)
                            )
                    ){

                    }
                }
            }
        }
    }
}