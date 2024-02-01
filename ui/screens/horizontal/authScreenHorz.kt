package com.example.lexpro_mobile.ui.screens.horizontal

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.lexpro_mobile.AuthEvents
import com.example.lexpro_mobile.R
import com.example.lexpro_mobile.ViewModel
import com.example.lexpro_mobile.ui.ResponseError
import com.example.lexpro_mobile.ui.theme.Lexpro_mobileTheme
import com.valentinilk.shimmer.shimmer

var isAuthorized = false

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun AuthHorizontal(
    isLoadingState: Boolean = false,
    error: ResponseError? = null,
    authViewModel: ViewModel = hiltViewModel(),
    onNavToNext: () -> Unit = {}
) {
    Lexpro_mobileTheme() {
        var authState = authViewModel.authState
        val login = remember { mutableStateOf("") }
        val password = remember { mutableStateOf("") }
        val passwordVisibility = remember { mutableStateOf(false) }
        var errorLoginText = ""

        var buttonModifier: Modifier = Modifier
        if (authState.isLoading) {
            buttonModifier = buttonModifier.shimmer()
        }

        val dropDownMenuExpanded = remember { mutableStateOf(false) }
        val dropDownMenuItems =
            arrayOf("", "url2.example.com", "url3.example.com", "url4.example.com")
        val url = remember { mutableStateOf(dropDownMenuItems[0]) }

        if (!isAuthorized) {
            if (!authState.isLoading && authState.token == 302) {
                authState = authState.copy(login = login.value, isAuthorised = true)
                isAuthorized = true
                onNavToNext()
            } else if (authState.token == 200 || authState.token == 404) {
                errorLoginText =
                    "Ошибка авторизации. Проверьте логин и пароль или попробуйте позже."
            }
        }

        //Приветственный текст, поле выбора сервера, поле для ввода логина/пароля, пикча, кнопка
        Column(
            Modifier.fillMaxHeight(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    modifier = Modifier
                        .padding(top = 0.dp)
                        .scale(0.9f),
                    painter = painterResource(id = R.drawable.ic_logo_auth),
                    contentDescription = null,
                    contentScale = ContentScale.FillBounds,
                    alignment = Alignment.Center
                )
            }
            //region Подключение к $url
            Text(
                text = "АИС Законопроект",
                fontSize = 30.sp,
                fontWeight = FontWeight(380)
            )

            Text(
                text = "Добро пожаловать",
                fontSize = 20.sp,
                fontWeight = FontWeight.Light,
                modifier = Modifier.padding(bottom = 50.dp)
            )

            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "Подключение к ${url.value}",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Light,
                    textAlign = TextAlign.Center
                )
                Row() {
                    DropdownMenu(
                        expanded = dropDownMenuExpanded.value,
                        onDismissRequest = {
                            dropDownMenuExpanded.value = !dropDownMenuExpanded.value
                        }) {
                        dropDownMenuItems.forEach { itemValue ->
                            DropdownMenuItem(onClick = {
                                url.value = itemValue
                                dropDownMenuExpanded.value = false
                            }) {
                                Text(text = itemValue)
                            }
                        }
                    }
                    IconButton(
                        onClick = {
                            dropDownMenuExpanded.value = !dropDownMenuExpanded.value
                        },
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.ic_arrow_down),
                            contentDescription = "",
                            tint = Color.Black,
                            modifier = Modifier
                                .size(17.dp)
                                .padding(top = 5.dp)
                        )
                    }
                }
            }
            //endregion

            val (loginFocusRequest, passFocusRequest, authAttemptFocusRequest) = remember { FocusRequester.createRefs() }
            val focusManager = LocalFocusManager.current

            //region Поле ввода логина/пароля
            Row(Modifier.padding(top = 0.dp)) {
                Spacer(Modifier.weight(1.3f))
                TextField(
                    //TODO Поменять цвета при фокусе
                    isError = error != null,
                    enabled = !isLoadingState,
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .focusRequester(loginFocusRequest),
                    singleLine = true,
                    textStyle = TextStyle(fontSize = 26.sp, fontWeight = FontWeight.Light),
                    label = {
                        Text(text = "Логин", fontSize = 15.sp)
                    }, colors = TextFieldDefaults.textFieldColors(
                        backgroundColor = Color.Transparent,
                        focusedIndicatorColor = Color.Blue
                    ),
                    keyboardActions = KeyboardActions(onDone = {
                        passFocusRequest.requestFocus()
                    }),
                    value = login.value,
                    onValueChange = { login.value = it }
                )
                Spacer(Modifier.weight(1.3f))
            }
            Row(Modifier.padding(top = 0.dp)) {
                Spacer(Modifier.weight(1.3f))
                TextField(
                    //TODO Поменять цвета при фокусе
                    isError = error != null,
                    enabled = !isLoadingState,
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .focusRequester(passFocusRequest),
                    singleLine = true,
                    textStyle = TextStyle(fontSize = 26.sp, fontWeight = FontWeight.Light),
                    visualTransformation = if (passwordVisibility.value) VisualTransformation.None else PasswordVisualTransformation(),
                    label = {
                        Text(text = "Пароль", fontSize = 15.sp)
                    }, colors = TextFieldDefaults.textFieldColors(
                        backgroundColor = Color.Transparent,
                        focusedIndicatorColor = Color.Blue
                    ),
                    trailingIcon = {
                        IconButton(
                            onClick = {
                                passwordVisibility.value = !passwordVisibility.value
                            },
                        ) {
                            if (passwordVisibility.value) {
                                Icon(
                                    painter = painterResource(R.drawable.ic_eye),
                                    contentDescription = "",
                                    tint = Color.Black
                                )
                            } else {
                                Icon(
                                    painter = painterResource(R.drawable.ic_eye_closed),
                                    contentDescription = "",
                                    tint = Color.Black
                                )
                            }
                        }
                    },

                    keyboardActions = KeyboardActions(onDone = {
                        focusManager.clearFocus()
                        if (!isAuthorized) {
                            authViewModel.onEvent(
                                AuthEvents.Authorise(
                                    login.value,
                                    password.value
                                )
                            )
                        } else {
                            authState = authState.copy(
                                login = "",
                                isAuthorised = false,
                                isLoading = false,
                                token = 0
                            )
                            isAuthorized = false
                            authViewModel.onEvent(
                                AuthEvents.Authorise(
                                    login.value,
                                    password.value
                                )
                            )
                        }
                    }),
                    value = password.value,
                    onValueChange = { letter -> password.value = letter }
                )
                Spacer(Modifier.weight(1.3f))
            }
            //endregion
            Text(
                text = errorLoginText,
                fontSize = 20.sp,
                color = Color.Red,
                fontWeight = FontWeight(400)
            )
            Button(
                enabled = !authState.isLoading,
                shape = RoundedCornerShape(6.dp),
                modifier = buttonModifier
                    .wrapContentWidth()
                    .wrapContentHeight()
                    .padding(top = 18.dp, bottom = 48.dp),
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = colorResource(id = R.color.button_primary),
                    contentColor = Color.LightGray,
                ),
                onClick = {
                    if (!isAuthorized) {
                        authViewModel.onEvent(
                            AuthEvents.Authorise(
                                login.value,
                                password.value
                            )
                        )
                    } else {
                        authState = authState.copy(
                            login = "",
                            isAuthorised = false,
                            isLoading = false,
                            token = 0
                        )
                        isAuthorized = false
                        authViewModel.onEvent(
                            AuthEvents.Authorise(
                                login.value,
                                password.value
                            )
                        )
                    }
                }
            ) {
                Text(text = "Авторизоваться", fontSize = 22.sp, color = Color.White)
            }
        }
    }
}