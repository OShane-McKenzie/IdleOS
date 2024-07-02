package objects

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import contentProvider
import cpuCores
import fileSystem
import getSimulatedCpuInfo
import getSimulatedMemInfo
import kotlinx.coroutines.*
import models.IdleDirectory
import models.IdleFile

class IdleTerminalCommands {
    private val rootFileSystem = fileSystem.getRootFileSystem()
    private val directoryMap = fileSystem.directoryMap
    private val procDir = directoryMap["proc"]!!

    private var cpuInfoFile = IdleFile(
        name = mutableStateOf("cpuinfo"),
        permission = mutableStateOf(Permission.READ),
        type = mutableStateOf(FileTypes.IDLE_DOC),
        content = mutableStateOf("")
    )
    private var memInfoFile = IdleFile(
        name = mutableStateOf("meminfo"),
        permission = mutableStateOf(Permission.READ),
        type = mutableStateOf(FileTypes.IDLE_DOC),
        content = mutableStateOf("")
    )

    private fun cpuInfo(): String {
        var cpu = ""
        for (i in 0..cpuCores) {
            cpu += getSimulatedCpuInfo(i)
        }
        return cpu
    }

    val commandList = listOf(
        "cat ",
        "reboot",
        "ls ",
        "logout",
        "clear"
    )

    val idleOutputStream = mutableStateOf("")
    val idleInputStream = mutableStateOf("")

    init {
        directoryMap.remove("")
        cpuInfoFile = procDir.files.find { it.name.value == "cpuinfo" }!!
        cpuInfoFile.content.value = cpuInfo()
        memInfoFile = procDir.files.find { it.name.value == "meminfo" }!!
        memInfoFile.content.value = getSimulatedMemInfo()
    }

    fun inputParser(input: String) {
        var command = ""
        var arguments = ""
        commandList.forEach {
            if (input.trim().startsWith(it)) {
                if (listOf("clear", "reboot", "logout").contains(input.trim())) {
                    command = input.trim()
                    return@forEach
                }
                command = it
                arguments = input.removePrefix(command).trim()
                return@forEach
            }else if(input.trimStart() == it.trim() || input.trim()+" " == it){
                command = it
            }
        }

        when (command) {
            "cat " -> {
                val pathList = arguments.split("/").toMutableList()
                pathList.add(0,"/")
                pathList.remove("")
                val pathListSize = pathList.size
                var contents: IdleFile? = null
                var currentDir: IdleDirectory? = contentProvider.presentWorkingDirectory.value

                if (pathListSize < 1) {
                    idleOutputStream.value += "Error: no file given.\n"
                    idleInputStream.value = ""
                    return
                }

                pathList.forEachIndexed { index, item ->
                    if (index != pathList.lastIndex) {
                        if (item.isNotEmpty()){
                            currentDir = directoryMap[item]
                        }
                        if (currentDir == null) {
                            return@forEachIndexed
                        }
                    }
                }

                if (pathList.last() in directoryMap) {
                    idleOutputStream.value += "Error: is a directory.\n"
                    idleInputStream.value = ""
                    return
                }

                if (currentDir == null) {
                    idleOutputStream.value += "Error: no such file.\n"
                    idleInputStream.value = ""
                } else {
                    contents = currentDir!!.files.find { it.name.value == pathList.last() }
                    if (contents != null) {
                        idleOutputStream.value += contents.content.value + "\n"
                        idleInputStream.value = ""
                    } else {
                        idleOutputStream.value += "Error: no such file.\n"
                        idleInputStream.value = ""
                    }
                }
            }
            "reboot" -> {
                CoroutineScope(Dispatchers.Default).launch {
                    withContext(Dispatchers.Main) {
                        contentProvider.startApp.value = false
                    }
                    delay(2000)
                    withContext(Dispatchers.Main) {
                        contentProvider.startApp.value = true
                    }
                }
            }
            "logout" -> {
                contentProvider.isLoggedOut.value = true
                idleInputStream.value = ""
            }
            "ls " -> {
                val pathList = arguments.split("/").toMutableList()
                pathList.add(0,"/")
                pathList.remove("")
                var currentDir: IdleDirectory? = contentProvider.presentWorkingDirectory.value

                pathList.forEach { item ->
                    if (item.isNotEmpty()){
                        currentDir = directoryMap[item]
                    }
                    if (currentDir == null) {
                        idleOutputStream.value += "Error: no such directory. \n"
                        idleInputStream.value = ""
                        return
                    }
                }

                currentDir?.let {
                    val contents = it.files.joinToString(" ") { file -> file.name.value } + "\n" +
                            it.dirs.joinToString(" ") { dir -> dir.name.value }
                    idleOutputStream.value += contents + "\n"
                    idleInputStream.value = ""
                }
            }
            "clear" -> {
                idleOutputStream.value = ""
                idleInputStream.value = ""
            }
        }
    }
}