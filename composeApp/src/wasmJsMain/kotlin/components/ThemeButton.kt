package components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.Menu
import androidx.compose.material.icons.rounded.Star
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import contentProvider
import objects.Sizes

@Composable
fun ThemeButton(
    modifier: Modifier=Modifier,
    icon:ImageVector = Icons.Rounded.Star,
    tint:Color = Color(0xfffdfab5),
    description:String = "",
    label:String = "Light",
    onClick:()->Unit={}
){
    Row(
        modifier = modifier
            .background(color = contentProvider.globalColor.value, shape = RoundedCornerShape(Sizes.twentyOne))
            .padding(8.dp).clickable{
            onClick.invoke()
        },
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ){
        Column(
            modifier = Modifier.fillMaxHeight().fillMaxWidth(0.6f).weight(0.6f),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ){
            Text(
                label,
                color = contentProvider.globalTextColor.value,
                fontWeight = FontWeight.Bold,
            )
        }
        Icon(
            modifier = Modifier.fillMaxHeight().fillMaxWidth(0.4f).padding(0.dp).weight(0.4f),
            imageVector = icon,
            contentDescription = description,
            tint = tint
        )
    }
}