package osComponents

import ColorTheme
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Star
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.semantics.Role.Companion.Button
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import appProvider
import components.ColorButton
import components.SimpleAnimator
import components.ThemeButton
import contentProvider
import idleSettingsScreens
import kotlinx.coroutines.delay
import objects.AnimationStyle
import objects.LayoutValues
import objects.Sizes
import richColors
import setDefaultColorTheme
import settingsAppNavigator

/**
 * ControlCenter is a composable function that displays a control center UI.
 *
 * @param modifier The modifier for the control center UI.
 * @return The control center UI.
 */

@Composable
fun ControlCenter(modifier: Modifier = Modifier){
    val density = LocalDensity.current
    var animate by remember {
        mutableStateOf(false)
    }
    LaunchedEffect(Unit){
        delay(10)
        animate = true
    }
    Box(
        modifier = modifier
            //.width((26.percentOfParent(ParentConfig.WIDTH, density = density)))
            //.height(42.percentOfParent(ParentConfig.HEIGHT, density))
            .background(
            color = contentProvider.globalColor.value.copy(alpha = 0.0f),
            shape = RoundedCornerShape(Sizes.eight)
        )

    ){
        if(animate){
            SimpleAnimator(AnimationStyle.DOWN){
                Column(
                    //
                    modifier = Modifier.fillMaxSize().background(
                        color = contentProvider.globalColor.value.copy(alpha = contentProvider.globalTransparency.floatValue),
                        shape = RoundedCornerShape(Sizes.eight)
                    ).padding(start = 8.dp, top = 5.dp, bottom = 5.dp, end = 8.dp),
                    verticalArrangement = Arrangement.Top,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    //
                    Row(
                        verticalAlignment = Alignment.Top,
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth().fillMaxHeight(0.333f).weight(0.5f)
                    ) {
                        Column(
                            modifier = Modifier.fillMaxSize(),
                            verticalArrangement = Arrangement.Top,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ){
                            Text("Default Color Schemes",color = contentProvider.globalTextColor.value)
                            Spacer(modifier = Modifier.height(3.dp))
                            Row(
                                modifier = Modifier.fillMaxWidth().fillMaxHeight(),
                                verticalAlignment = Alignment.Top,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ){
                                ThemeButton(
                                    modifier = Modifier.fillMaxHeight().fillMaxWidth(0.4f).weight(0.4f)
                                ) {
                                    setDefaultColorTheme("light")
                                }
                                Spacer(modifier = Modifier.fillMaxWidth(0.2f).weight(0.2f))
                                ThemeButton(
                                    modifier = Modifier.fillMaxHeight().fillMaxWidth(0.4f).weight(0.4f),
                                    icon = Icons.Rounded.Star,
                                    tint = Color(0xff656363),
                                    label = "Dark"
                                ) {
                                    setDefaultColorTheme("dark")
                                }
                            }
                        }
                    }
                    Row(
                        verticalAlignment = Alignment.Top,
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth().fillMaxHeight(0.333f).weight(0.5f)
                    ) {
                        Column(
                            modifier = Modifier.fillMaxSize(),
                            verticalArrangement = Arrangement.Top,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ){
                            Text("Custom Color Schemes",color = contentProvider.globalTextColor.value)
                            Button(
                                onClick = {
                                    settingsAppNavigator.setViewState(ColorTheme)
                                    contentProvider.settingsAppControllerIndex.value = idleSettingsScreens.indexOf(settingsAppNavigator.getView())
                                    appProvider.startApp("Settings")
                                    LayoutValues.showControlCenter.value = false
                                },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .fillMaxHeight()
                                    .clip(RoundedCornerShape(13)),
                                colors = ButtonDefaults.buttonColors(
                                    backgroundColor = contentProvider.globalColor.value,
                                    contentColor = contentProvider.globalTextColor.value
                                )
                            ){
                                Text(
                                    "Open theme settings",
                                    color = contentProvider.globalTextColor.value,
                                    fontWeight = FontWeight.Bold
                                )

                            }

                        }
                    }
                    Row(
                        modifier = Modifier.fillMaxWidth().fillMaxHeight(0.333f).weight(0.5f),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(5.dp),
                    ) {
                        Text("Brightness",color = contentProvider.globalTextColor.value, fontWeight = FontWeight.Bold)
                        Slider(
                            value = contentProvider.brightnessControl.floatValue,
                            onValueChange = {
                                contentProvider.brightnessControl.floatValue = it
                                contentProvider.brightness.floatValue = (1.0f - contentProvider.brightnessControl.floatValue)
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
                }
            }
        }
    }
}