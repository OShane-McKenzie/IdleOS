package models

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import objects.Permission

data class IdleFile(
    val name:MutableState<String> = mutableStateOf(""),
    val permission: MutableState<Permission> = mutableStateOf(Permission.READ_WRITE_EXECUTE),
    val content:MutableState<String> = mutableStateOf("")
)
data class IdleDirectory(
    val name:MutableState<String> = mutableStateOf(""),
    val permission: MutableState<Permission> = mutableStateOf(Permission.READ_WRITE_EXECUTE),
    val dirs:SnapshotStateList<IdleDirectory> = mutableStateListOf(),
    val files:SnapshotStateList<IdleFile> = mutableStateListOf()
)

//