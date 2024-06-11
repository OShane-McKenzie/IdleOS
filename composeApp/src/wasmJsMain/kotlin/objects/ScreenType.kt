package objects


const val REFERENCE_HEIGHT = 2000 // Define a constant reference height

enum class ScreenType(val width: Int, val height: Int, val scaleFactor: Float) {
    NHD(640, 360, calculateScaleFactor(360)),
    VGA(640, 480, calculateScaleFactor(480)),
    SVGA(800, 600, calculateScaleFactor(600)),
    XGA(1024, 768, calculateScaleFactor(768)),
    WXGA_720(1280, 720, calculateScaleFactor(720)),
    WXGA_800(1280, 800, calculateScaleFactor(800)),
    SXGA(1280, 1024, calculateScaleFactor(1024)),
    HD_1360(1360, 768, calculateScaleFactor(768)),
    HD_1366(1366, 768, calculateScaleFactor(768)),
    WXGA_PLUS(1440, 900, calculateScaleFactor(900)),
    UNKNOWN_1536_864(1536, 864, calculateScaleFactor(864)),
    HD_PLUS(1600, 900, calculateScaleFactor(900)),
    UXGA(1600, 1200, calculateScaleFactor(1200)),
    WSXGA_PLUS(1680, 1050, calculateScaleFactor(1050)),
    FHD(1920, 1080, calculateScaleFactor(1080)),
    WUXGA(1920, 1200, calculateScaleFactor(1200)),
    QWXGA(2048, 1152, calculateScaleFactor(1152)),
    QXGA(2048, 1536, calculateScaleFactor(1536)),
    UWFHD(2560, 1080, calculateScaleFactor(1080)),
    QHD(2560, 1440, calculateScaleFactor(1440)),
    WQXGA(2560, 1600, calculateScaleFactor(1600)),
    UWQHD(3440, 1440, calculateScaleFactor(1440)),
    UHD_4K(3840, 2160, calculateScaleFactor(2160)),
    NEW_RESOLUTION(3000, 2000, 1f);

    companion object {
        fun fromDimensions(width: Int, height: Int): ScreenType {
            return entries.find { it.width == width && height <= it.height } ?: HD_1366
        }
    }
}

fun calculateScaleFactor(height: Int): Float {
    return REFERENCE_HEIGHT.toFloat() / height
}
