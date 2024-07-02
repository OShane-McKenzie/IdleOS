package apps

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.input.key.*
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import components.IdleTextField
import contentProvider
import idleTerminalCommands
import kotlinx.coroutines.delay


@Composable
fun IdleTerminal(){
    val scrollState = rememberScrollState()
    val terminal = remember { idleTerminalCommands }
    val focusRequester = remember { FocusRequester() }

    LaunchedEffect(Unit) {
        delay(500)
        focusRequester.requestFocus()
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(3.dp)
            .background(
                shape = RoundedCornerShape(3f),
                color = contentProvider.globalColor.value.copy(alpha = contentProvider.globalTransparency.value))
            .verticalScroll(scrollState),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.Start
    ) {
        Row(modifier = Modifier.padding(start = 16.dp, bottom = 0.dp, top = 0.dp)){
            Text(
                "Supported commands: ${terminal.commandList.joinToString(", ") { it.trim() }}",
                color = contentProvider.globalTextColor.value,
                fontWeight = FontWeight.Bold,
                style = LocalTextStyle.current.copy(
                    fontStyle = FontStyle.Italic
                )
            )
        }
        if(terminal.idleOutputStream.value.isNotEmpty()){
            Row(modifier = Modifier.padding(start = 16.dp, bottom = 0.dp, top = 0.dp).wrapContentHeight()){ Text(terminal.idleOutputStream.value, color = contentProvider.globalTextColor.value, modifier = Modifier.padding(0.dp)) }
        }
        IdleTextField(
            value = terminal.idleInputStream.value,
            onValueChange = {
                if(it.contains("\n")){
                    terminal.inputParser(terminal.idleInputStream.value)
                }else{
                    terminal.idleInputStream.value = it
                }
                },
            placeholder = {
                Text("[${contentProvider.getSecrets().first}@IdleOS]$", color = contentProvider.globalTextColor.value,style = LocalTextStyle.current.copy(
                fontStyle = FontStyle.Italic
            )) },
            modifier = Modifier
                .fillMaxWidth(0.5f)
                .padding(all = 0.dp)
                .onKeyEvent {
                    if (it.type == KeyEventType.KeyDown && it.key == Key.Enter) {
                        terminal.inputParser(terminal.idleInputStream.value)
                        true
                    } else {
                        false
                    }
                }
                .focusRequester(focusRequester),
            colors = TextFieldDefaults.textFieldColors(
                textColor = contentProvider.globalTextColor.value,
                backgroundColor = contentProvider.globalColor.value.copy(alpha = 0.0f),
                cursorColor = contentProvider.globalTextColor.value,
                focusedIndicatorColor = contentProvider.globalTextColor.value.copy(alpha = 0.0f),
                unfocusedIndicatorColor = contentProvider.globalTextColor.value.copy(alpha = 0.0f)
            ),
            singleLine = false,
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Done
            ),
            keyboardActions = KeyboardActions(onDone = {
                terminal.inputParser(terminal.idleInputStream.value)
            })
        )
    }
}