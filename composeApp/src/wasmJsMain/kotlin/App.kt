import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.*
import jsFeatures.getCpuInfo
import objects.*

val cpuCores = getCpuInfo()
val fileSystemAppNavigator = AppNavigator("/")
val fileSystem = IdleFileSystem()
val contentProvider = ContentProvider()
val layoutConfigurator = LayoutConfigurator()
val settingsAppNavigator = AppNavigator(DockAndPanel)
val appProvider = AppProvider()
val idleTerminalCommands = IdleTerminalCommands()
val root = Root()


@Composable
fun App() {
//    LaunchedEffect(true){
//        var cpu = ""
//        for(i in 0..cpuCores){
//            cpu += getSimulatedCpuInfo(i)
//        }
//        fileSystemLogs.value = cpu
//    }
    MaterialTheme {
        root.Parent()
    }
}