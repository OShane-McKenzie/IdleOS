const val appName = "IdleOS"
const val DockAndPanel = "Dock and Panel"
const val ColorTheme = "Color Theme"
const val Transparency = "Transparency"
val idleSettingsScreens = listOf(
    DockAndPanel,
    ColorTheme,
    Transparency
)

const val about:String = """
Welcome to IdleOS! IdleOS is a simulated desktop environment built using Kotlin/WASM and Jetpack Compose. For more information on Kotlin/WASM, check out the Kotlin/WASM.

IdleOS includes a variety of core features you would expect from a desktop environment, such as a panel, dock, terminal, file manager, settings app, control center, info center, and more. Our aim is to create a fun and customizable desktop experience that is accessible and enjoyable for all users.

We are actively seeking contributors to help us enhance IdleOS, especially in developing new features like a browser and a text editor.
"""