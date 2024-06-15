package apps

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import components.ColorButton
import contentProvider
import objects.Sizes
import richColors
import settingsAppNavigator

@Composable
fun IdleSettings(modifier: Modifier = Modifier){
    Row(modifier = modifier) {
        Column(
            modifier = Modifier
                .fillMaxHeight()
                .fillMaxWidth(0.25f)
                .padding(3.dp)
                .background(
                    color = contentProvider.globalColor.value.copy(alpha = contentProvider.globalTransparency.value),
                    shape = RoundedCornerShape(5)
                )
        ) {

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
            when(settingsAppNavigator.getView()){
                "theme"->{
                    ColorThemeSettings(modifier = Modifier.fillMaxSize().padding(3.dp))
                }
            }
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