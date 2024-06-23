package osComponents

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import components.SimpleAnimator
import contentProvider
import idleos.composeapp.generated.resources.Res
import idleos.composeapp.generated.resources.logo_3
import kotlinx.coroutines.delay
import objects.AnimationStyle
import objects.Sizes
import org.jetbrains.compose.resources.painterResource
import turnOnWidget

@Composable
fun LockScreen(){
    val credentials = remember { contentProvider.getSecrets() }
    var loginInput by remember { mutableStateOf("") }
    var passwordInput by remember { mutableStateOf("") }
    val interactionSource = remember { MutableInteractionSource() }
    var info by remember { mutableStateOf("") }
    val focusRequester = remember { FocusRequester() }

    LaunchedEffect(Unit) {
        delay(1000)
        focusRequester.requestFocus()
    }
    SimpleAnimator(
        style = AnimationStyle.TRANSITION
    ) {
        Column(
            modifier = Modifier
                .clickable(
                    interactionSource = interactionSource,
                    indication = null
                ) {
                    turnOnWidget("none")
                }
                .fillMaxSize()
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            Color(0xFFFFDAB9),
                            Color(0xffc999ea),
                            Color(0xffed652e)
                        )
                    )
                ),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ){
            Spacer(modifier = Modifier.fillMaxHeight(0.13f))
            Text(info, color = contentProvider.globalTextColor.value, style = LocalTextStyle.current.copy(
                fontStyle = FontStyle.Italic
            ))
            Spacer(modifier = Modifier.height(5.dp))
            Image(
                painter = painterResource(Res.drawable.logo_3),
                contentDescription = "Image",
                contentScale = ContentScale.FillBounds,
                modifier =
                Modifier
                    .height(Sizes.twoThirtyThree.dp)
                    .width(Sizes.twoThirtyThree.dp)
                    .clip(RoundedCornerShape(8))
            )
            Spacer(modifier = Modifier.height(5.dp))
            OutlinedTextField(
                value = loginInput,
                onValueChange = { loginInput = it },
                placeholder = { Text("Enter user name. Hint: ${credentials.first}", color = contentProvider.globalTextColor.value) },
                modifier = Modifier
                    .fillMaxWidth(0.25f)
                    .focusRequester(focusRequester),
                colors = TextFieldDefaults.textFieldColors(
                    textColor = contentProvider.globalTextColor.value,
                    backgroundColor = contentProvider.globalColor.value.copy(alpha = 0.0f),
                    cursorColor = contentProvider.globalTextColor.value,
                    focusedIndicatorColor = contentProvider.globalTextColor.value
                ),
                singleLine = true
            )
            Spacer(modifier = Modifier.height(5.dp))
            OutlinedTextField(
                value = passwordInput,
                onValueChange = { passwordInput = it },
                placeholder = { Text("Password. Hint: ${credentials.second}", color = contentProvider.globalTextColor.value) },
                modifier = Modifier
                    .fillMaxWidth(0.25f),
                colors = TextFieldDefaults.textFieldColors(
                    textColor = contentProvider.globalTextColor.value,
                    backgroundColor = contentProvider.globalColor.value.copy(alpha = 0.0f),
                    cursorColor = contentProvider.globalTextColor.value,
                    focusedIndicatorColor = contentProvider.globalTextColor.value
                ),
                visualTransformation = PasswordVisualTransformation(),
                singleLine = true
            )
            Spacer(modifier = Modifier.height(5.dp))
            Button(
                onClick = {
                    if(loginInput == credentials.first && passwordInput == credentials.second){
                        info = ""
                        contentProvider.isLoggedOut.value = false
                    }else{
                        info = "Incorrect credentials"
                    }
                }
            ){
                Text("Login", color = contentProvider.globalTextColor.value)
            }

        }
    }

}