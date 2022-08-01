package com.poke.pokewikicompose.ui.register

import androidx.activity.OnBackPressedCallback
import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.ScaffoldState
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
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
import com.poke.pokewikicompose.utils.REGISTER_PAGE
import com.poke.pokewikicompose.utils.SEARCH_MAIN_PAGE

@Composable
fun RegisterPage(
    viewModel: RegisterViewModel = viewModel(),
    scaffoldState: ScaffoldState,
    navCtrl: NavController
) {
    val viewStates = viewModel.viewStates
    val coroutineState = rememberCoroutineScope()

    val isShowDialog = remember { mutableStateOf(false) }

    val callback = remember {
        object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                // 拦截返回
            }
        }
    }
    val dispatcher = LocalOnBackPressedDispatcherOwner.current?.onBackPressedDispatcher

    DisposableEffect(Unit) {
        onDispose {
            viewModel.dispatch(RegisterViewAction.UpdateEmail(""))
            viewModel.dispatch(RegisterViewAction.UpdatePassword(""))
            viewModel.dispatch(RegisterViewAction.UpdateCertain(""))
        }
    }

    LaunchedEffect(Unit) {
        viewModel.viewEvents.collect {
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
                    navCtrl.navigate(SEARCH_MAIN_PAGE){
                        popUpTo(REGISTER_PAGE){ inclusive = true }
                    }
                }
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
                value = viewStates.email,
                onValueChange = { viewModel.dispatch(RegisterViewAction.UpdateEmail(it)) },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Email,
                    imeAction = ImeAction.Next
                )
            )
            Spacer(modifier = Modifier.height(46.dp))
            AuthInputEditText(
                hint = stringResource(id = R.string.password_reg_hint),
                value = viewStates.password,
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
                value = viewStates.certain,
                onValueChange = { viewModel.dispatch(RegisterViewAction.UpdateCertain(it)) },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Password,
                    imeAction = ImeAction.Done
                )
            )
            Spacer(modifier = Modifier.height(106.dp))
            // 注册按钮
            Button(
                onClick = {
                    if (viewStates.same)
                        viewModel.dispatch(RegisterViewAction.OnRegisterClicked)
                    else
                        popupSnackBar(
                            coroutineState,
                            scaffoldState,
                            label = SNACK_ERROR,
                            "两次输入的密码不正确"
                        )
                },
                enabled = viewStates.enable,
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
    }else
        callback.remove()
}

@Composable
@Preview
fun RegisterPreview() {
//    RegisterPage()
}