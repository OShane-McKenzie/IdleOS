package components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import contentProvider
import idleos.composeapp.generated.resources.Res
import idleos.composeapp.generated.resources.logo_3
import objects.Sizes
import org.jetbrains.compose.resources.painterResource
import toFloat

/**
 * A composable function that represents a single item in a dock.
 *
 * @param modifier The modifier for the dock item. Default is an empty modifier.
 * @param id The unique identifier for the dock item. Default is "default".
 * @param height The height of the dock item. Default is 0.0f.
 * @param width The width of the dock item. Default is 0.0f.
 * @param painter The painter for the image in the dock item. Default is a painter resource for the "logo_3" drawable.
 * @param onClick The callback function when the dock item is clicked. Default is an empty function.
 */
@Composable
fun DockItem(modifier: Modifier = Modifier,id:String = "default", height:Float=0.0f, width:Float=0.0f, painter:Painter = painterResource(Res.drawable.logo_3),onClick:(Boolean)->Unit={}){
    var isActive by remember {
        mutableStateOf(false)
    }
    var calculatedWidth by remember { mutableStateOf(width.dp) }
    var calculatedHeight by remember { mutableStateOf(height.dp) }
    val density = LocalDensity.current
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        Column(
            modifier = Modifier
                .background(color = Color.Black.copy(alpha = 0.0f), shape = RoundedCornerShape(Sizes.twentyOne))
                .clickable{
                    isActive = !isActive
                    if(isActive){
                        if(id !in contentProvider.launchedDockItems){
                            contentProvider.launchedDockItems.add(id)
                        }
                    }
                    onClick(isActive)
                }
                .fillMaxHeight(0.9f)
                .width(calculatedWidth)
                .onGloballyPositioned {
                    calculatedHeight = with(density){(it.size.height).toDp()}
                    calculatedWidth = with(density){(it.size.height).toDp()}
                }
                .padding(top = 3.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painter,
                contentDescription = "",
                contentScale = ContentScale.FillBounds,
                modifier = Modifier.fillMaxSize().clip(CircleShape)
            )
        }
        //Spacer(modifier = Modifier.height(5.dp))
        Column (
                modifier = Modifier
                    .background(
                        color =
                        if(isActive && (id in contentProvider.launchedDockItems)) {
                            contentProvider.activeDockItemIndicator.value}
                        else if(!isActive && (id in contentProvider.launchedDockItems)){
                            contentProvider.inactiveDockItemIndicator.value
                        }else{
                            contentProvider.inactiveDockItemIndicator.value.copy(alpha = 0.0f)
                        },
                        shape = CircleShape
                    )
                    .height(5.dp)
                    .width(5.dp),
                verticalArrangement = Arrangement.Bottom,
                horizontalAlignment = Alignment.CenterHorizontally
        ){

        }
    }
}