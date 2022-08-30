package com.poke.pokewikicompose.ui.edit.profile

import androidx.activity.OnBackPressedCallback
import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.compose.rememberAsyncImagePainter
import com.orhanobut.logger.Logger
import com.poke.pokewikicompose.R
import com.poke.pokewikicompose.ui.SNACK_ERROR
import com.poke.pokewikicompose.ui.SNACK_SUCCESS
import com.poke.pokewikicompose.ui.popupSnackBar
import com.poke.pokewikicompose.ui.theme.AppTheme
import com.poke.pokewikicompose.ui.theme.RoyalBlue
import com.poke.pokewikicompose.ui.widget.*
import com.poke.pokewikicompose.utils.AppContext
import com.poke.pokewikicompose.utils.PASSWORD_EDIT_PAGE
import com.zj.mvi.core.observeEvent
import github.leavesczy.matisse.Matisse
import github.leavesczy.matisse.MatisseContract

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun ProfileEditPage(
    navCtrl: NavController,
    scaffoldState: ScaffoldState,
    viewModel: ProfileEditViewModel = viewModel()
) {
    val coroutineScope = rememberCoroutineScope()
    val lifecycleOwner = LocalLifecycleOwner.current
    val imeCtrl = LocalSoftwareKeyboardController.current
    val dispatcher = LocalOnBackPressedDispatcherOwner.current?.onBackPressedDispatcher
    val viewStates = viewModel.viewStates

    val bottomButton = remember { mutableStateListOf<BottomButtonItem>() }
    val userInfo = remember { mutableStateOf(AppContext.userData) }.value
    val painter = rememberAsyncImagePainter(
        model = userInfo.profile_photo,
        placeholder = painterResource(R.drawable.default_icon),
        error = painterResource(R.drawable.default_icon)
    )
    val isChangeUsername = remember { mutableStateOf(false) }
    val isShowBottomDialog = remember { mutableStateOf(false) }
    val isShowWarn = remember { mutableStateOf(false) }
    val isShowLoading = remember { mutableStateOf(false) }
    val tmpName = remember { mutableStateOf(TextFieldValue(viewStates.value.username)) }
    val focusRequester = remember { FocusRequester() }

    val callback = remember {
        object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                // 显示是否返回提示
                isShowWarn.value = true
            }
        }
    }
    val launcher = rememberLauncherForActivityResult(MatisseContract()) {
        if (it.isNotEmpty()) {
            val mediaResource = it[0]
            val imageUri = mediaResource.uri
            val imagePath = mediaResource.path
            Logger.d("imagePath: $imagePath")
            viewModel.dispatch(ProfileEditViewActions.ChangeUserIcon(imagePath))
        }
    }

    LaunchedEffect(Unit) {

        bottomButton.add(BottomButtonItem(text = "拍照") {})
        bottomButton.add(BottomButtonItem(text = "从相册中选择") {
            launcher.launch(Matisse())
            isShowBottomDialog.value = false
        })

        viewModel.viewEvents.observeEvent(lifecycleOwner) {
            when (it) {
                is ProfileEditViewEvents.ShowLoadingDialog -> isShowLoading.value = true
                is ProfileEditViewEvents.DismissLoadingDialog -> isShowLoading.value = false
                is ProfileEditViewEvents.ShowToast -> popupSnackBar(
                    coroutineScope,
                    scaffoldState,
                    SNACK_ERROR,
                    it.msg
                )
                is ProfileEditViewEvents.SuccessChangeName ->
                    popupSnackBar(
                        coroutineScope,
                        scaffoldState,
                        SNACK_SUCCESS,
                        "修改成功"
                    )
                is ProfileEditViewEvents.SuccessChangeIcon -> {
                    popupSnackBar(
                        coroutineScope,
                        scaffoldState,
                        SNACK_SUCCESS,
                        "修改成功"
                    )
                    // TODO 目前coil无法重新更换data 返回强制刷新
                    navCtrl.popBackStack()
                }
            }
        }
    }
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TitleBar(
                title = "个人信息编辑",
                onBackClick = {
                    if (isChangeUsername.value)
                        isShowWarn.value = true
                    else
                        navCtrl.popBackStack()
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxWidth()
                .fillMaxHeight(0.6f),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceEvenly
        ) {
            Image(
                painter = painter,
                contentDescription = "profile photo",
                modifier = Modifier
                    .size(100.dp)
                    .clip(CircleShape)
                    .clickable {
                        isShowBottomDialog.value = true
                    }
            )
            Box(modifier = Modifier.fillMaxWidth(0.5f).wrapContentHeight()) {
                TextField(
                    value = tmpName.value,
                    onValueChange = {
                        // 不能超过8个字符
                        if (it.text.length > 8) {
                            tmpName.value = TextFieldValue(it.text.slice(IntRange(0, 7)))
                                .copy(selection = TextRange(8))
                            popupSnackBar(
                                coroutineScope,
                                scaffoldState,
                                SNACK_ERROR,
                                "用户名最多为8个字符"
                            )
                        } else
                            tmpName.value = it
                    },
                    singleLine = true,
                    colors = TextFieldDefaults.textFieldColors(
                        textColor = Color.Black,
                        backgroundColor = Color.Transparent,
                        focusedIndicatorColor = if (isChangeUsername.value) RoyalBlue
                        else Color.Transparent,
                        unfocusedIndicatorColor = if (isChangeUsername.value) Color.Gray
                        else Color.Transparent
                    ),
                    textStyle = LocalTextStyle.current.copy(
                        fontSize = 16.sp,
                        textAlign = TextAlign.Center
                    ),
                    readOnly = !isChangeUsername.value,
                    modifier = Modifier
                        .align(Alignment.Center)
                        .padding(vertical = 0.dp)
                        .width(120.dp)
                        .focusRequester(focusRequester)
                        .onFocusChanged {
                            if (it.isFocused) {
                                Logger.d(imeCtrl)
                                imeCtrl?.show()
                                tmpName.value = tmpName.value.copy(
                                    selection = TextRange(tmpName.value.text.length)
                                )
                            }
                        },
                )
                Image(
                    painter = if (!isChangeUsername.value) painterResource(R.drawable.edit_icon)
                    else painterResource(R.drawable.edit_check),
                    contentDescription = "edit username",
                    modifier = Modifier
                        .align(Alignment.CenterEnd)
                        .size(30.dp)
                        .clip(CircleShape)
                        .clickable {
                            if (!isChangeUsername.value) {
                                isChangeUsername.value = !isChangeUsername.value
                                focusRequester.requestFocus()
                            } else {
                                isChangeUsername.value = !isChangeUsername.value
                                focusRequester.freeFocus()
                                viewModel.dispatch(ProfileEditViewActions.UpdateUsername(tmpName.value.text))
                                viewModel.dispatch(ProfileEditViewActions.ChangeUsername)
                            }
                        }
                )
            }
            ScreenItemBtn(
                modifier = Modifier.padding(horizontal = 20.dp),
                text = "修改密码",
                isShowArrow = true,
                onClick = {
                    navCtrl.navigate(PASSWORD_EDIT_PAGE)
                }
            )
        }
    }

    if (isShowLoading.value) {
        WarpLoadingDialog(text = "正在修改")
    }

    if (isShowWarn.value) {
        HintDialog(
            hint = "您的编辑还未保存\n是否退出",
            highLineCertain = false,
            onClickCertain = { navCtrl.popBackStack() },
            onClickCancel = { isShowWarn.value = false }
        )
    }
    BottomDialog(
        items = bottomButton,
        isShow = isShowBottomDialog
    )

    if (isChangeUsername.value) {
        dispatcher?.addCallback(callback)
    } else
        callback.remove()
}

@Composable
@Preview
private fun ProfileEditPreview() {
    AppTheme {
        Surface(
            color = AppTheme.colors.background,
            modifier = Modifier.fillMaxSize()
        ) {
            ProfileEditPage(navCtrl = rememberNavController(), rememberScaffoldState())
        }
    }
}