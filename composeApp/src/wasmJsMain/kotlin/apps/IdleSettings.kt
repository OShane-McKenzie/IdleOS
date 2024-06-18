package apps

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import components.ColorButton
import contentProvider
import idleSettingsScreens
import objects.Sizes
import richColors
import settingsAppNavigator

@Composable
fun IdleSettings(modifier: Modifier = Modifier){
    val scrollState = rememberScrollState()
    Box(){
        Row(modifier = modifier) {
            Column(
                modifier = Modifier
                    .fillMaxHeight()
                    .fillMaxWidth(0.25f)
                    .padding(3.dp)
                    .verticalScroll(scrollState)
                    .background(
                        color = contentProvider.globalColor.value.copy(alpha = contentProvider.globalTransparency.value),
                        shape = RoundedCornerShape(5)
                    ),
                verticalArrangement = Arrangement.SpaceEvenly,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                idleSettingsScreens.forEachIndexed { index, s ->
                    SettingsButton(index = index, label = s)
                    Spacer(modifier = Modifier.height(3.dp))
                }
            }
            Column(
                modifier = Modifier
                    .fillMaxHeight()
                    .fillMaxWidth()
                    .padding(3.dp)
                    .background(
                        color = contentProvider.globalColor.value,
                        shape = RoundedCornerShape(5)
                    )
            ) {
                when (settingsAppNavigator.getView()) {
                    idleSettingsScreens[0] -> {
                        DPSize()
                    }
                    idleSettingsScreens[1] -> {
                        ColorThemeSettings(modifier = Modifier.fillMaxSize().padding(3.dp))
                    }
                    idleSettingsScreens[2] -> {
                        DPTransparency()
                    }
                }
            }
        }
        VerticalScrollbar(modifier = Modifier.align(Alignment.CenterStart), adapter = rememberScrollbarAdapter(scrollState))
    }
}
@Composable
private fun SettingsButton(modifier: Modifier = Modifier,label:String, index:Int){

    Row(modifier = Modifier.padding(8.dp)){
        Row(
            modifier =
            modifier
                .height(Sizes.eightyNine.dp)
                .fillMaxWidth()
                .background(color = contentProvider.globalColor.value, shape = RoundedCornerShape(5))
                .border(
                    width = 2.dp, color = if (contentProvider.settingsAppControllerIndex.value == index) {
                        contentProvider.globalTextColor.value
                    } else {
                        contentProvider.globalTextColor.value.copy(alpha = 0.0f)
                    },
                    shape = RoundedCornerShape(5)
                )
                .padding(8.dp)
                .clickable {
                    contentProvider.settingsAppControllerIndex.value = index
                    settingsAppNavigator.setViewState(label, updateHistory = false)
                },
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Text(
                label,
                fontWeight = FontWeight.Bold,
                color = contentProvider.globalTextColor.value,
                fontSize = Sizes.twentyOne.sp
            )
        }
    }
}
@Composable
private fun ColorThemeSettings(modifier: Modifier = Modifier){
    val panelColorScroll = rememberScrollState()
    val textColorScroll = rememberScrollState()
    val indicatorColorScroll = rememberScrollState()
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.SpaceEvenly,
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        Spacer(modifier = Modifier.height(3.dp))
        Row(
            modifier = Modifier.fillMaxWidth().wrapContentHeight(),
            horizontalArrangement = Arrangement.Center
        ) { Text("Widgets & Panels", color = contentProvider.globalTextColor.value) }
        Spacer(modifier = Modifier.height(3.dp))
        Box(modifier = Modifier.weight(1f).fillMaxWidth(), contentAlignment = Alignment.TopCenter) {
            Row(
                modifier = Modifier.fillMaxWidth().fillMaxHeight()
                    .horizontalScroll(panelColorScroll),
                verticalAlignment = Alignment.Top,
                horizontalArrangement = Arrangement.Center
            ) {
                richColors.forEach {
                    Spacer(modifier = Modifier.width(3.dp))
                    ColorButton(
                        modifier = Modifier
                            .size(Sizes.eightyNine.dp),
                        color = it
                    ) { setColor ->
                        contentProvider.globalColor.value = setColor
                    }
                    Spacer(modifier = Modifier.width(3.dp))
                }
            }
            HorizontalScrollbar(
                modifier = Modifier.align(Alignment.BottomCenter),
                adapter = rememberScrollbarAdapter(panelColorScroll)
            )
        }
        Spacer(modifier = Modifier.height(3.dp))
        Row(
            modifier = Modifier.fillMaxWidth().wrapContentHeight(),
            horizontalArrangement = Arrangement.Center
        ) { Text("Text", color = contentProvider.globalTextColor.value) }
        Spacer(modifier = Modifier.height(3.dp))
        Box(modifier = Modifier.weight(1f), contentAlignment = Alignment.TopCenter) {
            Row(
                modifier = Modifier.fillMaxWidth().fillMaxHeight()
                    .horizontalScroll(textColorScroll),
                verticalAlignment = Alignment.Top,
                horizontalArrangement = Arrangement.Center
            ) {

                richColors.forEach {
                    Spacer(modifier = Modifier.width(3.dp))
                    ColorButton(
                        modifier = Modifier
                            .size(Sizes.eightyNine.dp),
                        color = it
                    ) { setColor ->
                        contentProvider.globalTextColor.value = setColor
                    }
                    Spacer(modifier = Modifier.width(3.dp))
                }

            }
            HorizontalScrollbar(
                modifier = Modifier.align(Alignment.BottomCenter),
                adapter = rememberScrollbarAdapter(textColorScroll)
            )
        }
        Spacer(modifier = Modifier.height(3.dp))
        Row(
            modifier = Modifier.fillMaxWidth().wrapContentHeight(),
            horizontalArrangement = Arrangement.Center
        ) { Text("Indicator", color = contentProvider.globalTextColor.value) }
        Spacer(modifier = Modifier.height(3.dp))
        Box(modifier = Modifier.weight(1f), contentAlignment = Alignment.TopCenter) {
            Row(
                modifier = Modifier.fillMaxWidth().fillMaxHeight()
                    .horizontalScroll(indicatorColorScroll),
                verticalAlignment = Alignment.Top,
                horizontalArrangement = Arrangement.Center
            ) {
                richColors.forEach {
                    Spacer(modifier = Modifier.width(3.dp))
                    ColorButton(
                        modifier = Modifier
                            .size(Sizes.eightyNine.dp),
                        color = it
                    ) { setColor ->
                        contentProvider.activeDockItemIndicator.value = setColor
                    }
                    Spacer(modifier = Modifier.width(3.dp))
                }
            }
            HorizontalScrollbar(
                modifier = Modifier.align(Alignment.BottomCenter),
                adapter = rememberScrollbarAdapter(indicatorColorScroll)
            )
        }
    }
}
@Composable
private fun DPTransparency(){
    Column(
        modifier = Modifier.fillMaxSize().padding(3.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().height(Sizes.thirtyFour.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(5.dp),
        ) {
            Text("Global Transparency",color = contentProvider.globalTextColor.value, fontWeight = FontWeight.Bold)
            Slider(
                value = contentProvider.globalTransparency.value,
                onValueChange = {
                    contentProvider.globalTransparency.value = it
                },
                colors = SliderDefaults.colors(
                    thumbColor = Color(0xff5ab8ab),
                    activeTrackColor = contentProvider.globalTextColor.value
                ),
                modifier = Modifier
                    .padding(0.dp)
                    .background(
                        color = contentProvider.globalColor.value,
                        shape = RoundedCornerShape(13)
                    )
            )
        }
        Spacer(modifier = Modifier.height(13.dp))
        Button(onClick = {
            contentProvider.globalTransparency.value = contentProvider.defaultGlobalTransparency
        },
            colors = ButtonDefaults.buttonColors(
                backgroundColor = contentProvider.globalColor.value
            )
        ){
            Text("Reset",color = contentProvider.globalTextColor.value, fontWeight = FontWeight.Bold)
        }
    }
}
@Composable
private fun DPSize(){
    Column(
        modifier = Modifier.fillMaxSize().padding(3.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().height(Sizes.thirtyFour.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(5.dp),
        ) {
            Text("Panel Height",color = contentProvider.globalTextColor.value, fontWeight = FontWeight.Bold)
            Slider(
                value = contentProvider.panelHeightScaleFactor.value,
                onValueChange = {
                    contentProvider.panelHeightScaleFactor.value = it
                    //contentProvider.panelHeightScaleFactor.value = (1.0f - contentProvider.panelHeightScaleFactor.value)
                },
                colors = SliderDefaults.colors(
                    thumbColor = Color(0xff5ab8ab),
                    activeTrackColor = contentProvider.globalTextColor.value
                ),
                modifier = Modifier
                    .padding(0.dp)
                    .background(
                        color = contentProvider.globalColor.value,
                        shape = RoundedCornerShape(13)
                    )
            )
        }
        Spacer(modifier = Modifier.height(13.dp))
        Row(
            modifier = Modifier.fillMaxWidth().height(Sizes.thirtyFour.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(5.dp),
        ) {
            Text("Dock Height",color = contentProvider.globalTextColor.value, fontWeight = FontWeight.Bold)
            Slider(
                value = contentProvider.dockHeightScaleFactor.value,
                onValueChange = {
                    contentProvider.dockHeightScaleFactor.value = it
                    //contentProvider.dockHeightScaleFactor.value = (1.0f -contentProvider.dockHeightScaleFactor.value)
                },
                colors = SliderDefaults.colors(
                    thumbColor = Color(0xff5ab8ab),
                    activeTrackColor = contentProvider.globalTextColor.value
                ),
                modifier = Modifier
                    .padding(0.dp)
                    .background(
                        color = contentProvider.globalColor.value,
                        shape = RoundedCornerShape(13)
                    )
            )
        }
        Spacer(modifier = Modifier.height(13.dp))
        Button(onClick = {
            contentProvider.dockHeightScaleFactor.value = contentProvider.defaultDockHeightScaleFactor
            contentProvider.panelHeightScaleFactor.value = contentProvider.defaultPanelHeightScaleFactor
        },
            colors = ButtonDefaults.buttonColors(
                backgroundColor = contentProvider.globalColor.value
            )
        ){
            Text("Reset",color = contentProvider.globalTextColor.value, fontWeight = FontWeight.Bold)
        }
    }
}