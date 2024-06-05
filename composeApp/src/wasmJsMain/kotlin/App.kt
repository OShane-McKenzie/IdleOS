import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.*
import objects.ContentProvider
import objects.LayoutConfigurator

val contentProvider = ContentProvider()
val layoutConfigurator = LayoutConfigurator()
val root = Root()
@Composable
fun App() {
    MaterialTheme {
        root.Parent()
    }
}