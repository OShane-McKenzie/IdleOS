import androidx.compose.foundation.Image
import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Menu
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.PointerEventType
import androidx.compose.ui.input.pointer.isSecondaryPressed
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import components.DockItem
import components.IdleAppContainer
import components.PanelWidget
import components.SimpleAnimator
import idleos.composeapp.generated.resources.*
import idleos.composeapp.generated.resources.Res
import kotlinx.coroutines.*
import objects.AnimationStyle
import objects.ParentConfig
import org.jetbrains.compose.resources.painterResource
import osComponents.*
import objects.LayoutValues
import objects.ScreenType

class Root {

    @Composable
    fun Parent(){
        val density = LocalDensity.current

        val interactionSource = remember { MutableInteractionSource() }
        val indication = LocalIndication.current
        var controlCenterOffsetX by rememberSaveable {
            mutableStateOf(1.0f)
        }
        var offsetX by remember { mutableStateOf(0f) }
        var offsetY by remember { mutableStateOf(0f) }
        var appAlignment by remember { mutableStateOf(Alignment.Center) }

        var dockHeight by remember { mutableFloatStateOf(1.0f) }
        var panelHeight by remember { mutableFloatStateOf(1.0f) }

        var rightClickOffset by remember { mutableStateOf<Offset?>(null) }
        var wallpaper by remember {
            mutableStateOf(Res.drawable.one)
        }

        var reloadWallpaper by remember {
            mutableStateOf(false)
        }
        var startApp by remember {
            mutableStateOf(false)
        }

        LaunchedEffect(Unit){
            //required to avoid layout calculations NaN value bug
            delay(2000)
            startApp = true
        }
        Box(modifier = Modifier.fillMaxSize()){
            if(startApp){
                Box(
                    modifier = Modifier.fillMaxSize().onGloballyPositioned {
                        layoutConfigurator.parentWidth.value = it.size.width
                        layoutConfigurator.parentHeight.value = it.size.height
                        layoutConfigurator.parentSize.value = (it.size.width * it.size.height)
                    }
                ){
                    if(reloadWallpaper){
                        SimpleAnimator(style = AnimationStyle.SCALE_IN_CENTER) {
                            Image(
                                painter = painterResource(wallpaper),
                                contentDescription = "Wallpaper",
                                contentScale = ContentScale.FillBounds,
                                modifier = Modifier
                                    .height(layoutConfigurator.parentHeight.value.dp)
                                    .width(layoutConfigurator.parentWidth.value.dp)
                            )
                        }

                    }else{
                        SimpleAnimator(style = AnimationStyle.SCALE_IN_CENTER) {
                            Image(
                                painter = painterResource(wallpaper),
                                contentDescription = "Wallpaper",
                                contentScale = ContentScale.FillBounds,
                                modifier = Modifier
                                    .height(layoutConfigurator.parentHeight.value.dp)
                                    .width(layoutConfigurator.parentWidth.value.dp)
                            )
                        }
                    }
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .clickable(
                                interactionSource = interactionSource,
                                indication = null
                            ) {
                                turnOnWidget("none")
                            }
                            .pointerInput(Unit) {
                                awaitPointerEventScope {
                                    while (true) {
                                        val event = awaitPointerEvent()
                                        if (event.type == PointerEventType.Press && event.buttons.isSecondaryPressed) {
                                            rightClickOffset = event.changes.first().position
                                            CoroutineScope(Dispatchers.Default).launch{
                                                withContext(Dispatchers.Main){
                                                    LayoutValues.showOsContextMenu.value = false
                                                }
                                                delay(20)
                                                withContext(Dispatchers.Main){
                                                    LayoutValues.osContextMenuOffsetX.value = rightClickOffset!!.x
                                                    LayoutValues.osContextMenuOffsetY.value = rightClickOffset!!.y
                                                    LayoutValues.showOsContextMenu.value = true
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                    ){
                        Panel(
                            modifier = Modifier.align(Alignment.TopCenter)
                                .fillMaxWidth()
                                .fillMaxHeight(0.05f),
                            start = {width, height->
                                panelHeight = height
                                var startWidgetOffsetX by remember { mutableFloatStateOf(1.0f) }
                                var startWidgetOffsetY by remember { mutableFloatStateOf(1.0f) }
                                var widgetId by remember { mutableStateOf("") }
                                PanelWidget(
                                    onClick = {
                                        LayoutValues.osInfoCenterOffsetX.value = (1.percentOfParent(ParentConfig.WIDTH,density).toFloat(density))
                                        LayoutValues.osInfoCenterOffsetY.value = ((contentProvider.panelHeight.value+1).percentOfParent(ParentConfig.HEIGHT, density).toFloat(density))
                                        turnOnWidget(widgetId)
                                    },
                                    id = "osInfo"
                                ) { offsetX, offsetY, id->
                                    startWidgetOffsetX = offsetX; startWidgetOffsetY = offsetY; widgetId = id
                                    Text(
                                        appName,
                                        fontFamily = FontFamily.Cursive,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = (height*0.3).sp,
                                        color = contentProvider.globalTextColor.value
                                    )
                                }
                            },
                            middle = {width, height->
                                var middleWidgetOffsetX by remember { mutableFloatStateOf(1.0f) }
                                var middleWidgetOffsetY by remember { mutableFloatStateOf(1.0f) }
                                var widgetId by remember { mutableStateOf("") }
                                PanelWidget(
                                    onClick = {
                                        LayoutValues.calendarOffsetX.value = (0.percentOfParent(ParentConfig.WIDTH, density).toFloat(density))    //(middleWidgetOffsetX-((width*4.5f)))
                                        LayoutValues.calendarOffsetY.value = ((contentProvider.panelHeight.value+1).percentOfParent(ParentConfig.HEIGHT, density).toFloat(density))
                                        turnOnWidget(widgetId)
                                    },
                                    id = "calendar"
                                ) {  offsetX, offsetY, id->
                                    middleWidgetOffsetX = offsetX; middleWidgetOffsetY = offsetY; widgetId = id
                                    Text(
                                        contentProvider.clockString.value +" ${100.percentOfParent(ParentConfig.WIDTH, density).toFloat(density)}",
                                        fontSize = (height*0.25).sp,
                                        color = contentProvider.globalTextColor.value
                                    )
                                }
                            },
                            end = {
                                    width, height->
                                PanelWidget(isWidgetClickable = false, id = "controlCenter") { offsetX, offsetY, id->
                                    val calculatedWidth = (width*0.03f)
                                    IconButton(
                                        onClick = {
                                            controlCenterOffsetX = 10.toFloat()
                                            LayoutValues.controlCenterOffsetY.value = 20f
                                            turnOnWidget(id)
                                        },
                                        modifier = Modifier
                                            .height(height.dp)
                                            .width(calculatedWidth.dp)
                                    ){
                                        Icon(
                                            modifier = Modifier.fillMaxSize(),
                                            imageVector = Icons.Rounded.Settings,
                                            contentDescription = "Control Center",
                                            tint = contentProvider.globalTextColor.value,
                                        )
                                    }
                                }
                                Spacer(modifier = Modifier.width(5.dp))
                                PanelWidget(isWidgetClickable = false, id = "infoCenter") { offsetX, offsetY, id->
                                    val calculatedWidth = (width*0.03f)
                                    IconButton(
                                        onClick = {
                                            LayoutValues.infoCenterOffsetX.value = (offsetX-(calculatedWidth*19.5f))
                                            LayoutValues.infoCenterOffsetY.value = (offsetY*(height*0.18f))
                                            turnOnWidget(id)
                                        },
                                        modifier = Modifier
                                            .height(height.dp)
                                            .width(calculatedWidth.dp)
                                    ){
                                        Icon(
                                            modifier = Modifier.fillMaxSize(),
                                            imageVector = Icons.Rounded.Menu,
                                            contentDescription = "Menu",
                                            tint = contentProvider.globalTextColor.value,
                                        )
                                    }
                                }
                            }
                        )
                        Dock(
                            modifier = Modifier.align(Alignment.BottomCenter).fillMaxWidth().fillMaxHeight(0.08f)
                        ) {
                                width, height->
                            dockHeight = height
                            DockItem(
                                height = (height*0.7f),
                                width = (width*0.45f),
                                id = "Launcher"
                            ){

                            }
                        }
                        if(LayoutValues.showCalendar.value){
                            IdleCalendar(
                                modifier = Modifier
                                    .align(Alignment.TopCenter)
                                    .fillMaxWidth(0.6f)
                                    .fillMaxHeight(0.86f)
                                    //.height(86.percentOfParent(ParentConfig.HEIGHT,density))
                                    //.width(60.percentOfParent(ParentConfig.WIDTH,density))
                                    .offset {
                                        IntOffset(LayoutValues.calendarOffsetX.value.toRoundedInt(),
                                        LayoutValues.calendarOffsetY.value.toRoundedInt())
                                    }
                            )
                        }
                        if(LayoutValues.showControlCenter.value){
                            ControlCenter(
                                modifier = Modifier.offset {
                                IntOffset(controlCenterOffsetX.toRoundedInt(),
                                LayoutValues.controlCenterOffsetY.value.toRoundedInt())
                                }
                            )
                        }
                        if(LayoutValues.showInfoCenter.value){
                            InfoCenter(modifier = Modifier.offset {
                                IntOffset(LayoutValues.infoCenterOffsetX.value.toRoundedInt(),
                                LayoutValues.infoCenterOffsetY.value.toRoundedInt())
                                }
                            )
                        }
                        if(LayoutValues.showOsInfo.value){
                            OsInfo(modifier = Modifier.offset {
                                IntOffset(LayoutValues.osInfoCenterOffsetX.value.toRoundedInt(),
                                LayoutValues.osInfoCenterOffsetY.value.toRoundedInt())
                                }
                            )
                        }

                        if(LayoutValues.showOsContextMenu.value){
                            OsContextMenu(
                                modifier = Modifier.offset {
                                    IntOffset(LayoutValues.osContextMenuOffsetX.value.toRoundedInt(),
                                    LayoutValues.osContextMenuOffsetY.value.toRoundedInt())
                                }
                            ) {
                                LayoutValues.showOsContextMenu.value = false
                                if(it == "Change Wallpaper"){
                                    LayoutValues.showWallpaperPicker.value = true
                                }
                            }
                        }
                        if(LayoutValues.showWallpaperPicker.value){
                            WallpaperPicker(
                                onDismissRequest = {LayoutValues.showWallpaperPicker.value = false},
                                modifier = Modifier.offset {
                                    IntOffset(1.percentOfParent(ParentConfig.WIDTH,density).toInt(density), (70.percentOfParent(ParentConfig.HEIGHT,density).toInt(density)))
                                }
                            ) {
                                wallpaper = it
                                reloadWallpaper = !reloadWallpaper
                            }
                        }
                        IdleAppContainer(
                            modifier = Modifier.offset { IntOffset(offsetX.toRoundedInt(), offsetY.toRoundedInt()) }
                                .pointerInput(Unit) {
                                    detectDragGestures { change, dragAmount ->
                                        change.consume()
                                        offsetX += dragAmount.x
                                        offsetY += dragAmount.y
                                    }
                                    detectTapGestures {  }
                                }.align(appAlignment),
                            onMaximize = {
                                offsetX = 0f
                                offsetY = 0f
                                appAlignment = Alignment.Center
                            },
                            onMinimize = {
                                offsetX = 0f
                                offsetY = 0f
                                appAlignment = Alignment.Center
                            }
                        ) {  }

                    }
                    //Brightness overlay
                    Column(modifier = Modifier
                        .fillMaxSize()
                        .background(color = Color.Black.copy(alpha = contentProvider.brightness.value)
                        )
                    ){
                    }

                }
            }else{
                Text(
                    "Loading please wait...",
                    modifier = Modifier.align(Alignment.Center),
                    fontSize = 32.sp, fontWeight = FontWeight.Bold
                )
            }
        }
    }
}