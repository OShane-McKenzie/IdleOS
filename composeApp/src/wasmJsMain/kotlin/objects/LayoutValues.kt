package objects

import androidx.compose.runtime.mutableStateOf

object LayoutValues {
    val controlCenterOffsetY = mutableStateOf(1.0f)

    val osContextMenuOffsetX = mutableStateOf(1.0f)
    val osContextMenuOffsetY = mutableStateOf(1.0f)
    val osContextMenuWidth = mutableStateOf(1.0f)

    val calendarOffsetX = mutableStateOf(1.0f)
    val calendarOffsetY = mutableStateOf(1.0f)
    val calendarWith = mutableStateOf(1.0f)

    val infoCenterOffsetX = mutableStateOf(1.0f)
    val infoCenterOffsetY = mutableStateOf(1.0f)
    val infoCenterWidth = mutableStateOf(1.0f)

    val osInfoCenterOffsetX = mutableStateOf(1.0f)
    val osInfoCenterOffsetY = mutableStateOf(1.0f)
    val osInfoCenter = mutableStateOf(1.0f)

    val showControlCenter = mutableStateOf(false)
    val showOsInfo = mutableStateOf(false)
    val showInfoCenter = mutableStateOf(false)
    val showCalendar = mutableStateOf(false)

    val showWallpaperPicker = mutableStateOf(false)
    val showOsContextMenu = mutableStateOf(false)
}