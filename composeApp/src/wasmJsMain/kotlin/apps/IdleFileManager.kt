package apps

import addToPath
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.automirrored.rounded.ArrowForward
import androidx.compose.material.icons.rounded.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.PointerEventType
import androidx.compose.ui.input.pointer.isSecondaryPressed
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import components.FileManagerButton
import contentProvider
import fileSystem
import fileSystemAppNavigator
import fileSystemLogs
import kotlinx.coroutines.*
import objects.LayoutValues
import objects.Sizes
import osComponents.DayBox
import osComponents.IdleFileContainer
import osComponents.IdleFolder
import removeFromPath

@Composable
fun IdleFileManager(modifier: Modifier=Modifier){
    val scrollState = rememberScrollState()
    var didNavigate by remember { mutableStateOf(false) }
    LaunchedEffect(didNavigate){
        fileSystemAppNavigator.getScreenCount()
        contentProvider.presentWorkingDirectory.value = fileSystem.directoryMap[fileSystemAppNavigator.getView()]?: fileSystem.getRootFileSystem()
        //fileSystemLogs.value += "\n${fileSystemAppNavigator.getView()} ${fileSystemAppNavigator.getScreenCount()}"
    }

    Box(){
        Row(modifier = modifier.pointerInput(Unit) {
            awaitPointerEventScope {
                while (true) {
                    val event = awaitPointerEvent()
                    if (event.type == PointerEventType.Press && event.buttons.isSecondaryPressed) {
                        val x = 0
                    }
                }
            }
        }) {
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
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(modifier = Modifier.padding(3.dp)){
                    FileManagerButton(
                        icon = Icons.Rounded.Lock,
                        label = "System"
                    ) {
                        contentProvider.currentPath.value = ""
                        contentProvider.lastAddedToPath.value = "/"
                        fileSystemAppNavigator.setViewState("/")
                        didNavigate = !didNavigate
                    }
                }
                //Spacer(modifier = Modifier.height(5.dp))
                Row(modifier = Modifier.padding(3.dp)){
                    FileManagerButton {
                        contentProvider.currentPath.value = "/home"
                        contentProvider.lastAddedToPath.value = "/home"
                        fileSystemAppNavigator.setViewState("home")
                        didNavigate = !didNavigate
                    }
                }
                Row(modifier = Modifier.padding(3.dp)){
                    FileManagerButton(
                        icon = Icons.Rounded.Face,
                        label = "Pictures"
                    ) {
                        contentProvider.currentPath.value = "/.Wallpapers"
                        contentProvider.lastAddedToPath.value = "/.Wallpapers"
                        fileSystemAppNavigator.setViewState(".Wallpapers")
                        didNavigate = !didNavigate
                    }
                }
            }
            Column(
                modifier = Modifier
                    .fillMaxHeight()
                    .fillMaxWidth()
                    .padding(3.dp)
                    .background(
                        color = contentProvider.globalColor.value,
                        shape = RoundedCornerShape(3)
                    )
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth().height(40.dp).padding(3.dp)
                ) {
                    IconButton(
                        onClick = {

                            removeFromPath("/${fileSystemAppNavigator.getView()}")
                            if(fileSystemAppNavigator.getLastIndex() >= 0){
                                fileSystemAppNavigator.goBack(fileSystemAppNavigator.getLastIndex())
                            }
                            didNavigate = !didNavigate

                        },
                        modifier = Modifier
                            .padding(0.dp)
                            .height(Sizes.thirtyFour.dp)
                            .width(Sizes.thirtyFour.dp)

                    ){
                        Icon(
                            imageVector = Icons.AutoMirrored.Rounded.ArrowBack,
                            contentDescription = "back",
                            modifier = Modifier
                                .padding(0.dp)
                                .height(Sizes.thirtyFour.dp)
                                .width(Sizes.thirtyFour.dp),
                            tint = contentProvider.globalTextColor.value
                        )
                    }
                    Text(contentProvider.currentPath.value,
                        color = contentProvider.globalTextColor.value)
                    IconButton(
                        onClick = {
                            if(!contentProvider.lastAddedToPath.value.endsWith(fileSystemAppNavigator.getView())){
                                addToPath(contentProvider.lastAddedToPath.value)
                            }
                            fileSystemAppNavigator.setViewState(contentProvider.lastAddedToPath.value.replace("/",""))
                            didNavigate = !didNavigate
                        },
                        modifier = Modifier
                            .padding(0.dp)
                            .height(Sizes.thirtyFour.dp)
                            .width(Sizes.thirtyFour.dp)

                    ){
                        Icon(
                            imageVector = Icons.AutoMirrored.Rounded.ArrowForward,
                            contentDescription = "forward",
                            modifier = Modifier
                                .padding(0.dp)
                                .height(Sizes.thirtyFour.dp)
                                .width(Sizes.thirtyFour.dp),
                            tint = contentProvider.globalTextColor.value
                        )
                    }
                }
                LazyVerticalGrid(
                    columns = GridCells.Adaptive(Sizes.eightyNine.dp),
                    contentPadding = PaddingValues(Sizes.eight.dp),
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            color = contentProvider.globalColor.value.copy(alpha = 0.0f),
                            shape = RoundedCornerShape(percent = Sizes.three)
                        ),
                    verticalArrangement = Arrangement.Top,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    itemsIndexed(contentProvider.presentWorkingDirectory.value.dirs.toList().sortedBy { it.name.value }) { index, dir ->
                        key(dir.id) {
                            IdleFolder(modifier = Modifier, idleDirectory = dir) { path ->
                                didNavigate = !didNavigate
                            }
                        }
                    }
                    itemsIndexed(contentProvider.presentWorkingDirectory.value.files.toList().sortedBy { it.name.value }) { index, file ->
                        key(file.id) {
                            IdleFileContainer(modifier = Modifier, idleFile = file) {

                            }
                        }
                    }
                }
            }
        }
        VerticalScrollbar(modifier = Modifier.align(Alignment.CenterStart), adapter = rememberScrollbarAdapter(scrollState))
    }
}


