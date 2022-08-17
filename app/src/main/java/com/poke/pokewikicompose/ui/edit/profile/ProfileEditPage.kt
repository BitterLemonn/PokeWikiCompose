package com.poke.pokewikicompose.ui.edit.profile

import androidx.activity.OnBackPressedCallback
import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import com.orhanobut.logger.Logger
import com.poke.pokewikicompose.R
import com.poke.pokewikicompose.ui.theme.AppTheme
import com.poke.pokewikicompose.ui.theme.RoyalBlue
import com.poke.pokewikicompose.ui.widget.HintDialog
import com.poke.pokewikicompose.ui.widget.ScreenItemBtn
import com.poke.pokewikicompose.ui.widget.TitleBar
import com.poke.pokewikicompose.utils.AppContext

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun ProfileEditPage(
    navCtrl: NavController,
    scaffoldState: ScaffoldState?
) {
    val imeCtrl = LocalSoftwareKeyboardController.current
    val dispatcher = LocalOnBackPressedDispatcherOwner.current?.onBackPressedDispatcher

    val userInfo = remember { mutableStateOf(AppContext.userData) }.value
    val isChangeUsername = remember { mutableStateOf(false) }
    val isShowWarn = remember { mutableStateOf(false) }
    val tmpName = remember { mutableStateOf(TextFieldValue(userInfo.username)) }
    val focusRequester = remember { FocusRequester() }

    val callback = remember {
        object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                // 显示是否返回提示
                isShowWarn.value = true
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
            AsyncImage(
                model = if (userInfo.profile_photo.isNullOrBlank()) R.drawable.default_icon
                else userInfo.profile_photo,
                contentDescription = "profile photo",
                modifier = Modifier
                    .size(100.dp)
                    .clip(CircleShape)
                    .clickable {

                    },
                error = painterResource(R.drawable.default_icon)
            )
            Box(modifier = Modifier.fillMaxWidth(0.5f).wrapContentHeight()) {
                TextField(
                    value = tmpName.value,
                    onValueChange = {
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
                            }
                        }
                )
            }
            ScreenItemBtn(
                modifier = Modifier.padding(horizontal = 20.dp),
                text = "修改密码",
                isShowArrow = true,
                onClick = {

                }
            )
        }
    }

    if (isShowWarn.value) {
        HintDialog(
            hint = "您的编辑还未保存\n是否退出",
            highLineCertain = false,
            onClickCertain = { navCtrl.popBackStack() },
            onClickCancel = { isShowWarn.value = false }
        )
    }

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
            ProfileEditPage(navCtrl = rememberNavController(), null)
        }
    }
}