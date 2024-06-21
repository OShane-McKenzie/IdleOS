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

val root = Root()


@Composable
fun App() {

    MaterialTheme {
        root.Parent()
    }
}