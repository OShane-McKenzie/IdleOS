package objects

import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.graphics.Color

class ContentProvider {
    val globalColor = mutableStateOf(Color(0xffffffff))
    val globalTransparency = mutableFloatStateOf(0.6f)
    val globalTextColor = mutableStateOf(Color(0xff000000))
    val activeDockItemIndicator = mutableStateOf(Color(0xff000000))
    val inactiveDockItemIndicator = mutableStateOf(Color(0xff706e6e))
    val launchedDockItems:SnapshotStateList<String> = mutableStateListOf()
    val dockHeight = mutableFloatStateOf(4.0f)
    val dockWidth = mutableFloatStateOf(100.0f)
    val panelHeight = mutableFloatStateOf(2.3f)
    val panelWidth = mutableFloatStateOf(100.0f)
    val clockString = mutableStateOf("")
    val brightnessControl = mutableFloatStateOf(1.0f)
    val brightness = mutableFloatStateOf(0.0f)
    val ramUsageString = mutableStateOf("")
    val netUsageString = mutableStateOf("")
    val widgetList:SnapshotStateList<String> = mutableStateListOf()
}