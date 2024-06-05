package components

import androidx.compose.foundation.layout.Column
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.compose.material.Text
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign

/**
 * Renders a styled text component with a label and a value.
 *
 * @param modifier The modifier for the component.
 * @param label The label text.
 * @param value The value text.
 * @param labelSize The size of the label text. Default is 16.
 * @param valueSize The size of the value text. Default is 16.
 */
@Composable
fun StyledText(modifier: Modifier=Modifier, label: String, value: String, labelSize:Int = 16, valueSize:Int = 16) {
    val styledText = buildAnnotatedString {
        append(label)
        addStyle(
            style = SpanStyle(
                color = Color(0xff5ab8ab),
                fontWeight = FontWeight.Bold,
                fontSize = labelSize.sp
            ),
            start = 0,
            end = label.length
        )
        append(value)
        addStyle(
            style = SpanStyle(
                color = Color.Blue,
                fontWeight = FontWeight.Normal,
                fontSize = valueSize.sp
            ),
            start = label.length,
            end = label.length + value.length
        )
    }

    Text(text = styledText, textAlign = TextAlign.Center,modifier = modifier)
}