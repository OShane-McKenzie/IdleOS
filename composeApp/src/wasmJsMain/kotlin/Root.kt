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
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import components.DockItem
import components.IdleAppContiner
import components.PanelWidget
import components.SimpleAnimator
import idleos.composeapp.generated.resources.*
import idleos.composeapp.generated.resources.Res
import kotlinx.coroutines.*
import objects.AnimationStyle
import objects.ParentConfig
import org.jetbrains.compose.resources.painterResource
import osComponents.*
import kotlin.math.roundToInt

class Root {

    @Composable
    fun Parent(){
        val interactionSource = remember { MutableInteractionSource() }
        val indication = LocalIndication.current
        var controlCenterOffsetX by rememberSaveable {
            mutableStateOf(1.0f)
        }
        var offsetX by remember { mutableStateOf(0f) }
        var offsetY by remember { mutableStateOf(0f) }
        var controlCenterOffsetY by rememberSaveable {
            mutableStateOf(1.0f)
        }

        var osContextMenuOffsetX by rememberSaveable {
            mutableStateOf(1.0f)
        }
        var osContextMenuOffsetY by rememberSaveable {
            mutableStateOf(1.0f)
        }

        var calendarOffsetX by rememberSaveable {
            mutableStateOf(1.0f)
        }
        var calendarOffsetY by rememberSaveable {
            mutableStateOf(1.0f)
        }

        var infoCenterOffsetX by rememberSaveable {
            mutableStateOf(1.0f)
        }
        var infoCenterOffsetY by rememberSaveable {
            mutableStateOf(1.0f)
        }

        var osInfoCenterOffsetX by rememberSaveable {
            mutableStateOf(1.0f)
        }
        var osInfoCenterOffsetY by rememberSaveable {
            mutableStateOf(1.0f)
        }

        var showControlCenter by rememberSaveable {
            mutableStateOf(false)
        }
        var showOsInfo by rememberSaveable {
            mutableStateOf(false)
        }
        var showInfoCenter by rememberSaveable {
            mutableStateOf(false)
        }
        var showCalendar by rememberSaveable {
            mutableStateOf(false)
        }

        var showWallpaperPicker by remember {
            mutableStateOf(false)
        }

        var showOsContextMenu by remember {
            mutableStateOf(false)
        }

        var dockHeight by remember { mutableFloatStateOf(1.0f) }
        var panelHeight by remember { mutableFloatStateOf(1.0f) }

        var rightClickOffset by remember { mutableStateOf<Offset?>(null) }
        var wallpaper by remember {
            mutableStateOf(Res.drawable.one)
        }

        fun turnOnWidget(id:String = ""){
            when(id){
                "infoCenter"->{
                    showInfoCenter = !showInfoCenter
                    showControlCenter = false
                    showCalendar = false
                    showOsInfo = false
                }
                "controlCenter"->{
                    showControlCenter = !showControlCenter
                    showInfoCenter = false
                    showCalendar = false
                    showOsInfo = false
                }
                "calendar"->{
                    showCalendar = !showCalendar
                    showControlCenter = false
                    showInfoCenter = false
                    showOsInfo = false
                }
                "osInfo"->{
                    showOsInfo = !showOsInfo
                    showCalendar = false
                    showControlCenter = false
                    showInfoCenter = false
                }
                "none"->{
                    showCalendar = false
                    showControlCenter = false
                    showInfoCenter = false
                    showOsInfo = false
                    showOsContextMenu = false
                }
            }
        }
        var reloadWallpaper by remember {
            mutableStateOf(false)
        }

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
                                            showOsContextMenu = false
                                        }
                                        delay(20)
                                        withContext(Dispatchers.Main){
                                            osContextMenuOffsetX = rightClickOffset!!.x
                                            osContextMenuOffsetY = rightClickOffset!!.y
                                            showOsContextMenu = true
                                        }
                                    }
                                }
                            }
                        }
                    }
            ){
                Panel(
                    modifier = Modifier.align(Alignment.TopCenter),
                    start = {width, height->
                        panelHeight = height
                        var startWidgetOffsetX by remember { mutableFloatStateOf(1.0f) }
                        var startWidgetOffsetY by remember { mutableFloatStateOf(1.0f) }
                        var widgetId by remember { mutableStateOf("") }
                        PanelWidget(
                            onClick = {
                                osInfoCenterOffsetX = (startWidgetOffsetX-8)
                                osInfoCenterOffsetY = (startWidgetOffsetY *(height*0.18f))
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
                                calendarOffsetX = (middleWidgetOffsetX-((width*4.5f)))
                                calendarOffsetY = (middleWidgetOffsetY *(height*0.18f))
                                turnOnWidget(widgetId)
                            },
                            id = "calendar"
                        ) {  offsetX, offsetY, id->
                            middleWidgetOffsetX = offsetX; middleWidgetOffsetY = offsetY; widgetId = id
                            Text(
                                contentProvider.clockString.value,
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
                                    controlCenterOffsetX = (offsetX-(calculatedWidth*16.3f))
                                    controlCenterOffsetY = (offsetY*(height*0.18f))
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
                                    infoCenterOffsetX = (offsetX-(calculatedWidth*19.5f))
                                    infoCenterOffsetY = (offsetY*(height*0.18f))
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
                    modifier = Modifier.align(Alignment.BottomCenter)
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
                if(showCalendar){
                    IdleCalendar(
                        modifier = Modifier
                            .align(Alignment.TopCenter)
                            .height(43.percentOfParent(ParentConfig.HEIGHT).dp)
                            .width(30.percentOfParent(ParentConfig.WIDTH).dp)
                            .offset { IntOffset(calendarOffsetX.roundToInt(), calendarOffsetY.roundToInt()) }
                    )
                }
                if(showControlCenter){
                    ControlCenter(modifier = Modifier.offset { IntOffset(controlCenterOffsetX.roundToInt(), controlCenterOffsetY.roundToInt()) })
                }
                if(showInfoCenter){
                    InfoCenter(modifier = Modifier.offset { IntOffset(infoCenterOffsetX.roundToInt(), infoCenterOffsetY.roundToInt()) })
                }
                if(showOsInfo){
                    OsInfo(modifier = Modifier.offset { IntOffset(osInfoCenterOffsetX.roundToInt(), osInfoCenterOffsetY.roundToInt()) })
                }

                if(showOsContextMenu){
                    OsContextMenu(
                        modifier = Modifier.offset { IntOffset(osContextMenuOffsetX.roundToInt(), osContextMenuOffsetY.roundToInt()) }
                    ) {
                        showOsContextMenu = false
                        if(it == "Change Wallpaper"){
                            showWallpaperPicker = true
                        }
                    }
                }
                if(showWallpaperPicker){
                    WallpaperPicker(
                        onDismissRequest = {showWallpaperPicker = false},
                        modifier = Modifier.offset { IntOffset(1.percentOfParent(ParentConfig.WIDTH).roundToInt(),(70.percentOfParent(ParentConfig.HEIGHT)).roundToInt()) }
                    ) {
                        wallpaper = it
                        reloadWallpaper = !reloadWallpaper
                    }
                }

                IdleAppContiner(
                    modifier = Modifier.offset { IntOffset(offsetX.roundToInt(), offsetY.roundToInt()) }
                        .pointerInput(Unit) {
                            detectDragGestures { change, dragAmount ->
                                change.consume()
                                offsetX += dragAmount.x
                                offsetY += dragAmount.y
                            }
                            detectTapGestures {  }
                        }.align(Alignment.Center)
                ) {  }
            }
            //Brightness overlay
            Column(modifier = Modifier.fillMaxSize().background(color = Color.Black.copy(alpha = contentProvider.brightness.value))){
            }

        }
    }
}