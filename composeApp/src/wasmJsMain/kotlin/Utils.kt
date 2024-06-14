
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import idleos.composeapp.generated.resources.Res
import idleos.composeapp.generated.resources.*
import jsFeatures.getNetworkInfo
import jsFeatures.getRamInfo
import kotlinx.coroutines.*
import kotlinx.datetime.*
import objects.LayoutValues
import objects.ParentConfig
import objects.ScreenType
import kotlin.math.floor

fun calculateScreenRatio(height: Int, width: Int): Float {
    require(height > 0) { "Height must be a positive integer" }
    require(width > 0) { "Width must be a positive integer" }

    return height.toFloat() / width.toFloat()
}

fun sizeIfRatio(ratio:Float, initSize:Float):Float{
    return when{
        (ratio <= 1f && ratio > 0.7f) ->{
            initSize
        }
        (ratio <= 0.7f && ratio > 0.6f) ->{
            initSize
        }
        (ratio < 0.6f && ratio > 0.5f) ->{
            (initSize*2)
        }
        (ratio > 1f && ratio < 2f) ->{
            (initSize/1.5f)
        }
        (ratio >= 2f && ratio < 3f) ->{
            (initSize/2f)
        }
        else -> {initSize}
    }
}

fun Int.percentOfParent(config: ParentConfig, density: Density): Dp {
    val thisInt = this
    return when (config) {
        ParentConfig.WIDTH -> {
            with(density) { (((thisInt.toFloat()) / 100f) * layoutConfigurator.parentWidth.value).toDp() }
        }
        ParentConfig.HEIGHT -> {
            with(density) { (((thisInt.toFloat()) / 100f) * layoutConfigurator.parentHeight.value * ScreenType.fromDimensions(layoutConfigurator.parentWidth.value, layoutConfigurator.parentHeight.value).heightScaleFactor).toDp() }
        }
        ParentConfig.SIZE -> {
            with(density) { (((thisInt.toFloat()) / 100f) * layoutConfigurator.parentSize.value).toDp() }
        }
    }
}

fun Float.percentOfParent(config: ParentConfig, density: Density): Dp {
    val thisFloat = this
    return when (config) {
        ParentConfig.WIDTH -> {
            with(density) { ((thisFloat / 100f) * layoutConfigurator.parentWidth.value).toDp() }
        }
        ParentConfig.HEIGHT -> {
            with(density) { ((thisFloat / 100f) * layoutConfigurator.parentHeight.value * ScreenType.fromDimensions(layoutConfigurator.parentWidth.value, layoutConfigurator.parentHeight.value).heightScaleFactor).toDp() }
        }
        ParentConfig.SIZE -> {
            with(density) { ((thisFloat / 100f) * layoutConfigurator.parentSize.value).toDp() }
        }
    }
}

fun Double.percentOfParent(config: ParentConfig, density: Density): Dp {
    val thisDouble =  this
    return when (config) {
        ParentConfig.WIDTH -> {
            with(density) { ((thisDouble / 100f) * layoutConfigurator.parentWidth.value).toFloat().toDp() }
        }
        ParentConfig.HEIGHT -> {
            with(density) { (((thisDouble / 100f) * layoutConfigurator.parentHeight.value).toFloat() * ScreenType.fromDimensions(layoutConfigurator.parentWidth.value, layoutConfigurator.parentHeight.value).heightScaleFactor).toDp() }
        }
        ParentConfig.SIZE -> {
            with(density) { ((thisDouble / 100f) * layoutConfigurator.parentSize.value).toFloat().toDp() }
        }
    }
}

fun Dp.toInt(density: Density): Int {

    return with(density) { this@toInt.toPx().toInt() }
}
fun Dp.toFloat(density: Density): Float {

    return with(density) { this@toFloat.toPx() }
}
fun getDateTimeToString():String {
    val currentMoment: Instant = Clock.System.now()
    val datetimeInSystemZone: LocalDateTime = currentMoment.toLocalDateTime(TimeZone.currentSystemDefault())
    return datetimeInSystemZone.toString().split(".")[0].replace("T"," ")
}
fun Dp.toDouble(density: Density): Double {
    return with(density) { this@toDouble.toPx().toDouble() }
}

