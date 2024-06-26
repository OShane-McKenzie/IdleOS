package osComponents

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import components.SimpleAnimator
import components.StyledText
import contentProvider
import fileSystem
import fileSystemLogs
import jsFeatures.getCpuInfo
import jsFeatures.getRamInfo
import kotlinx.coroutines.delay
import objects.AnimationStyle
import objects.ParentConfig
import objects.Sizes
import percentOfParent


/**
 * Renders the InfoCenter composable.
 *
 * @param modifier The modifier for the InfoCenter composable.
 */
@Composable
fun InfoCenter(modifier: Modifier = Modifier){
    val density = LocalDensity.current
    val scrollState = rememberScrollState()
    var animate by remember {
        mutableStateOf(false)
    }
    LaunchedEffect(Unit){
        delay(10)
        animate = true
    }
    var networkFeatures by remember {
        mutableStateOf(contentProvider.netUsageString.value.split("|"))
    }
    LaunchedEffect(contentProvider.netUsageString.value){
        networkFeatures = contentProvider.netUsageString.value.split("|")
    }
    Box(modifier = modifier
        //.width(26.percentOfParent(ParentConfig.WIDTH, density))
        //.height(84.percentOfParent(ParentConfig.HEIGHT, density))
        .background(
            color = contentProvider.globalColor.value.copy(alpha = 0.0f),
            shape = RoundedCornerShape(percent = Sizes.eight)
        )
    ){
        if(animate){
            SimpleAnimator(style = AnimationStyle.RIGHT){
                Column(
                    modifier = Modifier.fillMaxSize().background(
                        color = contentProvider.globalColor.value.copy(alpha = contentProvider.globalTransparency.floatValue),
                        shape = RoundedCornerShape(percent = Sizes.eight)
                    ).padding(13.dp),
                    verticalArrangement = Arrangement.Top,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        "Hardware Info",
                        color = contentProvider.globalTextColor.value,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(Sizes.five.dp))
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(Sizes.fiftyFive.dp)
                            .background(color = contentProvider.globalColor.value, shape = RoundedCornerShape(percent = Sizes.twentyOne)),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ){
                        StyledText(label = "Number of CPUs: ", value = getCpuInfo().toString().replace("NaN", "Unknown"))
                    }
                    Spacer(modifier = Modifier.height(Sizes.twentyOne.dp))
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(Sizes.fiftyFive.dp)
                            .background(
                                color = contentProvider.globalColor.value,
                                shape = RoundedCornerShape(percent = Sizes.twentyOne)),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ){
                        StyledText(label = "RAM: ", value = getRamInfo().toString().replace("NaN", "Unknown")+" Gb")
                    }
                    Spacer(modifier = Modifier.height(Sizes.twentyOne.dp))
                    Text(
                        "Network Info",
                        color = contentProvider.globalTextColor.value,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(Sizes.five.dp))
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(2),
                        modifier = Modifier.fillMaxWidth().wrapContentHeight()
                    ){
                        itemsIndexed(networkFeatures){ index, item ->
                            Row(
                                modifier = Modifier.wrapContentHeight().fillMaxWidth().padding(5.dp)
                            ){
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(Sizes.fiftyFive.dp)
                                        .background(
                                            color = contentProvider.globalColor.value,
                                            shape = RoundedCornerShape(percent = Sizes.twentyOne)
                                        ),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.Center
                                ) {
                                    StyledText(
                                        label = item.split("-")[0],
                                        value = item.split("-")[1],
                                        labelSize = 13,
                                        valueSize = 13
                                    )
                                }
                            }
                        }
                    }
                    Text("System Logs", color = contentProvider.globalTextColor.value,
                        fontWeight = FontWeight.Bold)
                    Column(
                        verticalArrangement = Arrangement.Top,
                        horizontalAlignment = Alignment.Start,
                        modifier = Modifier.fillMaxWidth().fillMaxHeight().verticalScroll(scrollState)
                    ) {
                        Text(fileSystemLogs.value, color = contentProvider.globalTextColor.value)
                    }

                }
            }
        }
    }
}

