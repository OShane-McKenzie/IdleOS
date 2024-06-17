import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.*
import objects.AppNavigator
import objects.AppProvider
import objects.ContentProvider
import objects.LayoutConfigurator

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