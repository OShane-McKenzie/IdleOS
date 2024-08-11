package models

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import fileSystemLogs
import idleos.composeapp.generated.resources.*
import idleos.composeapp.generated.resources.Res
import idleos.composeapp.generated.resources.file
import idleos.composeapp.generated.resources.file_exec
import idleos.composeapp.generated.resources.folder
import objects.FileTypes
import objects.FileTypes.*
import objects.Permission
import org.jetbrains.compose.resources.DrawableResource
import wallpaperMap
import kotlin.math.abs
import kotlinx.serialization.Serializable

@Serializable
data class User(val name: String, val age: Int)

data class IdleFile(
    val id:String = abs((0..999999999999999).random()).toString(),
    var name:MutableState<String>,
    var permission: MutableState<Permission>,
    var content:MutableState<String>,
    var type:MutableState<FileTypes>,
    var path:String = "/${name.value}",
    var icon:MutableState<DrawableResource> = try{
        when (type.value) {
            IMAGE -> {
                mutableStateOf(wallpaperMap[name.value]!!)
            }

            IDLE_DOC -> {
                mutableStateOf(Res.drawable.file)
            }

            EXECUTABLE -> {
                mutableStateOf(Res.drawable.file_exec)
            }

            else -> {
                mutableStateOf(Res.drawable.logo_2)
            }
        }
    }catch (e:Exception){
        fileSystemLogs.value += "$e\n ${e.printStackTrace()}"
        mutableStateOf(Res.drawable.logo_2)
    }
)

data class IdleDirectory(
    val id:String = abs((0..999999999999999).random()).toString(),
    var name:MutableState<String>,
    var permission: MutableState<Permission>,
    var dirs:SnapshotStateList<IdleDirectory>,
    var files:SnapshotStateList<IdleFile>,
    var path:String = "/${name.value}",
    var icon:MutableState<DrawableResource> = mutableStateOf(Res.drawable.folder)
)

