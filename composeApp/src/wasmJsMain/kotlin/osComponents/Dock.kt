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
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import contentProvider
import layoutConfigurator
import objects.ParentConfig.*
import percentOfParent
import toInt

/**
 * A composable function that represents a dock.
 *
 * @param modifier The modifier for the dock. Default is an empty modifier.
 * @param items A lambda function that takes the width and height of the dock as parameters and returns Unit.
 *              Default is a lambda function that takes the width and height as parameters and does nothing.
 *
 * @return The composable function that represents the dock.
 */
@Composable
fun Dock(modifier: Modifier = Modifier, items: @Composable (Float,Float)-> Unit={width, height->}){
    val density = LocalDensity.current
    val scrollState = rememberScrollState()
    var width by remember {
        mutableFloatStateOf(layoutConfigurator.parentWidth.value * 0.98f)
    }
    var height by remember {
        mutableFloatStateOf(layoutConfigurator.parentHeight.value * 0.03f)
    }
    Column(
        modifier = modifier
            .width(contentProvider.dockWidth.value.percentOfParent(WIDTH, density))
            .height(contentProvider.dockHeight.value.percentOfParent(HEIGHT, density))

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