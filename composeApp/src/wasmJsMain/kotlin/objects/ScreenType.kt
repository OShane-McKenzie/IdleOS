package objects


const val REFERENCE_HEIGHT = 2000 // Define a constant reference height
const val REFERENCE_WIDTH = 3000 // Define a constant reference height

enum class ScreenType(val width: Int, val height: Int, val heightScaleFactor: Float, val widthScaleFactor: Float) {
    NHD(640, 360, calculateHeightScaleFactor(360), calculateWidthScaleFactor(640)),
    VGA(640, 480, calculateHeightScaleFactor(480), calculateWidthScaleFactor(640)),
    SVGA(800, 600, calculateHeightScaleFactor(600), calculateWidthScaleFactor(800)),
    XGA(1024, 768, calculateHeightScaleFactor(768), calculateWidthScaleFactor(1024)),
    WXGA_720(1280, 720, calculateHeightScaleFactor(720), calculateWidthScaleFactor(1280)),
    WXGA_800(1280, 800, calculateHeightScaleFactor(800), calculateWidthScaleFactor(1280)),
    SXGA(1280, 1024, calculateHeightScaleFactor(1024), calculateWidthScaleFactor(1280)),
    HD_1360(1360, 768, calculateHeightScaleFactor(768), calculateWidthScaleFactor(1360)),
    HD_1366(1366, 768, calculateHeightScaleFactor(768), calculateWidthScaleFactor(1366)),
    WXGA_PLUS(1440, 900, calculateHeightScaleFactor(900), calculateWidthScaleFactor(1440)),
    UNKNOWN_1536_864(1536, 864, calculateHeightScaleFactor(864), calculateWidthScaleFactor(1536)),
    HD_PLUS(1600, 900, calculateHeightScaleFactor(900), calculateWidthScaleFactor(1600)),
    UXGA(1600, 1200, calculateHeightScaleFactor(1200), calculateWidthScaleFactor(1600)),
    WSXGA_PLUS(1680, 1050, calculateHeightScaleFactor(1050), calculateWidthScaleFactor(1680)),
    FHD(1920, 1080, calculateHeightScaleFactor(1080), calculateWidthScaleFactor(1920)),
    WUXGA(1920, 1200, calculateHeightScaleFactor(1200), calculateWidthScaleFactor(1920)),
    QWXGA(2048, 1152, calculateHeightScaleFactor(1152), calculateWidthScaleFactor(2048)),
    QXGA(2048, 1536, calculateHeightScaleFactor(1536), calculateWidthScaleFactor(2048)),
    UWFHD(2560, 1080, calculateHeightScaleFactor(1080), calculateWidthScaleFactor(2560)),
    QHD(2560, 1440, calculateHeightScaleFactor(1440), calculateWidthScaleFactor(2560)),
    WQXGA(2560, 1600, calculateHeightScaleFactor(1600), calculateWidthScaleFactor(2560)),
    UWQHD(3440, 1440, calculateHeightScaleFactor(1440), calculateWidthScaleFactor(3440)),
    UHD_4K(3840, 2160, calculateHeightScaleFactor(2160), calculateWidthScaleFactor(3840)),
    NEW_RESOLUTION(3000, 2000, 1f, 1f);

    companion object {
        fun fromDimensions(width: Int, height: Int): ScreenType {
            return entries.find { it.width == width && height <= it.height } ?: HD_1366
        }

    }
}

fun calculateHeightScaleFactor(height: Int): Float {
    return REFERENCE_HEIGHT.toFloat() / height
}

fun calculateWidthScaleFactor(width: Int): Float {
    return REFERENCE_WIDTH.toFloat() / width
}