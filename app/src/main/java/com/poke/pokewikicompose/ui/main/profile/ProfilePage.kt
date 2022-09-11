package com.poke.pokewikicompose.ui.main.profile

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.poke.pokewikicompose.R
import com.poke.pokewikicompose.dataBase.GlobalDataBase
import com.poke.pokewikicompose.ui.theme.AppTheme
import com.poke.pokewikicompose.ui.theme.BtnText
import com.poke.pokewikicompose.ui.theme.Error
import com.poke.pokewikicompose.ui.widget.ScreenItemBtn
import com.poke.pokewikicompose.utils.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch

@Composable
fun ProfilePage(
    scaffoldState: ScaffoldState? = null,
    navCtrl: NavController? = null
) {
    val coroutineScope = rememberCoroutineScope()
    val userInfo = AppContext.userData
    val isAutoCache = remember { mutableStateOf(AppContext.localSetting.isAutoCache) }
    Column(
        modifier = Modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceEvenly
    ) {
        Spacer(modifier = Modifier.height(20.dp))
        // 个人信息卡片
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
                .weight(0.25f),
            elevation = 10.dp,
            shape = RoundedCornerShape(6.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .padding(start = 20.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Image(
                    modifier = Modifier
                        .clip(CircleShape)
                        .size(90.dp),
                    painter = rememberAsyncImagePainter(
                        model = if (userInfo.profile_photo.isNullOrBlank()) R.drawable.default_icon
                        else userInfo.profile_photo
                    ),
                    contentDescription = "user icon"
                )
                Spacer(modifier = Modifier.width(20.dp))
                Column(
                    modifier = Modifier
                        .weight(0.6f)
                        .height(90.dp),
                    verticalArrangement = Arrangement.SpaceEvenly,
                    horizontalAlignment = Alignment.Start
                ) {
                    Text(
                        text = userInfo.username,
                        color = Color.Black,
                        fontSize = 28.sp
                    )
                    Text(
                        text = "在这里，探索宝可梦",
                        color = BtnText,
                        fontSize = 16.sp
                    )
                }
            }
        }
        // 功能按钮
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(0.6f),
            verticalArrangement = Arrangement.SpaceEvenly,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            ScreenItemBtn(
                modifier = Modifier.padding(horizontal = 20.dp),
                leftIcon = R.drawable.collection_icon,
                text = "我的收藏",
                isShowArrow = true
            ) {
                navCtrl?.navigate(COLLECTION_PAGE)
            }
            ScreenItemBtn(
                modifier = Modifier.padding(horizontal = 20.dp),
                leftIcon = R.drawable.edit_item_icon,
                text = "编辑个人信息",
                isShowArrow = true
            ) {
                navCtrl?.navigate(PROFILE_EDIT_PAGE)
            }
            ScreenItemBtn(
                modifier = Modifier.padding(horizontal = 20.dp),
                leftIcon = R.drawable.advice_icon,
                text = "意见反馈",
                isShowArrow = true
            ) {
                navCtrl?.navigate(FEEDBACK_PAGE)
            }
            ScreenItemBtn(
                modifier = Modifier.padding(horizontal = 20.dp),
                leftIcon = R.drawable.cache_icon,
                text = "自动缓存",
                subText = "开启自动缓存后可离线查看信息",
                switchInitState = isAutoCache.value,
                onSwitchChange = {
                    isAutoCache.value = it
                    AppContext.localSetting = AppContext.localSetting.copy(
                        isAutoCache = it
                    )
                    coroutineScope.launch {
                        flow {
                            GlobalDataBase.database.localSettingDao().updateLocalSetting(
                                AppContext.localSetting
                            )
                            emit("")
                        }.flowOn(Dispatchers.IO).collect()
                    }
                }
            )
        }
        // 退出登录
        Box(
            modifier = Modifier.weight(0.3f),
            contentAlignment = Alignment.Center
        ) {
            Button(
                colors = ButtonDefaults.buttonColors(
                    Color.Transparent, Color.Transparent, Color.Transparent, Color.Transparent
                ),
                elevation = ButtonDefaults.elevation(0.dp, 0.dp, 0.dp, 0.dp),
                onClick = {
                    AppContext.logout()
                    coroutineScope.launch {
                        flow {
                            GlobalDataBase.database.userDao().deleteAll()
                            emit("")
                        }.flowOn(Dispatchers.IO).collect()
                    }
                    navCtrl?.navigate(LOGIN_PAGE) {
                        popUpTo(MAIN_PAGE) { inclusive = true }
                    }
                },
            ) {
                Text(
                    text = "退出账号",
                    color = Error
                )
            }
        }
    }
}

@Composable
@Preview(showBackground = true)
fun ProfilePreview() {
    AppTheme {
        Surface(color = AppTheme.colors.background) {
            ProfilePage()
        }
    }
}