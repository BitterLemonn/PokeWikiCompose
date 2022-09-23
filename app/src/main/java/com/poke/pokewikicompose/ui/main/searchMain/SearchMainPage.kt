package com.poke.pokewikicompose.ui.main.searchMain

import android.view.MotionEvent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ScaffoldState
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.listSaver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.motionEventSpy
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.orhanobut.logger.Logger
import com.poke.pokewikicompose.R
import com.poke.pokewikicompose.dataBase.data.bean.PokemonSearchBean
import com.poke.pokewikicompose.ui.theme.PokeBallRed
import com.poke.pokewikicompose.ui.widget.LazyLoadMoreColumn
import com.poke.pokewikicompose.ui.widget.PokeBallSearchBar
import com.poke.pokewikicompose.ui.widget.PokemonSearchCard
import com.poke.pokewikicompose.ui.widget.WarpLoadingDialog
import com.poke.pokewikicompose.utils.DETAIL_PAGE
import com.poke.pokewikicompose.utils.SEARCH_PAGE
import com.zj.mvi.core.observeEvent
import com.zj.mvi.core.observeState
import kotlinx.coroutines.launch
import me.onebone.toolbar.CollapsingToolbarScaffold
import me.onebone.toolbar.ExperimentalToolbarApi
import me.onebone.toolbar.ScrollStrategy
import me.onebone.toolbar.rememberCollapsingToolbarScaffoldState

@OptIn(ExperimentalToolbarApi::class, ExperimentalComposeUiApi::class)
@Composable
fun SearchMainPage(
    navCtrl: NavController,
    scaffoldState: ScaffoldState,
    viewModel: SearchMainViewModel = viewModel()
) {
    val coroutineState = rememberCoroutineScope()
    val collapsingState = rememberCollapsingToolbarScaffoldState()
    val toolbarState by remember { derivedStateOf { collapsingState.toolbarState } }
    val viewStates = viewModel.viewStates

    var isExpand by rememberSaveable { mutableStateOf(true) }
    var isScrolling by remember { mutableStateOf(false) }
    var isChange by remember { mutableStateOf(false) }

    var isFirstInit by rememberSaveable { mutableStateOf(true) }
    var loading by remember { mutableStateOf(false) }

    val listSaver = listSaver<MutableList<PokemonSearchBean>, PokemonSearchBean>(
        save = { it.toList() },
        restore = { it.toMutableList() }
    )
    val dataList = rememberSaveable(saver = listSaver) { mutableStateListOf() }
    val lifecycleOwner = LocalLifecycleOwner.current

    val minHeight = with(LocalDensity.current) { 80.dp.roundToPx().toFloat() }
    val maxHeight = with(LocalDensity.current) { 180.dp.roundToPx().toFloat() }

    rememberSystemUiController().setStatusBarColor(
        PokeBallRed,
        darkIcons = MaterialTheme.colors.isLight
    )
    LaunchedEffect(toolbarState.height) {
        isChange = true
    }
    LaunchedEffect(Unit) {
        isChange = false
        viewModel.viewEvents.observeEvent(lifecycleOwner) {
            when (it) {
                is SearchMainViewEvent.ShowLoadingDialog -> loading = true
                is SearchMainViewEvent.DismissLoadingDialog -> loading = false
                is SearchMainViewEvent.ShowToast ->
                    coroutineState.launch {
                        scaffoldState.snackbarHostState.showSnackbar(message = it.msg)
                    }
            }
        }
        viewStates.let { states ->
            states.observeState(lifecycleOwner, SearchMainViewState::pokemonItemUpdate) {
                // 防止重复添加
                if (!dataList.containsAll(it))
                    dataList.addAll(it)
            }
        }
        if (isFirstInit) {
            viewModel.dispatch(SearchMainViewAction.GetData)
            isFirstInit = false
        }
    }

    CollapsingToolbarScaffold(
        state = collapsingState,
        modifier = Modifier.fillMaxSize()
            .motionEventSpy {
                Logger.d("motion: ${it.action}")
                if (
                    it.action == MotionEvent.ACTION_UP ||
                    it.action == MotionEvent.ACTION_CANCEL
                ) {
                    isChange = false
                    coroutineState.launch {
                        isScrolling = false
                        isExpand = if (toolbarState.height > maxHeight * 3 / 4) {
                            toolbarState.scroll {
                                scrollBy(maxHeight - toolbarState.height)
                            }
                            true
                        } else {
                            toolbarState.scroll {
                                scrollBy(minHeight - toolbarState.height)
                            }
                            false
                        }
                    }
                    Logger.d("isExpand: $isExpand")
                } else if (it.action == MotionEvent.ACTION_MOVE) {
                    isScrolling = true
                    Logger.d("is ")
                }
            },
        scrollStrategy = ScrollStrategy.ExitUntilCollapsed,
        toolbar = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp)
                    .pin()
                    .clip(RoundedCornerShape(bottomEnd = 15.dp, bottomStart = 15.dp))
                    .background(color = PokeBallRed)
            )
            if (isScrolling)
                Box(modifier = Modifier.height(80.dp).background(Color.Transparent))
            if (!isChange || !isScrolling) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(
                            top = 10.dp,
                            bottom = 10.dp,
                            start = if (isExpand) 0.dp else 10.dp
                        ),
                    contentAlignment = if (isExpand) Alignment.Center else Alignment.CenterStart
                ) {
                    Image(
                        modifier = Modifier.size(
                            width = if (isExpand) 190.dp else 110.dp,
                            height = if (isExpand) 95.dp else 60.dp
                        ),
                        painter = painterResource(R.drawable.logo),
                        contentDescription = "logo",
                        contentScale = ContentScale.Fit
                    )
                }

                Box(
                    modifier = Modifier
                        .offset(y = if (isExpand) 100.dp else 0.dp)
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp, vertical = 10.dp),
                    contentAlignment = if (isExpand) Alignment.Center else Alignment.CenterEnd
                ) {
                    Box(
                        modifier = Modifier
                            .height(if (isExpand) 70.dp else 60.dp)
                            .width(if (isExpand) 288.dp else 242.dp)
                    ) {
                        PokeBallSearchBar(
                            value = "",
                            onValueChange = {},
                            onClick = {
                                navCtrl.navigate(SEARCH_PAGE)
                            }
                        )
                    }
                }
            }
        }
    ) {
        LazyLoadMoreColumn(
            loadState = loading,
            onLoad = { viewModel.dispatch(SearchMainViewAction.GetData) }
        ) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth(),
                contentPadding = PaddingValues(vertical = 10.dp, horizontal = 5.dp),
                verticalArrangement = Arrangement.SpaceEvenly,
                state = rememberLazyListState()
            ) {
                items(items = dataList) {
                    PokemonSearchCard(item = it) {
                        navCtrl.navigate("$DETAIL_PAGE/${it.pokemon_id}")
                    }
                    Spacer(Modifier.height(10.dp))
                }
                if (dataList.isEmpty() && !loading)
                    item {
                        Text(
                            text = "暂无相关搜索结果~",
                            fontSize = 18.sp,
                            modifier = Modifier
                                .align(Alignment.Center)
                                .fillMaxSize()
                                .padding(vertical = 200.dp),
                            textAlign = TextAlign.Center,
                            color = Color.Gray
                        )
                    }

            }
        }
        if (loading)
            WarpLoadingDialog(text = "正在加载", backgroundAlpha = 0.1f)
    }
}