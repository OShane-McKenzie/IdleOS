package components

import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInWindow
import androidx.compose.ui.unit.dp
import contentProvider
import kotlinx.coroutines.delay

@Composable
fun PanelWidget(
    modifier: Modifier = Modifier,
    isWidgetClickable: Boolean = true,
    onClick: () -> Unit = {},
    id:String = "",
    onPositioned: (offsetX: Float, offsetY: Float) -> Unit = { _, _ -> },
    content: @Composable (offsetX: Float, offsetY: Float, String) -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }
    val indication = LocalIndication.current

    var offsetX by remember { mutableStateOf(1f) }
    var offsetY by remember { mutableStateOf(1f) }

    Row(
        modifier = modifier
            .background(
                color = contentProvider.globalColor.value.copy(
                    alpha = contentProvider.globalTransparency.value
                ),
                shape = CircleShape
            )
            .padding(start = 8.dp, end = 8.dp, top = 1.dp, bottom = 1.dp)
            .clickable(
                interactionSource = interactionSource,
                indication = if (!isWidgetClickable) null else indication
            ) {
                if (isWidgetClickable) { onClick.invoke() }
            }
            .wrapContentWidth()
            .fillMaxHeight()
            .onGloballyPositioned { coordinates ->
                val position = coordinates.positionInWindow()
                offsetX = position.x
                offsetY = position.y
                if(id !in contentProvider.widgetList){
                    contentProvider.widgetList.add(id)
                }
                onPositioned(offsetX, offsetY)
            },
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        content(offsetX, offsetY, id)
    }
}
