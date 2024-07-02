package objects

import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.runtime.*
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.PointerEventType
import androidx.compose.ui.input.pointer.isSecondaryPressed
import androidx.compose.ui.input.pointer.pointerInput

import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.zIndex
import apps.IdleFileManager
import apps.IdleSettings
import apps.IdleTerminal
import components.IdleAppContainer
import contentProvider
import idleos.composeapp.generated.resources.Res
import idleos.composeapp.generated.resources.file_manager
import idleos.composeapp.generated.resources.settings
import idleos.composeapp.generated.resources.terminal
import models.IdleAppModel
import toRoundedInt
import kotlin.math.roundToInt

class AppProvider {
    val appList:SnapshotStateList<IdleAppModel> = mutableStateListOf()
    val addedApps:SnapshotStateList<String> = mutableStateListOf()
    val runningApps:SnapshotStateList<String> = mutableStateListOf()
    val selectedApp = mutableStateOf("")
    init {
        appList.add(
            IdleAppModel().apply {
                this.name = "Settings"
                this.app = { IdleSettings() }
                this.icon = Res.drawable.settings
            }
        )
        appList.add(
            IdleAppModel().apply {
                this.name = "File Manager"
                this.icon = Res.drawable.file_manager
                this.app = { IdleFileManager() }
            }
        )
        appList.add(
            IdleAppModel().apply {
                this.name = "Terminal"
                this.icon = Res.drawable.terminal
                this.app = { IdleTerminal() }
            }
        )
    }
    fun startApp(id:String){
        if(id !in addedApps){
            addedApps.add(id)

        }
        if(id !in contentProvider.launchedDockItems){
            contentProvider.launchedDockItems.add(id)
        }
    }
    fun stopApp(id:String){
        if(id in addedApps){
            addedApps.remove(id)
        }
        if(id in contentProvider.launchedDockItems){
            contentProvider.launchedDockItems.remove(id)
        }
    }

    @Composable
    fun Show(){
        val displayedApps by remember { derivedStateOf { addedApps.toList() } }
        val interactionSource = remember { MutableInteractionSource() }

        Box(modifier = Modifier.fillMaxSize()) {
            appList.forEach { app ->
                if (displayedApps.contains(app.name)) {
                    var isActive by remember { mutableStateOf(true) }
                    if(selectedApp.value == ""){
                        selectedApp.value = app.name
                    }
                    if(displayedApps.size == 1){
                        selectedApp.value = app.name
                    }
                    LaunchedEffect(app.name){
                        selectedApp.value = app.name
                    }
                    IdleAppContainer(
                        modifier = Modifier
                            .offset { IntOffset(app.offsetX.value.roundToInt(), app.offsetY.value.roundToInt()) }
                            .clickable(
                                interactionSource = interactionSource,
                                indication = null
                            ) {
                                selectedApp.value = app.name
                            }
                            .pointerInput(Unit) {
                                detectDragGestures { change, dragAmount ->
                                    change.consume()
                                    app.offsetX.value += dragAmount.x
                                    app.offsetY.value += dragAmount.y
                                }
                                detectTapGestures { }
                                awaitPointerEventScope {
                                    while (true) {
                                        val event = awaitPointerEvent()
                                        if (event.type == PointerEventType.Press && event.buttons.isSecondaryPressed) {
                                            val x = 0
                                        }
                                    }
                                }
                            }
                            .zIndex(
                                if(selectedApp.value==app.name){
                                    isActive = true
                                    1f
                                }else{
                                    isActive = false
                                    0f
                                }
                            )
                            .align(app.alignment.value),
                        onMaximize = {
                            app.offsetX.value = 0f
                            app.offsetY.value = 0f
                            app.alignment.value = Alignment.Center
                        },
                        onMinimize = {
                            app.offsetX.value = 0f
                            app.offsetY.value = 0f
                            app.alignment.value = Alignment.Center
                        },
                        onCloseRequest = {
                            stopApp(app.name)
                        },
                        app = app,
                        displayWindow = addedApps.contains(app.name),
                        isMoving = app.offsetX.value != 0f || app.offsetY.value != 0f,
                        isInactive = !isActive
                    )
                }
            }
        }
    }
}
