package com.poke.pokewikicompose.ui.edit.profile.password

import androidx.activity.OnBackPressedCallback
import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
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
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.poke.pokewikicompose.ui.SNACK_ERROR
import com.poke.pokewikicompose.ui.SNACK_SUCCESS
import com.poke.pokewikicompose.ui.popupSnackBar
import com.poke.pokewikicompose.ui.theme.PokeBallRed
import com.poke.pokewikicompose.ui.widget.HintDialog
import com.poke.pokewikicompose.ui.widget.TitleBar
import com.poke.pokewikicompose.ui.widget.WarpLoadingDialog
import com.zj.mvi.core.observeEvent
import com.zj.mvi.core.observeState

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun PasswordEditPage(
    navCtrl: NavController,
    scaffoldState: ScaffoldState,
    viewModel: PasswordEditViewModel = viewModel()
) {
    val coroutineScope = rememberCoroutineScope()
    val lifecycleOwner = LocalLifecycleOwner.current
    val viewStates = viewModel.viewStates
    val imeCtrl = LocalSoftwareKeyboardController.current

    val showLoading = remember { mutableStateOf(false) }
    val showTip = remember { mutableStateOf(false) }
    val oldPassword = remember { mutableStateOf(viewStates.value.oldPassword) }
    val newPassword = remember { mutableStateOf(viewStates.value.newPassword) }
    val enabled = remember { mutableStateOf(viewStates.value.enabled) }

    val dispatcher = LocalOnBackPressedDispatcherOwner.current?.onBackPressedDispatcher
    val callback = remember {
        object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (oldPassword.value.isNotBlank() || newPassword.value.isNotBlank()) {
                    showTip.value = true
                    imeCtrl?.hide()
                } else
                    navCtrl.popBackStack()
            }
        }
    }

    LaunchedEffect(Unit) {
        viewStates.let { states ->
            states.observeState(lifecycleOwner, PasswordEditViewStates::oldPassword) {
                oldPassword.value = it
            }
            states.observeState(lifecycleOwner, PasswordEditViewStates::newPassword) {
                newPassword.value = it
            }
            states.observeState(lifecycleOwner, PasswordEditViewStates::enabled) {
                enabled.value = it
            }
        }

        viewModel.viewEvents.observeEvent(lifecycleOwner) {
            when (it) {
                is PasswordEditViewEvents.ShowToast ->
                    popupSnackBar(
                        coroutineScope,
                        scaffoldState,
                        SNACK_ERROR,
                        it.msg
                    )
                is PasswordEditViewEvents.ShowLoadingDialog -> showLoading.value = true
                is PasswordEditViewEvents.DismissLoadingDialog -> showLoading.value = false
                is PasswordEditViewEvents.SuccessChange -> {
                    navCtrl.popBackStack()
                    popupSnackBar(
                        coroutineScope,
                        scaffoldState,
                        SNACK_SUCCESS,
                        "修改成功"
                    )
                }
            }
        }
    }
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TitleBar(
                title = "修改密码",
                onBackClick = {
                    if (oldPassword.value.isNotBlank() || newPassword.value.isNotBlank()) {
                        showTip.value = true
                        imeCtrl?.hide()
                    } else
                        navCtrl.popBackStack()
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(Modifier.height(75.dp))
            TextField(
                modifier = Modifier
                    .padding(vertical = 0.dp),
                value = oldPassword.value,
                onValueChange = {
                    viewModel.dispatch(PasswordEditViewActions.UpdateOldPassword(it))
                },
                placeholder = { Text(text = "请输入旧密码", color = Color.Gray, fontSize = 16.sp) },
                textStyle = LocalTextStyle.current.copy(
                    fontSize = 16.sp,
                    color = Color(0xFF4C4C4C)
                ),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Password,
                    imeAction = ImeAction.Next
                ),
                shape = RoundedCornerShape(8.dp),
                colors = TextFieldDefaults.textFieldColors(
                    backgroundColor = Color(0xFFE6EBF6),
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent
                ),
                visualTransformation = PasswordVisualTransformation()
            )
            Spacer(Modifier.height(45.dp))
            TextField(
                modifier = Modifier
                    .padding(vertical = 0.dp),
                value = newPassword.value,
                onValueChange = {
                    viewModel.dispatch(PasswordEditViewActions.UpdateNewPassword(it))
                },
                placeholder = { Text(text = "请输入新密码", color = Color.Gray, fontSize = 16.sp) },
                textStyle = LocalTextStyle.current.copy(
                    fontSize = 16.sp,
                    color = Color(0xFF4C4C4C)
                ),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Password,
                    imeAction = ImeAction.Done
                ),
                shape = RoundedCornerShape(8.dp),
                colors = TextFieldDefaults.textFieldColors(
                    backgroundColor = Color(0xFFE6EBF6),
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent
                ),
                visualTransformation = PasswordVisualTransformation(),
                keyboardActions = KeyboardActions(
                    onDone = {
                        if (enabled.value) {
                            viewModel.dispatch(PasswordEditViewActions.ChangePassword)
                            imeCtrl?.hide()
                        }
                    }
                )
            )
            Spacer(Modifier.height(45.dp))
            Button(
                colors = ButtonDefaults.buttonColors(backgroundColor = PokeBallRed),
                elevation = ButtonDefaults.elevation(0.dp, 0.dp, 0.dp, 0.dp),
                onClick = {
                    viewModel.dispatch(PasswordEditViewActions.ChangePassword)
                    imeCtrl?.hide()
                },
                enabled = enabled.value,
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier.fillMaxWidth(0.7f).height(48.dp)
            ) {
                Text(
                    text = "确认",
                    color = Color.White,
                    fontSize = 20.sp
                )
            }
        }
    }
    if (showTip.value) {
        HintDialog(
            hint = "您还有更改没有保存\n是否返回",
            onClickCancel = { showTip.value = false },
            onClickCertain = { navCtrl.popBackStack() },
            highLineCertain = false
        )
    }
    if (showLoading.value) {
        WarpLoadingDialog(text = "正在修改")
    }
    dispatcher?.addCallback(callback)
}