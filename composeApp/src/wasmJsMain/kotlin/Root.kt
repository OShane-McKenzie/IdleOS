import androidx.compose.foundation.*
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.material.Divider
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
import androidx.compose.ui.zIndex
import apps.IdleSettings
import components.DockItem
import components.IdleAppContainer
import components.PanelWidget
import components.SimpleAnimator
import idleos.composeapp.generated.resources.*
import idleos.composeapp.generated.resources.Res
import kotlinx.coroutines.*
import models.IdleAppModel
import objects.*
import org.jetbrains.compose.resources.painterResource
import osComponents.*
import kotlin.math.roundToInt

class Root {

    @Composable
    fun Parent(){
        val density = LocalDensity.current

        val interactionSource = remember { MutableInteractionSource() }
        val indication = LocalIndication.current

        var offsetX by remember { mutableStateOf(0f) }
        var offsetY by remember { mutableStateOf(0f) }
        var appAlignment by remember { mutableStateOf(Alignment.Center) }

        var dockHeight by remember { mutableFloatStateOf(1.0f) }
        var panelHeight by remember { mutableFloatStateOf(1.0f) }

        var rightClickOffset by remember { mutableStateOf<Offset?>(null) }
        var wallpaper by remember {
            mutableStateOf(Res.drawable.twentynine)
        }

        var reloadWallpaper by remember {
            mutableStateOf(false)
        }


        LaunchedEffect(Unit){
            //required to avoid layout calculations NaN value bug
            delay(2000)
            fileSystemLogs.value+="System restarted at: ${getDateTimeToString()}\n"
            contentProvider.startApp.value = true
        }
        LaunchedEffect(layoutConfigurator.parentWidth.value, layoutConfigurator.parentHeight.value){
            if(contentProvider.startApp.value ){
                fileSystemLogs.value+="Adjusting to screen-height: ${layoutConfigurator.parentHeight.value}\n"
                fileSystemLogs.value+="Adjusting to screen-width: ${layoutConfigurator.parentWidth.value}\n"
            }

        }
        Box(modifier = Modifier.fillMaxSize()){
            if(contentProvider.startApp.value ){
                Box(
                    modifier = Modifier.fillMaxSize().onGloballyPositioned {
                        layoutConfigurator.parentWidth.value = with(density){it.size.width.toDp().toInt(density)}
                        layoutConfigurator.parentHeight.value = with(density){it.size.height.toDp().toInt(density)}
                        layoutConfigurator.parentSize.value = (layoutConfigurator.parentWidth.value * layoutConfigurator.parentHeight.value)
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
                                .fillMaxHeight(contentProvider.panelHeightScaleFactor.value)
                                .onGloballyPositioned {
                                    contentProvider.panelHeight.value = with(density){it.size.height.toDp().toFloat(density)}
                                    contentProvider.panelWidth.value = with(density){it.size.width.toDp().toFloat(density)}
                                }
                            ,
                            start = {width, height->
                                panelHeight = height
                                var startWidgetOffsetX by remember { mutableFloatStateOf(1.0f) }
                                var startWidgetOffsetY by remember { mutableFloatStateOf(1.0f) }
                                var widgetId by remember { mutableStateOf("") }
                                PanelWidget(
                                    onClick = {
                                        LayoutValues.osInfoCenterOffsetX.value = 5f
                                        LayoutValues.osInfoCenterOffsetY.value = (contentProvider.panelHeight.value+3)
                                        turnOnWidget(widgetId)
                                    },
                                    id = "osInfo"
                                ) { offsetX, offsetY, id->
                                    startWidgetOffsetX = offsetX; startWidgetOffsetY = offsetY; widgetId = id
                                    Text(
                                        appName,
                                        fontFamily = FontFamily.Cursive,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = ((height*0.5)/2).sp,
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
                                        LayoutValues.calendarOffsetX.value = 0f    //(middleWidgetOffsetX-((width*4.5f)))
                                        LayoutValues.calendarOffsetY.value = (contentProvider.panelHeight.value+3)
                                        turnOnWidget(widgetId)
                                    },
                                    id = "calendar"
                                ) {  offsetX, offsetY, id->
                                    middleWidgetOffsetX = offsetX; middleWidgetOffsetY = offsetY; widgetId = id
                                    Text(
                                        contentProvider.clockString.value,
                                        fontSize = ((height*0.5)/2).sp,
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

                                            LayoutValues.controlCenterOffsetX.value = (contentProvider.panelWidth.value*0.735f)
                                            LayoutValues.controlCenterOffsetY.value = (contentProvider.panelHeight.value+3)
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
                                            LayoutValues.infoCenterOffsetX.value = (contentProvider.panelWidth.value*0.735f)
                                            LayoutValues.infoCenterOffsetY.value = (contentProvider.panelHeight.value+3)
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
                            modifier = Modifier.align(Alignment.BottomCenter).fillMaxWidth().fillMaxHeight(contentProvider.dockHeightScaleFactor.value)
                                .onGloballyPositioned {
                                    contentProvider.dockHeight.value = with(density){it.size.height.toDp().toFloat(density)}
                                    contentProvider.dockWidth.value = with(density){it.size.width.toDp().toFloat(density)}
                                }
                        ) {
                                width, height->
                            dockHeight = height
                            DockItem(
                                modifier = Modifier.fillMaxHeight().wrapContentWidth(),
                                height = (height*0.75f),
                                width = (width*0.02f),
                                id = "Launcher",
                                iconBg = contentProvider.globalColor.value.copy(alpha = 0.8f)
                            ){
                                LayoutValues.showIdleLauncher.value = true
                            }
                            Spacer(modifier = Modifier.width(17.dp))
                            Column(
                                modifier = Modifier
                                    .padding(1.dp)
                                    .width(2.dp)
                                    .fillMaxHeight(),
                                verticalArrangement = Arrangement.Center,
                                horizontalAlignment = Alignment.CenterHorizontally

                            ) {
                                Divider(modifier = Modifier.fillMaxHeight(0.5f), color = contentProvider.globalTextColor.value, thickness = 2.dp)
                            }
                            Spacer(modifier = Modifier.width(17.dp))
                            appProvider.appList.forEach { app ->

                                DockItem(
                                    modifier = Modifier.fillMaxHeight().wrapContentWidth(),
                                    height = (height*0.75f),
                                    width = (width*0.02f),
                                    id = app.name,
                                    painter = painterResource(app.icon)
                                ){
                                    appProvider.startApp(app.name)
                                }
                                Spacer(modifier = Modifier.width(5.dp))
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
                                IntOffset(LayoutValues.controlCenterOffsetX.value.toRoundedInt(),
                                LayoutValues.controlCenterOffsetY.value.toRoundedInt())
                                }.fillMaxWidth(0.26f).fillMaxHeight(0.42f)
                            )
                        }
                        if(LayoutValues.showInfoCenter.value){
                            InfoCenter(modifier = Modifier.offset {
                                IntOffset(LayoutValues.infoCenterOffsetX.value.toRoundedInt(),
                                LayoutValues.infoCenterOffsetY.value.toRoundedInt())
                                }.fillMaxWidth(0.26f).fillMaxHeight(0.84f)
                            )
                        }
                        if(LayoutValues.showOsInfo.value){
                            OsInfo(modifier = Modifier.offset {
                                IntOffset(LayoutValues.osInfoCenterOffsetX.value.toRoundedInt(),
                                LayoutValues.osInfoCenterOffsetY.value.toRoundedInt())
                                }.fillMaxWidth(0.16f).fillMaxHeight(0.2f)
                            )
                        }

                        if(LayoutValues.showOsContextMenu.value){
                            OsContextMenu(
                                modifier = Modifier.offset {
                                    IntOffset(LayoutValues.osContextMenuOffsetX.value.toRoundedInt(),
                                    LayoutValues.osContextMenuOffsetY.value.toRoundedInt())
                                }.fillMaxWidth(0.16f).fillMaxHeight(0.2f)
                            ) {
                                LayoutValues.showOsContextMenu.value = false
                                if(it == "Change Wallpaper"){
                                    LayoutValues.showWallpaperPicker.value = true
                                }
                                if(it=="Settings"){
                                    settingsAppNavigator.setViewState(DockAndPanel)
                                    contentProvider.settingsAppControllerIndex.value = idleSettingsScreens.indexOf(settingsAppNavigator.getView())
                                    appProvider.startApp("Settings")
                                }
                            }
                        }
                        if(contentProvider.showAbout.value){
                            var moveX by remember { mutableStateOf(0f) }
                            var moveY by remember { mutableStateOf(0f) }
                            IdleAbout(
                                modifier = Modifier
                                    .offset { IntOffset(moveX.roundToInt(), moveY.roundToInt()) }
                                    .pointerInput(Unit) {
                                        detectDragGestures { change, dragAmount ->
                                            change.consume()
                                            moveX += dragAmount.x
                                            moveY += dragAmount.y
                                        }
                                        detectTapGestures { }
                                    }
                                    .fillMaxWidth(0.25f)
                                    .fillMaxHeight(0.5f)
                                    .align(Alignment.Center)
                            ){
                                contentProvider.showAbout.value = false
                            }
                        }
                        if(LayoutValues.showWallpaperPicker.value){

                            WallpaperPicker(
                                onDismissRequest = {LayoutValues.showWallpaperPicker.value = false},
                                modifier = Modifier.offset {
                                    IntOffset(10, layoutConfigurator.parentHeight.value-(layoutConfigurator.parentHeight.value*0.3f).toInt())
                                }
                                    .fillMaxWidth(0.98f).fillMaxHeight(0.2f).align(Alignment.TopCenter)
                            ) {
                                wallpaper = it
                                reloadWallpaper = !reloadWallpaper
                            }
                        }

                        appProvider.Show()
                        IdleLauncher()
                    }
                    //Brightness overlay
                    Column(modifier = Modifier
                        .fillMaxSize()
                        .background(color = Color.Black.copy(alpha = contentProvider.brightness.value)
                        )
                    ){
                    }

                    if(contentProvider.isLoggedOut.value){
                        turnOnWidget("none")
                        LockScreen()
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