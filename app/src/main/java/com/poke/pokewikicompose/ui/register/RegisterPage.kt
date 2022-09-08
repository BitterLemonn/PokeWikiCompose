package com.poke.pokewikicompose.ui.register

import androidx.activity.OnBackPressedCallback
import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.ScaffoldState
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.poke.pokewikicompose.R
import com.poke.pokewikicompose.ui.SNACK_ERROR
import com.poke.pokewikicompose.ui.popupSnackBar
import com.poke.pokewikicompose.ui.theme.AppTheme
import com.poke.pokewikicompose.ui.widget.AuthInputEditText
import com.poke.pokewikicompose.ui.widget.WarpLoadingDialog
import com.poke.pokewikicompose.utils.LOGIN_PAGE
import com.poke.pokewikicompose.utils.MAIN_PAGE
import com.zj.mvi.core.observeEvent
import com.zj.mvi.core.observeState
import kotlinx.coroutines.launch

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun RegisterPage(
    viewModel: RegisterViewModel = viewModel(),
    scaffoldState: ScaffoldState,
    navCtrl: NavController
) {
    val viewStates = viewModel.viewStates
    val coroutineState = rememberCoroutineScope()
    val lifecycleOwner = LocalLifecycleOwner.current

    val isShowDialog = remember { mutableStateOf(false) }

    val callback = remember {
        object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                // 拦截返回
            }
        }
    }
    val dispatcher = LocalOnBackPressedDispatcherOwner.current?.onBackPressedDispatcher
    val keyboardController = LocalSoftwareKeyboardController.current

    val password = remember { mutableStateOf(viewStates.value.password) }
    val email = remember { mutableStateOf(viewStates.value.email) }
    val certain = remember { mutableStateOf(viewStates.value.certain) }
    val enable = remember { mutableStateOf(viewStates.value.enable) }

    DisposableEffect(Unit) {
        onDispose {
            viewModel.dispatch(RegisterViewAction.UpdateEmail(""))
            viewModel.dispatch(RegisterViewAction.UpdatePassword(""))
            viewModel.dispatch(RegisterViewAction.UpdateCertain(""))
        }
    }

    LaunchedEffect(Unit) {
        viewModel.viewEvents.observeEvent(lifecycleOwner) {
            when (it) {
                is RegisterViewEvent.ShowToast -> popupSnackBar(
                    coroutineState,
                    scaffoldState,
                    label = SNACK_ERROR,
                    it.msg
                )
                is RegisterViewEvent.ShowLoadingDialog -> isShowDialog.value = true
                is RegisterViewEvent.DismissLoadingDialog -> isShowDialog.value = false
                is RegisterViewEvent.TransIntent -> {
                    navCtrl.navigate(MAIN_PAGE) {
                        popUpTo(LOGIN_PAGE) { inclusive = true }
                    }
                }
            }
        }
        viewStates.let { states ->
            states.observeState(lifecycleOwner, RegisterViewState::email) {
                email.value = it
            }
            states.observeState(lifecycleOwner, RegisterViewState::password) {
                password.value = it
            }
            states.observeState(lifecycleOwner, RegisterViewState::certain) {
                certain.value = it
            }
            states.observeState(lifecycleOwner, RegisterViewState::enable) {
                enable.value = it
            }
        }
    }

    Box(
        modifier = Modifier.fillMaxWidth(),
        contentAlignment = Alignment.TopEnd
    ) {
        Image(
            painter = painterResource(id = R.drawable.register_squirtle),
            contentDescription = "register squirtle",
            modifier = Modifier
                .width(200.dp)
                .height(200.dp)
                .absoluteOffset(x = 60.dp, y = (-70).dp),
        )
    }
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.padding(top = 56.dp)
    ) {
        Image(
            painter = painterResource(id = R.drawable.logo),
            contentDescription = "logo",
            modifier = Modifier
                .fillMaxWidth()
                .height(60.dp)
        )
        Spacer(modifier = Modifier.height(46.dp))
        Column(modifier = Modifier.fillMaxWidth(0.8f)) {
            AuthInputEditText(
                hint = stringResource(id = R.string.email_reg_hint),
                value = email.value,
                onValueChange = { viewModel.dispatch(RegisterViewAction.UpdateEmail(it)) },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Email,
                    imeAction = ImeAction.Next
                )
            )
            Spacer(modifier = Modifier.height(46.dp))
            AuthInputEditText(
                hint = stringResource(id = R.string.password_reg_hint),
                value = password.value,
                onValueChange = { viewModel.dispatch(RegisterViewAction.UpdatePassword(it)) },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Password,
                    imeAction = ImeAction.Next
                ),
                isTransform = true
            )
            Spacer(modifier = Modifier.height(46.dp))
            AuthInputEditText(
                hint = stringResource(id = R.string.password_certain_hint),
                value = certain.value,
                onValueChange = { viewModel.dispatch(RegisterViewAction.UpdateCertain(it)) },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Password,
                    imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(
                    onDone = {
                        if (enable.value) {
                            keyboardController?.hide()
                            viewModel.dispatch(RegisterViewAction.OnRegisterClicked)
                        } else {
                            coroutineState.launch {
                                keyboardController?.hide()
                            }
                        }
                    }
                )
            )
            Spacer(modifier = Modifier.height(106.dp))
            // 注册按钮
            Button(
                onClick = {
                    keyboardController?.hide()
                    viewModel.dispatch(RegisterViewAction.OnRegisterClicked)
                },
                enabled = enable.value,
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .fillMaxWidth()
                    .height(60.dp)
                    .offset(y = (-60).dp),
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = AppTheme.colors.secondBtnBg
                ),
                shape = RoundedCornerShape(8.dp),
                elevation = ButtonDefaults.elevation(
                    defaultElevation = 10.dp,
                    pressedElevation = 5.dp
                )
            ) {
                Text(
                    text = stringResource(id = R.string.register),
                    fontSize = 14.sp,
                    color = Color(0xFFFEFAFA)
                )
            }
        }
    }

    if (isShowDialog.value) {
        WarpLoadingDialog("正在注册")
        dispatcher?.addCallback(callback)
    } else
        callback.remove()
}