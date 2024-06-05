package osComponents

import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.unit.dp
import contentProvider
import objects.ParentConfig.*
import percentOfParent

@Composable
fun Dock(modifier: Modifier = Modifier, items: @Composable (Float,Float)-> Unit={width, height->}){
    val scrollState = rememberScrollState()
    var width by remember {
        mutableFloatStateOf(98.percentOfParent(WIDTH))
    }
    var height by remember {
        mutableFloatStateOf(3.percentOfParent(HEIGHT))
    }
    Column(
        modifier = modifier
            .width(contentProvider.dockWidth.value.percentOfParent(WIDTH).dp)
            .height(contentProvider.dockHeight.value.percentOfParent(HEIGHT).dp)

            .padding(top = 8.dp, bottom = 8.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        Row(
            modifier = Modifier
                .fillMaxHeight()
                .fillMaxWidth()
                .horizontalScroll(scrollState)
                .onGloballyPositioned {
                    height = it.size.height.toFloat()
                    width = it.size.height.toFloat()
                }
                .background(
                    shape = RoundedCornerShape(1),
                    color = contentProvider.globalColor.value.copy(alpha = contentProvider.globalTransparency.value)
                ).padding(0.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            items(width, height)
        }
    }
}