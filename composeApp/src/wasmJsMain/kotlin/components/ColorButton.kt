package components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import objects.Sizes

@Composable
fun ColorButton(modifier: Modifier = Modifier, color: Color, onClick:(Color)->Unit={}){
    Box(
        modifier = modifier.background(color = color, shape = RoundedCornerShape(Sizes.thirtyFour))
            .border(width = 1.dp, color = Color(0xff918b8b), shape = RoundedCornerShape(Sizes.thirtyFour))
            .clickable {
            onClick.invoke(color)
        }
    ){

    }
}