
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import idleos.composeapp.generated.resources.Res
import idleos.composeapp.generated.resources.*
import jsFeatures.getNetworkInfo
import jsFeatures.getRamInfo
import kotlinx.browser.window
import kotlinx.coroutines.*
import kotlinx.datetime.*
import objects.LayoutValues
import objects.ParentConfig
import kotlin.math.floor
import kotlinx.coroutines.launch
import models.ResponseModel


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
            with(density) { (((thisInt.toFloat()) / 100f) * layoutConfigurator.parentHeight.value).toDp() }
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
            with(density) { ((thisFloat / 100f) * layoutConfigurator.parentHeight.value).toDp() }
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
            with(density) { (((thisDouble / 100f) * layoutConfigurator.parentHeight.value).toFloat()).toDp() }
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
    Color(0xff000000),
    Color(0xffffffff),
    Color(0xFFFFC0C0),
    Color(0xFFFFD580),
    Color(0xFFFFFFE0),
    Color(0xFF90EE90),
    Color(0xFFADD8E6),
    Color(0xFFD8BFD8),
    Color(0xFFEEAAEE),
    Color(0xFFFF0000),
    Color(0xFFFFA500),
    Color(0xFFFFFF00),
    Color(0xFF008000),
    Color(0xFF0000FF),
    Color(0xFF4B0082),
    Color(0xFFEE82EE)
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
    Res.drawable.eleven,
    Res.drawable.twelve,
    Res.drawable.thirteen,
    Res.drawable.fourteen,
    Res.drawable.fifteen,
    Res.drawable.sixteen,
    Res.drawable.seventeen,
    Res.drawable.eighteen,
    Res.drawable.nineteen,
    Res.drawable.twenty,
    Res.drawable.twentyone,
    Res.drawable.twentytwo,
    Res.drawable.twentythree,
    Res.drawable.twentyfour,
    Res.drawable.twentyfive,
    Res.drawable.twentysix,
    Res.drawable.twentyseven,
    Res.drawable.twentyeight,
    Res.drawable.twentynine
)


fun getSimulatedCpuInfo(core:Int):String{
    return """
        processor       : $core
        vendor_id       : GenuineIntel
        cpu family      : 6
        model           : 126
        model name      : Intel(R) Core(TM) i7-1065G7 CPU @ 1.30GHz
        stepping        : 5
        microcode       : 0xc4
        cpu MHz         : 1200.351
        cache size      : 8192 KB
        physical id     : 0
        siblings        : 8
        core id         : $core
        cpu cores       : 4
        apicid          : 0
        initial apicid  : 0
        fpu             : yes
        fpu_exception   : yes
        cpuid level     : 27
        wp              : yes
        flags           : fpu vme de pse tsc msr pae mce cx8 apic sep mtrr pge mca cmov pat pse36 clflush dts acpi mmx fxsr sse sse2 ss ht tm pbe syscall nx pdpe1gb rdtscp lm constant_tsc art arch_perfmon pebs bts rep_good nopl xtopology nonstop_tsc cpuid aperfmperf tsc_known_freq pni pclmulqdq dtes64 monitor ds_cpl vmx est tm2 ssse3 sdbg fma cx16 xtpr pdcm pcid sse4_1 sse4_2 x2apic movbe popcnt tsc_deadline_timer aes xsave avx f16c rdrand lahf_lm abm 3dnowprefetch cpuid_fault epb ssbd ibrs ibpb stibp ibrs_enhanced tpr_shadow flexpriority ept vpid ept_ad fsgsbase tsc_adjust bmi1 avx2 smep bmi2 erms invpcid avx512f avx512dq rdseed adx smap avx512ifma clflushopt intel_pt avx512cd sha_ni avx512bw avx512vl xsaveopt xsavec xgetbv1 xsaves split_lock_detect dtherm ida arat pln pts hwp hwp_notify hwp_act_window hwp_epp hwp_pkg_req vnmi avx512vbmi umip pku ospke avx512_vbmi2 gfni vaes vpclmulqdq avx512_vnni avx512_bitalg avx512_vpopcntdq rdpid fsrm md_clear flush_l1d arch_capabilities
        vmx flags       : vnmi preemption_timer posted_intr invvpid ept_x_only ept_ad ept_1gb flexpriority apicv tsc_offset vtpr mtf vapic ept vpid unrestricted_guest vapic_reg vid ple pml ept_mode_based_exec tsc_scaling
        bugs            : spectre_v1 spectre_v2 spec_store_bypass swapgs itlb_multihit srbds mmio_stale_data retbleed eibrs_pbrsb gds bhi
        bogomips        : 2996.00
        clflush size    : 64
        cache_alignment : 64
        address sizes   : 39 bits physical, 48 bits virtual
        
    """.trimIndent()+"\n"
}

fun getSimulatedMemInfo():String{
    return """
        MemTotal:        8173032 kB
        MemFree:          638940 kB
        MemAvailable:    4567136 kB
        Buffers:          216324 kB
        Cached:          2764860 kB
        SwapCached:        11876 kB
        Active:          3487252 kB
        Inactive:        2871528 kB
        Active(anon):    2178252 kB
        Inactive(anon):   879204 kB
        Active(file):    1309000 kB
        Inactive(file):  1992324 kB
        Unevictable:       32800 kB
        Mlocked:           32800 kB
        SwapTotal:       2097148 kB
        SwapFree:        2084024 kB
        Dirty:              1308 kB
        Writeback:             0 kB
        AnonPages:       2871400 kB
        Mapped:           916300 kB
        Shmem:           1079440 kB
        Slab:             296080 kB
        SReclaimable:     210420 kB
        SUnreclaim:        85660 kB
        KernelStack:       17280 kB
        PageTables:        70440 kB
        NFS_Unstable:          0 kB
        Bounce:               0 kB
        WritebackTmp:          0 kB
        CommitLimit:     6183664 kB
        Committed_AS:    7585328 kB
        VmallocTotal:   34359738367 kB
        VmallocUsed:      128736 kB
        VmallocChunk:   34359508288 kB
        Percpu:            16640 kB
        HardwareCorrupted:     0 kB
        AnonHugePages:    724992 kB
        ShmemHugePages:        0 kB
        ShmemPmdMapped:        0 kB
        FileHugePages:         0 kB
        FilePmdMapped:         0 kB
        CmaTotal:              0 kB
        CmaFree:               0 kB
        HugePages_Total:       0
        HugePages_Free:        0
        HugePages_Rsvd:        0
        HugePages_Surp:        0
        Hugepagesize:       2048 kB
        DirectMap4k:      192528 kB
        DirectMap2M:     8192000 kB
        DirectMap1G:           0 kB
    """.trimIndent()
}

fun addToPath(path:String){
    contentProvider.currentPath.value+=path
}
fun removeFromPath(path: String) {
    val currentPath = contentProvider.currentPath.value
    if (currentPath.endsWith(path)) {
        val newPath = currentPath.removeSuffix(path)
        contentProvider.currentPath.value = newPath
    }
}
fun String.trimStart(): String {
    var startIndex = 0
    while (startIndex < length && this[startIndex].isWhitespace()) {
        startIndex++
    }
    return substring(startIndex)
}


fun contentFetcher(url: String, callback: (ResponseModel) -> Unit) {
    window.fetch(url)
        .then {
            if (it.ok) {
                it.text().then { response ->
                    callback(ResponseModel(response.toString(), true))
                    null
                }
            } else {
                callback(ResponseModel("Not found\n", true))
            }
            null
        }
        .catch { error->
            callback(ResponseModel("$error", true))
            null
        }
}








