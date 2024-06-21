package objects

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.runtime.snapshots.SnapshotStateMap
import androidx.compose.runtime.toMutableStateList
import contentProvider
import fileSystem
import fileSystemLogs
import models.IdleDirectory
import models.IdleFile
import wallpaperMap

class IdleFileSystem {
    val directoryMap:SnapshotStateMap<String, IdleDirectory> = mutableStateMapOf("" to getRootFileSystem())
    fun addDirs(dirs: List<String>, permission: Permission = Permission.READ_WRITE_EXECUTE): IdleDirectory {
        val dirModel = IdleDirectory(
            files = mutableStateListOf(),
            name = mutableStateOf(""),
            dirs = mutableStateListOf(),
            permission = mutableStateOf(Permission.READ_WRITE_EXECUTE)
        )
        dirs.forEach {
            val newDir = if (it in directoryModels) {
                directoryModels[it] ?: IdleDirectory(
                    name = mutableStateOf(it),
                    permission = mutableStateOf(permission),
                    files = mutableStateListOf(),
                    dirs = mutableStateListOf()
                )
            } else {
                IdleDirectory(
                    name = mutableStateOf(it),
                    permission = mutableStateOf(permission),
                    files = mutableStateListOf(),
                    dirs = mutableStateListOf()
                )
            }

            dirModel.dirs.add(newDir)
            directoryMap[it] = newDir
        }
        return dirModel
    }

    fun addFiles(files: List<String>, permission: Permission, types: FileTypes):IdleDirectory {
        val dir = IdleDirectory(
            files = mutableStateListOf(),
            name = mutableStateOf(""),
            dirs = mutableStateListOf(),
            permission = mutableStateOf(Permission.READ_WRITE_EXECUTE)
        )
        files.forEach {
            dir.files.add(
                IdleFile(
                    type = mutableStateOf(types),
                    name = mutableStateOf(it),
                    permission = mutableStateOf(permission),
                    content = mutableStateOf(it)
                )
            )
        }
        return dir
    }

    val rootDirectory = listOf(
        "bin", "dev", "etc", "lib", "lost+found", "sbin","opt", "root", "sys", "usr",
        "boot", "efi", "home", "lib64", "mnt", "proc", "run", "srv", "tmp", "var", ".Wallpapers"
    )

    val binFiles = listOf(
        "bash", "sh", "cat", "chmod", "chown", "cp", "date", "dd", "df", "dmesg", "echo",
        "false", "hostname", "kill", "ln", "login", "ls", "mkdir", "mknod", "more", "mount",
        "mv", "ps", "pwd", "rm", "rmdir", "sed", "sh", "stty", "sync", "true", "umount", "uname"
    )
    val usrBinFiles = listOf(
        "awk", "bc", "cmp", "diff", "find", "grep", "gzip", "head", "less", "man", "nano",
        "perl", "python", "scp", "ssh", "sort", "sudo", "tail", "tar", "vi", "wget", "who", "zip"
    )
    val procDirs = listOf(
        "fs", "bus", "irq", "sys", "net", "driver", "acpi", "tty"
    )

    val procFiles = listOf(
        "cmdline", "cpuinfo", "devices", "diskstats", "filesystems", "loadavg", "meminfo",
        "mounts", "partitions", "stat", "swaps", "uptime", "version"
    )

    val homeDirs = listOf(
        "Desktop", "Documents", "Downloads", "Music", "Videos", "Public", "Templates"
    )

    val homeFiles = listOf(
        ".bashrc", ".profile", ".bash_profile", ".bash_logout", ".ssh", ".gitconfig", ".vimrc", ".nanorc"
    )
    val pictureFiles = listOf(
        "One", "Two", "Three", "Four", "Five", "Six", "Seven", "Eight", "Nine", "Ten", "Eleven",
        "Twelve", "Thirteen", "Fourteen", "Fifteen", "Sixteen", "Eighteen", "Nineteen", "Twenty"
    )

    var directoryModels = mapOf("" to IdleDirectory(
        files = mutableStateListOf(),
        name = mutableStateOf(""),
        dirs = mutableStateListOf(),
        permission = mutableStateOf(Permission.READ_WRITE_EXECUTE)
    ))

