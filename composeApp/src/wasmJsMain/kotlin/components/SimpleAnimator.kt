package components
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.TransformOrigin
import kotlinx.coroutines.delay
import objects.AnimationStyle

/**
 * Composable function that provides simple animations based on the specified style.
 *
 * @param style The animation style to apply (default is AnimationStyle.RIGHT).
 * @param modifier The modifier for the animated content (default is Modifier).
 * @param task The task to be executed after the animation (default is an empty lambda).
 * @param animationCounter The counter for the animation (default is 0).
 * @param content The content to animate.
 */
@Composable
fun SimpleAnimator(
    style: AnimationStyle = AnimationStyle.RIGHT,
    modifier: Modifier = Modifier,
    task: (Int) -> Unit = {},
    animationCounter: Int = 0,
    content: @Composable () -> Unit
) {

    @Composable
    fun Right() {
        var visible by remember { mutableStateOf(false) }
        LaunchedEffect(Unit) {
            visible = true
        }
        AnimatedVisibility(
            modifier = modifier,
            visible = visible,
            enter = slideInHorizontally(
                initialOffsetX = { fullWidth -> fullWidth },
                animationSpec = tween(durationMillis = 200)
            ) + fadeIn(animationSpec = tween(durationMillis = 200)),
            exit = slideOutHorizontally(animationSpec = spring(stiffness = Spring.StiffnessHigh)) {
                200
            } + fadeOut()
        ) {
            content()
        }
    }

    @Composable
    fun Left() {
        var visible by remember { mutableStateOf(false) }
        LaunchedEffect(Unit) {
            visible = true
        }
        AnimatedVisibility(
            modifier = modifier,
            visible = visible,
            enter = slideInHorizontally(
                initialOffsetX = { fullWidth -> -fullWidth },
                animationSpec = tween(durationMillis = 200)
            ) + fadeIn(animationSpec = tween(durationMillis = 200)),
            exit = slideOutHorizontally(animationSpec = spring(stiffness = Spring.StiffnessHigh)) {
                -200
            } + fadeOut()
        ) {
            content()
        }
    }

    @Composable
    fun ScaleInTop() {
        var visible by remember { mutableStateOf(false) }
        LaunchedEffect(Unit) {
            visible = true
        }

        AnimatedVisibility(
            modifier = modifier,
            visible = visible,
            enter = scaleIn(),
            exit = scaleOut()
        ) {
            content()
        }
    }

    @Composable
    fun ScaleInLeft() {
        var visible by remember { mutableStateOf(false) }
        LaunchedEffect(Unit) {
            visible = true
        }
        AnimatedVisibility(
            modifier = modifier,
            visible = visible,
            enter = scaleIn(transformOrigin = TransformOrigin(0f, 0f)) + fadeIn(),
            exit = scaleOut(transformOrigin = TransformOrigin(0f, 0f)) + fadeOut()
        ) {
            content()
        }
    }

    @Composable
    fun Up() {
        var visible by remember { mutableStateOf(false) }
        LaunchedEffect(Unit) {
            visible = true
        }
        AnimatedVisibility(
            modifier = modifier,
            visible = visible,
            enter = slideInVertically(initialOffsetY = { 40 }) + fadeIn(initialAlpha = 0.3f),
            exit = slideOutVertically() + fadeOut()
        ) {
            content()
        }
    }

    @Composable
    fun Down() {
        var visible by remember { mutableStateOf(false) }
        LaunchedEffect(Unit) {
            visible = true
        }
        AnimatedVisibility(
            modifier = modifier,
            visible = visible,
            enter = slideInVertically(initialOffsetY = { -40 }) + fadeIn(initialAlpha = 0.3f),
            exit = slideOutVertically() + fadeOut()
        ) {
            content()
        }
    }

    @Composable
    fun Transition(color: Color = Color.White, speed: Int = 15) {
        var setAlpha by remember {
            mutableFloatStateOf(1.0f)
        }
        var isAlphaSet by remember {
            mutableStateOf(false)
        }
        val colorList = remember { mutableStateListOf<Color>() }
        if (!isAlphaSet) {
            var counter = setAlpha
            while (counter > 0) {
                colorList.add(color.copy(alpha = counter))
                counter -= 0.05f
            }
            colorList.add(color.copy(alpha = 0.0f))
            isAlphaSet = true
        }
        var transitionColor by remember {
            mutableStateOf(color.copy(alpha = setAlpha))
        }

        LaunchedEffect(isAlphaSet) {
            if (isAlphaSet) {
                for (i in colorList) {
                    transitionColor = i
                    delay(speed.toLong())
                }
                setAlpha = 0.0f
            }
        }

        if (setAlpha <= 0f) {
            content()
        } else {
            Column(
                Modifier
                    .fillMaxSize()
                    .background(transitionColor)
            ) {
            }
        }
    }

    @Composable
    fun ScaleInCenter() {
        var visible by remember { mutableStateOf(false) }
        LaunchedEffect(Unit) {
            visible = true
        }
        AnimatedVisibility(
            modifier = modifier,
            visible = visible,
            enter = scaleIn(transformOrigin = TransformOrigin.Center) + fadeIn(),
            exit = scaleOut(transformOrigin = TransformOrigin.Center) + fadeOut()
        ) {
            content()
        }
    }

    when (style) {
        AnimationStyle.RIGHT -> {
            Right()
            task(animationCounter)
        }

        AnimationStyle.LEFT -> {
            Left()
            task(animationCounter)
        }

        AnimationStyle.SCALE_IN_TOP -> {
            ScaleInTop()
            task(animationCounter)
        }

        AnimationStyle.SCALE_IN_LEFT -> {
            ScaleInLeft()
            task(animationCounter)
        }

        AnimationStyle.UP -> {
            Up()
            task(animationCounter)
        }

        AnimationStyle.DOWN -> {
            Down()
            task(animationCounter)
        }

        AnimationStyle.TRANSITION -> {
            Transition()
            task(animationCounter)
        }

        AnimationStyle.SCALE_IN_CENTER -> {
            ScaleInCenter()
            task(animationCounter)
        }
    }
}