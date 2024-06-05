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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.unit.dp
import contentProvider
import idleos.composeapp.generated.resources.Res
import idleos.composeapp.generated.resources.logo_3
import objects.Sizes
import org.jetbrains.compose.resources.painterResource

@Composable
fun DockItem(modifier: Modifier = Modifier,id:String = "default", height:Float=0.0f, width:Float=0.0f, painter:Painter = painterResource(Res.drawable.logo_3),onClick:(Boolean)->Unit={}){
    var isActive by remember {
        mutableStateOf(false)
    }

    Column(
        modifier = Modifier.wrapContentSize(),
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        Spacer(modifier = Modifier.height(5.dp))
        Column(
            modifier = modifier
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
                .height((height*0.6).dp).width((width).dp)
                .onGloballyPositioned {
                },
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painter,
                contentDescription = "",
                contentScale = ContentScale.FillBounds,
                modifier = Modifier.fillMaxSize()
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
                    .width(4.dp),
                verticalArrangement = Arrangement.Bottom,
                horizontalAlignment = Alignment.CenterHorizontally
        ){

        }
    }
}