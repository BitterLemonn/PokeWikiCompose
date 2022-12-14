package com.poke.pokewikicompose.ui.detail

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.poke.pokewikicompose.R
import com.poke.pokewikicompose.dataBase.data.bean.PokemonDetailBean
import com.poke.pokewikicompose.ui.SNACK_ERROR
import com.poke.pokewikicompose.ui.SNACK_SUCCESS
import com.poke.pokewikicompose.ui.detail.info.InfoPage
import com.poke.pokewikicompose.ui.detail.moves.MovesPage
import com.poke.pokewikicompose.ui.detail.state.StatePage
import com.poke.pokewikicompose.ui.popupSnackBar
import com.poke.pokewikicompose.ui.theme.BackGround
import com.poke.pokewikicompose.ui.widget.BottomNaviBar
import com.poke.pokewikicompose.ui.widget.NaviItem
import com.poke.pokewikicompose.ui.widget.PokemonTag
import com.poke.pokewikicompose.ui.widget.WarpLoadingDialog
import com.poke.pokewikicompose.utils.AppCache
import com.poke.pokewikicompose.utils.getColorByText
import com.poke.pokewikicompose.utils.getPokemonColor
import com.zj.mvi.core.observeEvent
import kotlinx.coroutines.launch
import java.io.File

