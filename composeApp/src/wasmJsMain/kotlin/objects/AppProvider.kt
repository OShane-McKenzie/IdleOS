package objects

import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.runtime.*
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.IntOffset
import apps.IdleSettings
import components.IdleAppContainer
import contentProvider
import idleos.composeapp.generated.resources.Res
import idleos.composeapp.generated.resources.settings
import models.IdleAppModel
import toRoundedInt
import kotlin.math.roundToInt

class AppProvider {
    val appList:SnapshotStateList<IdleAppModel> = mutableStateListOf()
    val addedApps:SnapshotStateList<String> = mutableStateListOf()
    val runningApps:SnapshotStateList<String> = mutableStateListOf()
    init {
        appList.add(
            IdleAppModel().apply {
                this.name = "Settings"
                this.app = { IdleSettings() }
                this.icon = Res.drawable.settings
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

        Box(modifier = Modifier.fillMaxSize()) {
            appList.forEach { app ->
                if (displayedApps.contains(app.name)) {
                    IdleAppContainer(
                        modifier = Modifier
                            .offset { IntOffset(app.offsetX.value.roundToInt(), app.offsetY.value.roundToInt()) }
                            .pointerInput(Unit) {
                                detectDragGestures { change, dragAmount ->
                                    change.consume()
                                    app.offsetX.value += dragAmount.x
                                    app.offsetY.value += dragAmount.y
                                }
                                detectTapGestures { }
                            }.align(app.alignment.value),
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
                        isMoving = app.offsetX.value != 0f || app.offsetY.value != 0f
                    )
                }
            }
        }
    }
}
