package osComponents

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*

import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.unit.dp
import contentProvider
import objects.ParentConfig.*
import objects.Sizes
import percentOfParent

@Composable
fun Panel(
    modifier: Modifier = Modifier,
    start: @Composable (Float,Float)->Unit={width, height->},
    middle: @Composable (Float,Float)->Unit={width, height->},
    end: @Composable (Float,Float)->Unit={width, height->}
){
    var panelWidth by remember{
        mutableFloatStateOf(1.0f)
    }
    var panelHeight by remember{
        mutableFloatStateOf(1.0f)
    }

    Row(
        modifier = modifier
            .height(contentProvider.panelHeight.value.percentOfParent(HEIGHT).dp)
            .width(contentProvider.panelWidth.value.percentOfParent(WIDTH).dp)
            .padding(3.dp)
            .background(color = Color.Black.copy(alpha = 0.0f))
            .onGloballyPositioned {
                panelWidth = it.size.width.toFloat()
                panelHeight = it.size.height.toFloat()
            },
        verticalAlignment = Alignment.Top,
        horizontalArrangement = Arrangement.Center
    ){
        Box(
            modifier = Modifier.fillMaxSize().background(color = Color.Black.copy(alpha = 0.0f)),
        ){
            Row(
                modifier = Modifier.fillMaxHeight().width((panelWidth*0.4f).dp).align(Alignment.TopStart),
                verticalAlignment = Alignment.Top,
                horizontalArrangement = Arrangement.Start
            ){
                start((panelWidth*0.4f),panelHeight)
            }
            Row(
                modifier = Modifier.fillMaxHeight().width((panelWidth*0.1f).dp).align(Alignment.TopCenter),
                verticalAlignment = Alignment.Top,
                horizontalArrangement = Arrangement.Center
            ){
                middle((panelWidth*0.1f),panelHeight)
            }
            Row(
                modifier = Modifier.fillMaxHeight().width((panelWidth*0.4f).dp).align(Alignment.TopEnd),
                verticalAlignment = Alignment.Top,
                horizontalArrangement = Arrangement.End
            ){
                end((panelWidth*0.4f),panelHeight)
            }
        }
    }
}