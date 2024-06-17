package jsFeatures

import org.w3c.dom.events.Event

external interface NetworkInformation {
    val downLink: Double
    val effectiveType: String
    val rtt: Double
    val saveData: Boolean

    fun addEventListener(type: String, listener: (Event) -> Unit)
    fun removeEventListener(type: String, listener: (Event) -> Unit)
}

external interface Navigator {
    val deviceMemory: Double
    val hardwareConcurrency: Int
    val connection: NetworkInformation
}

external val navigator: Navigator

@OptIn(ExperimentalJsExport::class)
@JsExport
fun getRamInfo(): Double {
    val ram = navigator.deviceMemory
    return ram
}

@OptIn(ExperimentalJsExport::class)
@JsExport
fun getCpuInfo(): Int {
    val cpu = navigator.hardwareConcurrency
    return cpu
}

@OptIn(ExperimentalJsExport::class)
@JsExport
fun getNetworkInfo(): String {
    val downLink = navigator.connection.downLink
    val effectiveType = navigator.connection.effectiveType
    val rtt = navigator.connection.rtt
    val saveData = navigator.connection.saveData

    return "Download Speed:- $downLink Mbps|Network Type:- $effectiveType|Latency:- $rtt ms|Data Saving Mode:- $saveData"
        .replace("NaN","Unknown")
        .replace("false","No")
        .replace("true","Yes")
}