@OptIn(ExperimentalPagerApi::class)
@Composable
fun DetailPage(
    pokemonID: Int,
    scaffoldState: ScaffoldState,
    navCtrl: NavController,
    viewModel: DetailPageViewModel = viewModel()
) {
    val itemList = remember { mutableStateListOf<NaviItem>() }

    val lifecycleOwner = LocalLifecycleOwner.current
    val coroutineScope = rememberCoroutineScope()

    var imageCacheItem by remember { mutableStateOf(AppCache.getPathItem(pokemonID)) }
    val detailCacheItem = AppCache.getDetailItem(pokemonID)

    var isLike by remember { mutableStateOf(false) }
    var isInit by remember { mutableStateOf(false) }
    var isShowLoading by remember { mutableStateOf(false) }
    var pokeDetail by remember { mutableStateOf(PokemonDetailBean.getEmpty()) }

    val pageStates = rememberPagerState(0)
    var curPage by remember { mutableStateOf(pageStates.currentPage) }

    LaunchedEffect(pokeDetail) {
        if (pokeDetail.pokemon_type.isNotEmpty()) {
            isLike = pokeDetail.is_star == 1
        }
    }

    LaunchedEffect(Unit) {
        itemList.add(NaviItem(R.drawable.pokemon_info, "??????"))
        itemList.add(NaviItem(R.drawable.pokemon_state, "??????"))
        itemList.add(NaviItem(R.drawable.pokemon_move, "??????"))

        viewModel.viewEvents.observeEvent(lifecycleOwner) {
            when (it) {
                is DetailPageViewEvents.TransDetail -> {
                    pokeDetail = it.pokeDetail
                    isInit = true
                }
                is DetailPageViewEvents.ShowLoadingDialog -> isShowLoading = true
                is DetailPageViewEvents.DismissLoadingDialog -> isShowLoading = false
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
                    if (isLike) "????????????"
                    else "??????????????????"
                )
                is DetailPageViewEvents.LikeActionFailure -> {
                    popupSnackBar(
                        coroutineScope,
                        scaffoldState,
                        SNACK_ERROR,
                        it.msg
                    )
                    isLike = !isLike
                }
            }
        }
        if (detailCacheItem == null)
            viewModel.dispatch(DetailPageViewActions.GetDetailWithID(pokemonID))
        else {
            pokeDetail = detailCacheItem
            isInit = true
        }
    }
    rememberSystemUiController().setSystemBarsColor(
        getPokemonColor(pokeDetail.pokemon_color),
        darkIcons = MaterialTheme.colors.isLight
    )
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = getPokemonColor(pokeDetail.pokemon_color)
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
                        viewModel.dispatch(DetailPageViewActions.ClickLike(isLike))
                        isLike = !isLike
                    },
                    contentPadding = PaddingValues()
                ) {
                    if (!isLike)
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
        // ??????
        Surface(
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier
                .padding(top = 158.dp, start = 28.dp, end = 28.dp, bottom = 40.dp)
                .fillMaxHeight()
                .fillMaxWidth(),
            elevation = 20.dp,
            color = BackGround
        ) {
            AnimatedVisibility(
                visible = isInit,
                enter = fadeIn(),
                exit = fadeOut()
            ) {
                // ??????
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 38.dp, end = 18.dp),
                    horizontalAlignment = Alignment.End
                ) {
                    Box {
                        PokemonTag(text = pokeDetail.pokemon_id)
                    }
                }
                // ?????? ??????
                Column(
                    modifier = Modifier.padding(top = 60.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = pokeDetail.pokemon_name,
                        fontSize = 20.sp,
                        color = Color.Black,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    LazyRow(
                        modifier = Modifier
                            .padding(horizontal = 110.dp)
                            .fillMaxWidth(),
                        horizontalArrangement = if (pokeDetail.pokemon_type.size > 1)
                            Arrangement.SpaceBetween
                        else Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        items(pokeDetail.pokemon_type) {
                            PokemonTag(text = it, isColored = true)
                        }
                    }
                }
            }
            Column(
                modifier = Modifier
                    .padding(top = 130.dp)
                    .fillMaxWidth()
                    .fillMaxHeight()
            ) {
                HorizontalPager(
                    count = 3,
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .padding(top = 10.dp),
                    state = pageStates,
                    verticalAlignment = Alignment.Top
                ) { page ->
                    AnimatedVisibility(
                        visible = isInit,
                        enter = fadeIn(),
                        exit = fadeOut()
                    ) {
                        curPage = pageStates.currentPage
                        when (page) {
                            0 -> InfoPage(
                                getColorByText(pokeDetail.pokemon_type[0]),
                                pokeDetail
                            ) {
                                AppCache.getDetailItem(it)?.let { detail ->
                                    pokeDetail = detail
                                    imageCacheItem = AppCache.getPathItem(it)
                                } ?: viewModel.dispatch(DetailPageViewActions.GetDetailWithID(it))
                            }
                            1 -> StatePage(
                                pokeDetail.poke_stat
                            )
                            2 -> MovesPage(
                                pokeDetail.poke_moves.moves.sortedBy { it.level }
                            )
                        }
                    }
                }
                Box(
                    modifier = Modifier
                        .height(60.dp)
                        .fillMaxWidth()
                        .background(Color.White)
                ) {
                    Divider(
                        modifier = Modifier.fillMaxWidth(),
                        color = Color.LightGray
                    )
                    BottomNaviBar(
                        itemList = itemList,
                        chooseIndex = curPage
                    ) {
                        coroutineScope.launch { pageStates.scrollToPage(it) }
                    }
                }
            }
        }
        // ???????????????
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 32.dp),
            contentAlignment = Alignment.TopCenter
        ) {
            Image(
                modifier = Modifier.size(170.dp),
                painter = painterResource(R.drawable.pokemon_detail_bg),
                contentDescription = "detail background"
            )
            AnimatedVisibility(
                visible = isInit,
                enter = fadeIn(),
                exit = fadeOut()
            ) {
                AsyncImage(
                    modifier = Modifier
                        .size(150.dp),
                    model = imageCacheItem?.let {
                        it.bigPath?.let { path -> File(path) } ?: pokeDetail.img_url
                    } ?: pokeDetail.img_url,
                    contentDescription = "pokemon image"
                )
            }
        }


    }
    if (isShowLoading) {
        WarpLoadingDialog(text = "????????????")
    }
}

@Composable
@Preview
private fun DetailPreview() {
    DetailPage(
        pokemonID = 1,
        scaffoldState = rememberScaffoldState(),
        navCtrl = rememberNavController()
    )
}