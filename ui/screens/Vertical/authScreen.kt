package com.example.lexpro_mobile.ui.screens.vertical

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.valentinilk.shimmer.shimmer
import com.example.lexpro_mobile.R
import com.example.lexpro_mobile.ui.ResponseError
import com.example.lexpro_mobile.AuthEvents
import com.example.lexpro_mobile.ViewModel
import com.example.lexpro_mobile.ui.theme.Lexpro_mobileTheme

var isAuthorized = false

@Composable
fun authVertical(
    title: String = "",
    subtitle: String = "",
    textFieldLogin: String = "",
    textFieldPass: String = "",
    textLabelLogin: String = "",
    textLabelPass: String = "",
    buttonText: String = "",
    isLoadingState: Boolean = false,
    error: ResponseError? = null,
    hideBackButton: Boolean = false,
    onBackPressed: () -> Unit = {},
    authViewModel: ViewModel = hiltViewModel(),
    onNavToNext: () -> Unit = {}
) {
    var uiState = authViewModel.authState
    val login = remember { mutableStateOf("") }
    val password = remember { mutableStateOf("") }
    var passwordVisibility = remember { mutableStateOf(false) }
    var errorLoginText = ""

    Lexpro_mobileTheme() {
        if (!isAuthorized) {
            if (!uiState.isLoading && uiState.token == 302) {
                uiState = uiState.copy(login = login.value, isAuthorised = true)
                isAuthorized = true
                onNavToNext()
            } else if (uiState.token == 200 || uiState.token == 404) {
                errorLoginText =
                    "Ошибка авторизации. Проверьте логин и пароль или попробуйте позже."
            }
        }
        Box(contentAlignment = Alignment.BottomEnd) {
            Box {
                Column(
                    modifier = Modifier.fillMaxSize()
                ) {
                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .padding(horizontal = 24.dp)
                    ) {
                        Text(
                            modifier = Modifier.paddingFromBaseline(100.dp),
                            text = "Информационная система Законопроект",
                            overflow = TextOverflow.Ellipsis,
                            style = MaterialTheme.typography.h4
                        )
                        CompositionLocalProvider(LocalContentAlpha provides ContentAlpha.medium) {
                            Text(
                                modifier = Modifier.paddingFromBaseline(32.dp),
                                text = "Выполните авторизацию",
                                overflow = TextOverflow.Ellipsis,
                                style = MaterialTheme.typography.subtitle1
                            )
                        }
                        Spacer(modifier = Modifier.weight(1f))
                        Spacer(modifier = Modifier.height(20.dp))
                    }

                    Column(modifier = Modifier.padding(horizontal = 24.dp)) {

                        var isFocusRequested by rememberSaveable {
                            mutableStateOf(false)
                        }

                        val focusManager = LocalFocusManager.current
                        val focusRequester = remember { FocusRequester() }

                        LaunchedEffect(Unit) {
                            if (!isFocusRequested) {
                                isFocusRequested = true
                                focusRequester.requestFocus()
                            }
                        }





                        OutlinedTextField(
                            isError = error != null,
                            enabled = !isLoadingState,
                            modifier = Modifier
                                .fillMaxWidth(),
                            singleLine = true,
                            label = {
                                Text(text = "Логин")
                            }, colors = TextFieldDefaults.textFieldColors(
                                backgroundColor = Color.Transparent,
                                focusedIndicatorColor = MaterialTheme.colors.error
                            ),
                            keyboardActions = KeyboardActions(onDone = {
                                focusManager.clearFocus()
                            }),

                            value = login.value,
                            onValueChange = { letter -> login.value = letter }
                        )
                        OutlinedTextField(
                            isError = error != null,
                            enabled = !isLoadingState,
                            modifier = Modifier
                                .fillMaxWidth(),
                            singleLine = true,
                            visualTransformation = if (passwordVisibility.value) VisualTransformation.None else PasswordVisualTransformation(),
                            label = {
                                Text(text = "Пароль")
                            }, colors = TextFieldDefaults.textFieldColors(
                                backgroundColor = Color.Transparent,
                                focusedIndicatorColor = MaterialTheme.colors.error
                            ),
                            keyboardActions = KeyboardActions(onDone = {
                                focusManager.clearFocus()
                            }),
                            value = password.value,
                            onValueChange = { letter -> password.value = letter }
                        )
                        var buttonModifier: Modifier = Modifier
                        if (uiState.isLoading) {
                            buttonModifier = buttonModifier.shimmer()
                        }
                        Text(
                            text = errorLoginText,
                            fontSize = 20.sp,
                            color = Color.Red,
                            fontWeight = FontWeight(400)
                        )
                        Button(
                            enabled = !uiState.isLoading,
                            modifier = buttonModifier
                                .fillMaxWidth()
                                .height(58.dp)
                                .focusRequester(focusRequester),
                            colors = ButtonDefaults.buttonColors(
                                backgroundColor = Color.Blue.copy(alpha = 0.7f),
                                contentColor = Color.LightGray,
                            ),
                            onClick = {
                                focusManager.clearFocus()
                                if (!isAuthorized) {
                                    authViewModel.onEvent(
                                        AuthEvents.Authorise(
                                            login.value,
                                            password.value
                                        )
                                    )
                                } else {
                                    uiState = uiState.copy(login = "", isAuthorised = false, isLoading = false, token = 0)
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
                            Text(text = "Продолжить")
                        }
                    }
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {
                        Image(
                            modifier = Modifier
                                .padding(top = 50.dp)
                                .scale(0.75f),
                            painter = painterResource(id = R.drawable.ic_logo_auth),
                            contentDescription = null,
                            contentScale = ContentScale.FillBounds,
                            alignment = Alignment.Center
                        )
                    }
                }
            }
        }
    }
}