    private var rootFileSystem = IdleDirectory(
        files = mutableStateListOf(),
        name = mutableStateOf(""),
        dirs = mutableStateListOf(),
        permission = mutableStateOf(Permission.READ_WRITE_EXECUTE)
    )

    init {
        directoryModels = mapOf(
            ".Wallpapers" to IdleDirectory(
                name = mutableStateOf(".Wallpapers"),
                files = addFiles(pictureFiles, Permission.READ, types = FileTypes.IMAGE).files.toMutableStateList(),
                dirs = mutableStateListOf(),
                permission = mutableStateOf(Permission.HIDDEN)
            ),
            "home" to IdleDirectory(
                files = addFiles(homeFiles, Permission.READ, types = FileTypes.IDLE_DOC).files.toMutableStateList(),
                name = mutableStateOf("home"),
                dirs = addDirs(homeDirs).dirs.toMutableStateList(),
                permission = mutableStateOf(Permission.READ_WRITE_EXECUTE)
            ),

            "proc" to IdleDirectory(
                name = mutableStateOf("proc"),
                files = addFiles(procFiles, Permission.READ, types = FileTypes.IDLE_DOC).files.toMutableStateList(),
                dirs = addDirs(procDirs, Permission.READ).dirs.toMutableStateList(),
                permission = mutableStateOf(Permission.READ_WRITE_EXECUTE)
            ),
            "bin" to IdleDirectory(
                name = mutableStateOf("bin"),
                files = addFiles(binFiles, Permission.READ_WRITE_EXECUTE, types = FileTypes.EXECUTABLE).files.toMutableStateList(),
                dirs = mutableStateListOf(),
                permission = mutableStateOf(Permission.READ_WRITE_EXECUTE)
            ),
            "usr" to IdleDirectory(
                name = mutableStateOf("usr"),
                files = mutableStateListOf(),
                dirs = mutableStateListOf(),
                permission = mutableStateOf(Permission.READ_WRITE_EXECUTE)
            ).apply {
                this.dirs.add(
                    IdleDirectory(
                        name = mutableStateOf("bin"),
                        files = addFiles(usrBinFiles, Permission.READ_WRITE_EXECUTE, types = FileTypes.EXECUTABLE).files.toMutableStateList(),
                        dirs = mutableStateListOf(),
                        permission = mutableStateOf(Permission.READ_WRITE_EXECUTE)
                    )
                )
            }
        )

        rootFileSystem = IdleDirectory(
            name = mutableStateOf("/"),
            files = mutableStateListOf(),
            permission = mutableStateOf(Permission.READ_WRITE_EXECUTE),
            dirs = addDirs(rootDirectory).dirs.toMutableStateList()
        )
    }

    fun getRootFileSystem():IdleDirectory{
        return rootFileSystem
    }

    fun searchDirectory(dirName: String, startDir: IdleDirectory = rootFileSystem): IdleDirectory? {
        if (startDir.name.value == dirName) {
            return startDir
        }
        for (dir in startDir.dirs) {
            val result = searchDirectory(dirName, dir)
            if (result != null) {
                return result
            }
        }
        return null
    }

    fun getDebugInfo(){
        val root = getRootFileSystem()
        val home = root.dirs.find { it.name.value == "home" }
        val pictures = home?.dirs?.find { it.name.value == "Pictures" }

//        fileSystemLogs.value += "Root directories: ${root.dirs.map { it.name.value }}\n\n"
//        println("Root directories: ${root.dirs.map { it.name.value }}")
//        home?.let {
//            fileSystemLogs.value += "Home directories: ${it.dirs.map { dir -> dir.name.value }}\n"
//            println("Home directories: ${it.dirs.map { dir -> dir.name.value }}")
//        }
        pictures?.let {
            fileSystemLogs.value += "Pictures files: ${it.files.map { file -> file.name.value }}\n\n"
            fileSystemLogs.value += "Pictures dirs: ${it.dirs.map { file -> file.name.value }}\n\n"
            println("Pictures files: ${it.files.map { file -> file.name.value }}")
        }
        //fileSystemLogs.value += "${directoryMap.keys.map { it }}\n\n${directoryMap.keys.map { directoryMap[it]?.name?.value }}"
    }

}

