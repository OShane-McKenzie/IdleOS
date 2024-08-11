package components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import contentProvider
import kotlinx.dom.appendElement
import kotlinx.dom.createElement

@Composable
fun WebAppCreator(modifier: Modifier = Modifier, url:String){
    HtmlView(
        modifier = modifier.fillMaxSize()
            .padding(3.dp)
            .background(color = contentProvider.globalColor.value.copy(alpha = 0.0f), shape = RoundedCornerShape(5)),
        factory = {


            val frame = createElement("iframe"){
                setAttribute("id","frame")
                setAttribute("src", url)
                setAttribute("width","100%")
                setAttribute("height","100%")
                setAttribute("style","border:none;")
                setAttribute("allow","fullscreen")
            }

            frame
        }

    )

}
