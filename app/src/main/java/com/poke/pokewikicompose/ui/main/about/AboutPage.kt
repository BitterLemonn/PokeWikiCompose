package com.poke.pokewikicompose.ui.main.about

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ScaffoldState
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.poke.pokewikicompose.R
import com.poke.pokewikicompose.ui.SNACK_SUCCESS
import com.poke.pokewikicompose.ui.popupSnackBar
import com.poke.pokewikicompose.ui.theme.BtnText
import com.poke.pokewikicompose.ui.widget.ScreenItemBtn
import com.poke.pokewikicompose.ui.widget.WarpLoadingDialog
import com.poke.pokewikicompose.utils.COVER_PAGE
import com.poke.pokewikicompose.utils.FEEDBACK_PAGE
import com.poke.pokewikicompose.utils.getAppVersionName
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.random.Random

@Composable
fun AboutPage(
    scaffoldState: ScaffoldState,
    navCtrl: NavController
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val isLoading = remember { mutableStateOf(false) }

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(bottom = 70.dp)
        ) {
            Column(
                verticalArrangement = Arrangement.SpaceEvenly,
                modifier = Modifier
                    .weight(0.3f)
                    .padding(top = 10.dp)
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Image(
                        painter = painterResource(R.drawable.app_icon),
                        contentDescription = "icon",
                        modifier = Modifier.clip(RoundedCornerShape(20.dp))
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = "???????????? v${getAppVersionName(context)}",
                        color = Color.Black,
                        fontSize = 16.sp
                    )
                }
            }
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceEvenly,
                modifier = Modifier.weight(0.7f)
            ) {
                ScreenItemBtn(
                    leftIcon = R.drawable.update,
                    text = "????????????",
                    modifier = Modifier.padding(horizontal = 18.dp)
                ) {
                    coroutineScope.launch {
                        isLoading.value = true
                        delay(Random.nextLong(500, 1_500))
                        popupSnackBar(
                            coroutineScope,
                            scaffoldState,
                            label = SNACK_SUCCESS,
                            message = "????????????????????????"
                        )
                        isLoading.value = false
                    }
                }
                ScreenItemBtn(
                    leftIcon = R.drawable.like,
                    text = "???PokeWiKi??????",
                    modifier = Modifier.padding(horizontal = 18.dp)
                ) {
                    popupSnackBar(
                        coroutineScope,
                        scaffoldState,
                        label = SNACK_SUCCESS,
                        message = "?????????????????????"
                    )
                }
                ScreenItemBtn(
                    leftIcon = R.drawable.welcome,
                    text = "?????????",
                    modifier = Modifier.padding(horizontal = 18.dp)
                ) {
                    navCtrl.navigate("$COVER_PAGE?skipType=About")
                }
                ScreenItemBtn(
                    leftIcon = R.drawable.contact,
                    text = "????????????",
                    modifier = Modifier.padding(horizontal = 18.dp)
                ) {
                    navCtrl.navigate(FEEDBACK_PAGE)
                }
            }
        }
        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 20.dp)
        ) {
            Column {
                Text(
                    text = "?????????????????? | ???????????? | ???????????? | ??????????????????",
                    color = Color(0xFF018EFC),
                    fontSize = 12.sp
                )
                Spacer(modifier = Modifier.height(10.dp))
                Text(
                    text = "Copyright@2022-2022 PokeWiki-Inc.All Rights Reserved",
                    color = BtnText,
                    fontSize = 11.sp
                )
            }
        }
    }

    if (isLoading.value)
        WarpLoadingDialog(
            text = "??????????????????",
            dialogAlpha = 0.8f
        )
}