fun startDateTimeWatcher(){
    CoroutineScope(Dispatchers.Default).launch {
        while (true){
            withContext(Dispatchers.Main){
                contentProvider.clockString.value = getDateTimeToString()
            }
            delay(1000)
        }
    }
}
fun startRamUsageWatcher(){
    CoroutineScope(Dispatchers.Default).launch {
        while (true){
            withContext(Dispatchers.Main){
                contentProvider.ramUsageString.value = getRamInfo().toString()
            }
            delay(20000)
        }
    }
}

fun startNetUsageWatcher(){
    CoroutineScope(Dispatchers.Default).launch {
        while (true){
            withContext(Dispatchers.Main){
                contentProvider.netUsageString.value = getNetworkInfo()
            }
            delay(20000)
        }
    }
}

fun setDefaultColorTheme(theme:String="light"){
    when(theme){
        "light"->{
            contentProvider.globalColor.value = Color.White
            contentProvider.globalTextColor.value = Color.Black
            contentProvider.activeDockItemIndicator.value = Color.Black
        }
        "dark"->{
            contentProvider.globalColor.value = Color.Black
            contentProvider.globalTextColor.value = Color.White
            contentProvider.activeDockItemIndicator.value = Color.White
        }
    }
}

fun Float.toRoundedInt(): Int {
    try{
        return if(!this.isNaN()){
            if (this > Int.MAX_VALUE) Int.MAX_VALUE else if (this < Int.MIN_VALUE) Int.MIN_VALUE else floor(this + 0.5f).toInt()
        }else{
            0
        }
    }catch (e:Exception){
        println("$e")
        return 0
    }

}
inline fun Unit.then(block: Unit.() -> Unit) {
    val result = this
    result.block()
}

fun turnOnWidget(id:String = ""){
    when(id){
        "infoCenter"->{
            LayoutValues.showInfoCenter.value = !LayoutValues.showInfoCenter.value
            LayoutValues.showControlCenter.value = false
            LayoutValues.showCalendar.value = false
            LayoutValues.showOsInfo.value = false
        }
        "controlCenter"->{
            LayoutValues.showControlCenter.value = !LayoutValues.showControlCenter.value
            LayoutValues.showInfoCenter.value = false
            LayoutValues.showCalendar.value = false
            LayoutValues.showOsInfo.value = false
        }
        "calendar"->{
            LayoutValues.showCalendar.value = !LayoutValues.showCalendar.value
            LayoutValues.showControlCenter.value = false
            LayoutValues.showInfoCenter.value = false
            LayoutValues.showOsInfo.value = false
        }
        "osInfo"->{
            LayoutValues.showOsInfo.value = !LayoutValues.showOsInfo.value
            LayoutValues.showCalendar.value = false
            LayoutValues.showControlCenter.value = false
            LayoutValues.showInfoCenter.value = false
        }
        "none"->{
            LayoutValues.showCalendar.value = false
            LayoutValues.showControlCenter.value = false
            LayoutValues.showInfoCenter.value = false
            LayoutValues.showOsInfo.value = false
            LayoutValues.showOsContextMenu.value = false
        }
    }
}



val richColors = listOf(
    Color(0xFFFF0000),
    Color(0xFFFFA500),
    Color(0xFFFFFF00),
    Color(0xFF008000),
    Color(0xFF0000FF),
    Color(0xFF4B0082),
    Color(0xFFEE82EE),
    Color(0xff000000),
    Color(0xffffffff)
)

val wallPapers = listOf(
    Res.drawable.one,
    Res.drawable.two,
    Res.drawable.three,
    Res.drawable.four,
    Res.drawable.five,
    Res.drawable.six,
    Res.drawable.seven,
    Res.drawable.eight,
    Res.drawable.nine,
    Res.drawable.ten,
    Res.drawable.eleven
)


