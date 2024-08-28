package osComponents

import about
import androidx.compose.foundation.*
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import contentProvider
import org.w3c.workers.Client
import kotlin.math.roundToInt

@Composable
fun IdleAbout(modifier:Modifier = Modifier,  onClick:()->Unit = {}){
    val scrollState = rememberScrollState()
    Box(
        modifier = modifier
            .background(
                color = contentProvider.globalColor.value.copy(alpha = contentProvider.globalTransparency.value),
                shape = RoundedCornerShape(3)
            ).padding(8.dp)
    ){
        Column(
            modifier = Modifier.fillMaxSize()
                .background(
                    color = Color.White.copy(alpha = 0.0f),
                    shape = RoundedCornerShape(8)
                )
                .align(Alignment.Center)
                .verticalScroll(scrollState),
            verticalArrangement = Arrangement.spacedBy(2.dp)
        ){
            Text(
                about,
                modifier = Modifier.fillMaxSize(),
                color = contentProvider.globalTextColor.value,
                textAlign = TextAlign.Center
            )
        }



        VerticalScrollbar(
            modifier = Modifier.align(Alignment.CenterEnd),
            adapter = rememberScrollbarAdapter(scrollState),
            style = LocalScrollbarStyle.current.copy(
                hoverColor = contentProvider.globalTextColor.value,
                unhoverColor = contentProvider.globalTextColor.value.copy(alpha = 0.4f)
            )
        )
        Row(
            modifier = Modifier.fillMaxWidth().wrapContentHeight().align(Alignment.TopCenter),
            horizontalArrangement = Arrangement.End
        ){
            IconButton(
                onClick = {onClick.invoke()},
                modifier = Modifier
                    .background(color = contentProvider.globalTextColor.value.copy(alpha = 0.4f), shape = CircleShape)
                    .size(30.dp)
                    .padding(0.dp)
            ){
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "",
                    tint = contentProvider.globalTextColor.value,
                    modifier = Modifier.size(30.dp).padding(0.dp)
                )
            }
        }

    }
}