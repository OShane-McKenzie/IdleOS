import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.window.ComposeViewport
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
    "Eighteen" to Res.drawable.eighteen,
    "Nineteen" to Res.drawable.nineteen,
    "Twenty" to Res.drawable.twenty
)
@OptIn(ExperimentalComposeUiApi::class)
fun main() {
    startDateTimeWatcher()
    startRamUsageWatcher()
    startNetUsageWatcher()
    ComposeViewport(document.body!!) {
        App()
    }
}