
import androidx.compose.ui.graphics.Color
import idleos.composeapp.generated.resources.Res
import idleos.composeapp.generated.resources.*
import jsFeatures.getNetworkInfo
import jsFeatures.getRamInfo
import kotlinx.coroutines.*
import kotlinx.datetime.*
import objects.ParentConfig


fun Int.percentOfParent(config: ParentConfig):Float{
    return when(config){
        ParentConfig.WIDTH->{
            ((this.toFloat())/100f)*layoutConfigurator.parentWidth.value
        }

        ParentConfig.HEIGHT ->{
            ((this.toFloat())/100f)*layoutConfigurator.parentHeight.value
        }

        ParentConfig.SIZE ->{
            ((this.toFloat())/100f)*layoutConfigurator.parentSize.value
        }
    }
}
fun Float.percentOfParent(config: ParentConfig):Float{
    return when(config){
        ParentConfig.WIDTH->{
            (this/100f)*layoutConfigurator.parentWidth.value
        }

        ParentConfig.HEIGHT ->{
            (this/100f)*layoutConfigurator.parentHeight.value
        }

        ParentConfig.SIZE ->{
            (this/100f)*layoutConfigurator.parentSize.value
        }
    }
}

fun Double.percentOfParent(config: ParentConfig):Float{
    return when(config){
        ParentConfig.WIDTH->{
            ((this/100f)*layoutConfigurator.parentWidth.value).toFloat()
        }

        ParentConfig.HEIGHT ->{
            ((this/100f)*layoutConfigurator.parentHeight.value).toFloat()
        }

        ParentConfig.SIZE ->{
            ((this/100f)*layoutConfigurator.parentSize.value).toFloat()
        }
    }
}

fun getDateTimeToString():String {
    val currentMoment: Instant = Clock.System.now()
    val datetimeInSystemZone: LocalDateTime = currentMoment.toLocalDateTime(TimeZone.currentSystemDefault())
    return datetimeInSystemZone.toString().split(".")[0].replace("T"," ")
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

inline fun Unit.then(block: Unit.() -> Unit) {
    val result = this
    result.block()
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


