package com.poke.pokewikicompose.ui.search

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.poke.pokewikicompose.R
import com.poke.pokewikicompose.dataBase.data.bean.PokemonDetailBean
import com.poke.pokewikicompose.ui.SNACK_ERROR
import com.poke.pokewikicompose.ui.SNACK_SUCCESS
import com.poke.pokewikicompose.ui.popupSnackBar
import com.poke.pokewikicompose.ui.theme.BackGround
import com.poke.pokewikicompose.ui.widget.PokemonTag
import com.poke.pokewikicompose.ui.widget.WarpLoadingDialog
import com.poke.pokewikicompose.utils.getPokemonColor
import com.zj.mvi.core.observeEvent
import com.zj.mvi.core.observeState

@Composable
fun DetailPage(
    pokemonID: Int,
    scaffoldState: ScaffoldState,
    navCtrl: NavController,
    viewModel: DetailPageViewModel = viewModel()
) {
    val lifecycleOwner = LocalLifecycleOwner.current
    val coroutineScope = rememberCoroutineScope()

    val isLike = remember { mutableStateOf(false) }
    val isInit = remember { mutableStateOf(false) }
    val isShowLoading = remember { mutableStateOf(false) }
    val pokeDetail = remember { mutableStateOf(PokemonDetailBean.getEmpty()) }

    LaunchedEffect(Unit) {
        viewModel.viewEvents.observeEvent(lifecycleOwner) {
            when (it) {
                is DetailPageViewEvents.TransDetail -> {
                    pokeDetail.value = it.pokeDetail
                    isInit.value = true
                }
                is DetailPageViewEvents.ShowLoadingDialog -> isShowLoading.value = true
                is DetailPageViewEvents.DismissLoadingDialog -> isShowLoading.value = false
                is DetailPageViewEvents.ShowToast -> popupSnackBar(
                    coroutineScope,
                    scaffoldState,
                    SNACK_ERROR,
                    it.msg
                )
                is DetailPageViewEvents.LikeActionSuccess -> popupSnackBar(
                    coroutineScope,
                    scaffoldState,
                    SNACK_SUCCESS,
                    "收藏成功"
                )
                is DetailPageViewEvents.LikeActionFailure -> popupSnackBar(
                    coroutineScope,
                    scaffoldState,
                    SNACK_ERROR,
                    it.msg
                )
            }
        }

        viewModel.viewStates.let { states ->
            states.observeState(lifecycleOwner, DetailPageViewStates::isLike) {
                isLike.value = it
            }
        }
        viewModel.dispatch(DetailPageViewActions.GetDetailWithID(pokemonID))
    }
    rememberSystemUiController().setStatusBarColor(
        getPokemonColor(pokeDetail.value.pokemon_color),
        darkIcons = MaterialTheme.colors.isLight
    )
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = getPokemonColor(pokeDetail.value.pokemon_color)
    ) {
        // toolbar
        Box(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 15.dp, start = 15.dp, end = 15.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Button(
                    modifier = Modifier
                        .size(40.dp),
                    colors = ButtonDefaults.buttonColors(
                        Color.Transparent,
                        Color.Transparent,
                        Color.Transparent,
                        Color.Transparent
                    ),
                    elevation = ButtonDefaults.elevation(0.dp, 0.dp, 0.dp, 0.dp),
                    onClick = {
                        navCtrl.popBackStack()
                    },
                    contentPadding = PaddingValues()
                ) {
                    Image(
                        modifier = Modifier
                            .size(30.dp),
                        painter = painterResource(R.drawable.black_back),
                        contentDescription = "back button"
                    )
                }

                Button(
                    modifier = Modifier
                        .size(40.dp),
                    colors = ButtonDefaults.buttonColors(
                        Color.Transparent,
                        Color.Transparent,
                        Color.Transparent,
                        Color.Transparent
                    ),
                    elevation = ButtonDefaults.elevation(0.dp, 0.dp, 0.dp, 0.dp),
                    onClick = {
                        viewModel.dispatch(DetailPageViewActions.ClickLike)
                    },
                    contentPadding = PaddingValues()
                ) {
                    if (!isLike.value)
                        Image(
                            modifier = Modifier
                                .size(30.dp),
                            painter = painterResource(R.drawable.pokemon_love),
                            contentDescription = "like button"
                        )
                    else
                        Image(
                            modifier = Modifier
                                .size(30.dp),
                            painter = painterResource(R.drawable.pokemon_love_select),
                            contentDescription = "unlike button"
                        )
                }
            }

        }
        // 内容
        Surface(
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier
                .padding(top = 158.dp, start = 28.dp, end = 28.dp, bottom = 40.dp)
                .fillMaxHeight()
                .fillMaxWidth(),
            elevation = 20.dp,
            color = BackGround
        ) {
            if (isInit.value) {
                // 标签
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 38.dp, end = 18.dp),
                    horizontalAlignment = Alignment.End
                ) {
                    Box {
                        PokemonTag(text = pokeDetail.value.pokemon_id)
                    }
                }
                // 名字 属性
                Column(
                    modifier = Modifier.padding(top = 60.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = pokeDetail.value.pokemon_name,
                        fontSize = 20.sp,
                        color = Color.Black,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    LazyRow(
                        modifier = Modifier
                            .padding(horizontal = 85.dp)
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        items(pokeDetail.value.pokemon_type) {
                            PokemonTag(text = it, isColored = true)
                        }
                    }
                }
            }
        }
        // 宝可梦图片
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 32.dp),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    modifier = Modifier.size(170.dp),
                    painter = painterResource(R.drawable.pokemon_detail_bg),
                    contentDescription = "detail background"
                )
                if (isInit.value)
                    AsyncImage(
                        modifier = Modifier
                            .size(150.dp),
                        model = pokeDetail.value.img_url,
                        contentDescription = "pokemon image"
                    )
            }
        }


    }
    if (isShowLoading.value) {
        WarpLoadingDialog(text = "正在获取")
    }
}

@Composable
@Preview
fun preview() {
    DetailPage(
        pokemonID = 1,
        scaffoldState = rememberScaffoldState(),
        navCtrl = rememberNavController()
    )
}