package objects

import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import percentOfParent


class LayoutConfigurator {

    val parentHeight = mutableIntStateOf(1)
    val parentWidth = mutableIntStateOf(1)
    val parentSize = mutableIntStateOf((parentHeight.value*parentWidth.value))

    fun isPortraitLayout():Boolean{
        return parentWidth.value < parentHeight.value
    }

    fun isLandscapeLayout():Boolean{
        return parentHeight.value < parentWidth.value
    }

    fun isEvenLayout():Boolean{
        return parentWidth.value == parentHeight.value
    }

}