package components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Home
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector

import androidx.compose.ui.unit.dp
import contentProvider
import objects.Sizes

@Composable
fun FileManagerButton(modifier: Modifier = Modifier, icon: ImageVector = Icons.Rounded.Home, label:String = "Home", onClick:(String)->Unit={}){
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(Sizes.thirtyFour.dp)
            .background(shape = RoundedCornerShape(100), color = contentProvider.globalColor.value.copy(alpha = 0.0f))
            .clickable {
                onClick.invoke(label)
            }
            .padding(3.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start
    ){
        Icon(
            imageVector = icon,
            contentDescription = "",
            modifier = Modifier.size(Sizes.thirtyFour.dp),
            tint = contentProvider.globalTextColor.value
        )
        Spacer(modifier=Modifier.width(5.dp))
        Text(label, color = contentProvider.globalTextColor.value)
    }
}