package objects

import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.graphics.Color
import fileSystem
import models.IdleDirectory

class ContentProvider {
    val defaultPanelHeightScaleFactor = 0.05f
    val defaultDockHeightScaleFactor = 0.08f
    val defaultGlobalTransparency = 0.7f
    val settingsAppControllerIndex = mutableStateOf(0)
    val globalColor = mutableStateOf(Color(0xffffffff))
    val globalTransparency = mutableFloatStateOf(defaultGlobalTransparency)
    val globalTextColor = mutableStateOf(Color(0xff000000))
    val activeDockItemIndicator = mutableStateOf(Color(0xff000000))
    val inactiveDockItemIndicator = mutableStateOf(Color(0xff706e6e))
    val launchedDockItems:SnapshotStateList<String> = mutableStateListOf()
    val dockHeight = mutableFloatStateOf(8.0f)
    val dockWidth = mutableFloatStateOf(100.0f)
    val panelHeight = mutableFloatStateOf(5.0f)
    val panelWidth = mutableFloatStateOf(100.0f)
    val clockString = mutableStateOf("")
    val brightnessControl = mutableFloatStateOf(1.0f)
    val brightness = mutableFloatStateOf(0.0f)
    val ramUsageString = mutableStateOf("")
    val netUsageString = mutableStateOf("")
    val widgetList:SnapshotStateList<String> = mutableStateListOf()
    val panelHeightScaleFactor = mutableStateOf(defaultPanelHeightScaleFactor)
    val dockHeightScaleFactor = mutableStateOf(defaultDockHeightScaleFactor)
    val presentWorkingDirectory = mutableStateOf(fileSystem.getRootFileSystem())
    val currentPath = mutableStateOf("")
    val lastAddedToPath = mutableStateOf("")
    val previousPath = mutableStateOf("")


}