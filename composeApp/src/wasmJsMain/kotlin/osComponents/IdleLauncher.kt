package osComponents

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.input.key.*
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import appProvider
import components.ImageButton
import components.SimpleAnimator
import contentProvider
import fileSystemLogs
import kotlinx.coroutines.delay
import objects.AnimationStyle
import objects.LayoutValues
import objects.Sizes
import turnOnWidget

@Composable
fun IdleLauncher() {
    val interactionSource = remember { MutableInteractionSource() }
    var searchQuery by remember { mutableStateOf("") }
    val filteredApps = appProvider.appList.toList().sortedBy { it.name }.filter {
        it.name.contains(searchQuery, ignoreCase = true)
    }


    if (LayoutValues.showIdleLauncher.value) {
        val focusRequester = remember { FocusRequester() }
        SimpleAnimator(
            style = AnimationStyle.SCALE_IN_CENTER
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(5.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        color = contentProvider.globalColor.value.copy(alpha = 0.4f),
                        shape = RoundedCornerShape(percent = 1)
                    )
                    .onKeyEvent {
                        if (it.type == KeyEventType.KeyDown && it.key == Key.Escape) {
                            try {
                                contentProvider.launchedDockItems.remove("Launcher")
                            } catch (e: Exception) {
                                fileSystemLogs.value += "\n$e"
                            }
                            LayoutValues.showIdleLauncher.value = false
                            true
                        } else {
                            false
                        }
                    }
            ) {
                Spacer(modifier = Modifier.height(3.dp))
                TextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    placeholder = { Text("Search", color = contentProvider.globalTextColor.value) },
                    modifier = Modifier
                        .fillMaxWidth(0.25f)
                        .focusRequester(focusRequester),
                    colors = TextFieldDefaults.textFieldColors(
                        textColor = contentProvider.globalTextColor.value,
                        backgroundColor = contentProvider.globalColor.value.copy(alpha = 0.8f),
                        cursorColor = contentProvider.globalTextColor.value,
                        focusedIndicatorColor = contentProvider.globalTextColor.value
                    )
                )
                LazyVerticalGrid(
                    columns = GridCells.Adaptive(100.dp),
                    contentPadding = PaddingValues(8.dp),
                    modifier = Modifier
                        .fillMaxSize()
                        .clickable(
                            interactionSource = interactionSource,
                            indication = null
                        ) {
                            turnOnWidget("none")
                            try {
                                contentProvider.launchedDockItems.remove("Launcher")
                            } catch (e: Exception) {
                                fileSystemLogs.value += "\n$e"
                            }
                            LayoutValues.showIdleLauncher.value = false
                        },
                    verticalArrangement = Arrangement.Top,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    itemsIndexed(filteredApps) { index, app ->
                        key(app.name) {
                            Column(
                                verticalArrangement = Arrangement.Top,
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                ImageButton(
                                    modifier = Modifier
                                        .height(Sizes.eightyNine.dp)
                                        .width(Sizes.eightyNine.dp)
                                        .clip(CircleShape),
                                    image = app.icon
                                ) {
                                    appProvider.startApp(app.name)
                                    try {
                                        contentProvider.launchedDockItems.remove("Launcher")
                                    } catch (e: Exception) {
                                        fileSystemLogs.value += "\n$e"
                                    }
                                    LayoutValues.showIdleLauncher.value = false
                                }
                                Text(app.name, color = contentProvider.globalTextColor.value, textAlign = TextAlign.Center)
                            }
                        }
                    }
                }
            }
        }
        LaunchedEffect(Unit) {
            delay(200)
            focusRequester.requestFocus()
        }
    }
}