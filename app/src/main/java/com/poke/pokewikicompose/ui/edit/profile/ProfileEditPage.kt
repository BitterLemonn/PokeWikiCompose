package com.poke.pokewikicompose.ui.edit.profile

import androidx.activity.OnBackPressedCallback
import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Scaffold
import androidx.compose.material.ScaffoldState
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.os.bundleOf
import androidx.navigation.NavController
import androidx.navigation.NavOptions
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import com.poke.pokewikicompose.R
import com.poke.pokewikicompose.ui.theme.AppTheme
import com.poke.pokewikicompose.ui.widget.ScreenItemBtn
import com.poke.pokewikicompose.ui.widget.TitleBar
import com.poke.pokewikicompose.utils.AppContext
import com.poke.pokewikicompose.utils.MAIN_PAGE

@Composable
fun ProfileEditPage(
    navCtrl: NavController,
    scaffoldState: ScaffoldState?
) {
    val userInfo = remember { mutableStateOf(AppContext.userData) }.value

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TitleBar(
                title = "个人信息编辑",
                onBackClick = {
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
                Text(
                    text = userInfo.username,
                    color = Color.Black,
                    fontSize = 16.sp,
                    modifier = Modifier.align(Alignment.Center)
                )
                Image(
                    painter = painterResource(R.drawable.edit_icon),
                    contentDescription = "edit username",
                    modifier = Modifier
                        .align(Alignment.CenterEnd)
                        .size(30.dp)
                        .clip(CircleShape)
                        .clickable {

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