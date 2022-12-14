package com.poke.pokewikicompose.ui.login

import androidx.activity.OnBackPressedCallback
import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
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
import androidx.compose.ui.tooling.preview.Preview
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
import com.poke.pokewikicompose.utils.REGISTER_PAGE
import com.zj.mvi.core.observeEvent
import com.zj.mvi.core.observeState

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun LoginPage(
    navCtrl: NavController,
    scaffoldState: ScaffoldState,
    viewModel: LoginViewModel = viewModel()
) {
    val viewStates = viewModel.viewStates
    val coroutineState = rememberCoroutineScope()
    val keyboardController = LocalSoftwareKeyboardController.current
    val lifecycleOwner = LocalLifecycleOwner.current

    val isShowDialog = remember { mutableStateOf(false) }
    val email = remember { mutableStateOf(viewStates.value.email) }
    val password = remember { mutableStateOf(viewStates.value.password) }
    val enable = remember { mutableStateOf(viewStates.value.enable) }

    val callback = remember {
        object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                // ????????????
            }
        }
    }
    val dispatcher = LocalOnBackPressedDispatcherOwner.current?.onBackPressedDispatcher

    LaunchedEffect(Unit) {
        viewModel.viewEvent.observeEvent(lifecycleOwner) {
            when (it) {
                is LoginViewEvent.ShowToast ->
                    popupSnackBar(coroutineState, scaffoldState, label = SNACK_ERROR, it.msg)
                is LoginViewEvent.TransIntent -> {
                    navCtrl.navigate(MAIN_PAGE) {
                        popUpTo(LOGIN_PAGE) { inclusive = true }
                    }
                }
                is LoginViewEvent.ShowLoadingDialog -> isShowDialog.value = true
                is LoginViewEvent.DismissLoadingDialog -> isShowDialog.value = false
            }
        }
        viewStates.let { states ->
            states.observeState(lifecycleOwner, LoginViewState::email) {
                email.value = it
            }
            states.observeState(lifecycleOwner, LoginViewState::password) {
                password.value = it
            }
            states.observeState(lifecycleOwner, LoginViewState::enable) {
                enable.value = it
            }
        }
    }
    // ???????????????
    Image(
        painter = painterResource(id = R.drawable.login_pikachu),
        contentDescription = "login_pikachu",
        modifier = Modifier
            .width(200.dp)
            .height(200.dp)
            .absoluteOffset(x = (-50).dp, y = (-70).dp),
        alignment = Alignment.TopStart
    )
    Column(modifier = Modifier.padding(top = 56.dp)) {
        Image(
            painter = painterResource(id = R.drawable.logo),
            contentDescription = "logo",
            modifier = Modifier
                .fillMaxWidth()
                .height(60.dp)
        )
        Spacer(modifier = Modifier.height(46.dp))
        Column(
            modifier = Modifier
                .fillMaxWidth(fraction = 0.8f)
                .align(Alignment.CenterHorizontally)
        ) {
            // ????????????
            AuthInputEditText(
                hint = stringResource(id = R.string.email_hint),
                value = email.value,
                onValueChange = { viewModel.dispatch(LoginViewAction.UpdateEmail(it)) },
                isTransform = false,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Email,
                    imeAction = ImeAction.Next
                )
            )
            Spacer(modifier = Modifier.height(46.dp))
            // ????????????
            AuthInputEditText(
                hint = stringResource(id = R.string.password_hint),
                value = password.value,
                onValueChange = { viewModel.dispatch(LoginViewAction.UpdatePassword(it)) },
                isTransform = true,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Password,
                    imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(
                    onDone = {
                        keyboardController?.hide()
                        viewModel.dispatch(LoginViewAction.OnLoginClicked)
                    }
                )
            )
            Spacer(modifier = Modifier.height(67.dp))

            // ?????????
            Surface(
                color = Color.Transparent,
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .fillMaxWidth()
            ) {
                // ????????????
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.login_mewtwo),
                        contentDescription = "login mewtwo",
                        modifier = Modifier
                            .width(100.dp)
                            .height(100.dp)
                    )
                    Image(
                        painter = painterResource(id = R.drawable.login_mew),
                        contentDescription = "login mew",
                        modifier = Modifier
                            .width(100.dp)
                            .height(80.dp)
                    )
                }
                // ????????????
                Button(
                    modifier = Modifier.wrapContentWidth(),
                    onClick = {
                        keyboardController?.hide()
                        navCtrl.navigate(REGISTER_PAGE)
                    },
                    elevation = ButtonDefaults.elevation(
                        defaultElevation = 0.dp,
                        hoveredElevation = 0.dp,
                        pressedElevation = 0.dp
                    ),
                    colors = ButtonDefaults.buttonColors(backgroundColor = Color.Transparent)
                ) {
                    Text(
                        modifier = Modifier
                            .padding(vertical = 5.dp),
                        text = stringResource(
                            id = R.string.register_now
                        ),
                        fontSize = 14.sp,
                        color = Color(0xFF766F6F)
                    )
                }
            }

            // ????????????
            Button(
                onClick = {
                    keyboardController?.hide()
                    viewModel.dispatch(LoginViewAction.OnLoginClicked)
                },
                enabled = enable.value,
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .fillMaxWidth()
                    .height(60.dp)
                    .offset(y = (-60).dp),
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = AppTheme.colors.primaryBtnBg
                ),
                shape = RoundedCornerShape(8.dp),
                elevation = ButtonDefaults.elevation(
                    defaultElevation = 10.dp,
                    pressedElevation = 5.dp
                )
            ) {
                Text(
                    text = stringResource(id = R.string.login),
                    fontSize = 14.sp,
                    color = Color(android.graphics.Color.parseColor("#FEFAFA"))
                )
            }
        }
    }

    if (isShowDialog.value) {
        WarpLoadingDialog("????????????", 120)
        dispatcher?.addCallback(callback)
    } else {
        callback.remove()
    }
}

@Preview(showBackground = true)
@Composable
fun MainPreView() {
//    LoginPage()
}