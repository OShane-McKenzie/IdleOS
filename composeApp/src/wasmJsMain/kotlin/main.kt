import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.CanvasBasedWindow
import androidx.compose.ui.window.ComposeViewport
import components.HtmlView
import components.LocalLayerContainer
import idleos.composeapp.generated.resources.*
import kotlinx.browser.document
import org.jetbrains.compose.resources.DrawableResource


val fileSystemLogs = mutableStateOf("")
val wallpaperMap: Map<String, DrawableResource> = mapOf(
    "One" to Res.drawable.one,
    "Two" to Res.drawable.two,
    "Three" to Res.drawable.three,
    "Four" to Res.drawable.four,
    "Five" to Res.drawable.five,
    "Six" to Res.drawable.six,
    "Seven" to Res.drawable.seven,
    "Eight" to Res.drawable.eight,
    "Nine" to Res.drawable.nine,
    "Ten" to Res.drawable.ten,
    "Eleven" to Res.drawable.eleven,
    "Twelve" to Res.drawable.twelve,
    "Thirteen" to Res.drawable.thirteen,
    "Fourteen" to Res.drawable.fourteen,
    "Fifteen" to Res.drawable.fifteen,
    "Sixteen" to Res.drawable.sixteen,
    "Seventeen" to Res.drawable.seventeen,
    "Eighteen" to Res.drawable.eighteen,
    "Nineteen" to Res.drawable.nineteen,
    "Twenty" to Res.drawable.twenty,
    "Twenty_One" to Res.drawable.twentyone,
    "Twenty_Two" to Res.drawable.twentytwo,
    "Twenty_Three" to Res.drawable.twentythree,
    "Twenty_Four" to Res.drawable.twentyfour,
    "Twenty_Five" to Res.drawable.twentyfive,
    "Twenty_Six" to Res.drawable.twentysix,
    "Twenty_Seven" to Res.drawable.twentyseven,
    "Twenty_Eight" to Res.drawable.twentyeight,
    "Twenty_Nine" to Res.drawable.twentynine
)

@OptIn(ExperimentalComposeUiApi::class)
fun main() {
    startDateTimeWatcher()
    startRamUsageWatcher()
    startNetUsageWatcher()

    ComposeViewport(document.body!!) {
//        LaunchedEffect(true){
//            contentFetcher("/test.html"){
//                fileSystemLogs.value+=it.responseValue
//            }
//        }
        CompositionLocalProvider(LocalLayerContainer provides document.getElementById("components")!!){
            App()
        }

    }

}