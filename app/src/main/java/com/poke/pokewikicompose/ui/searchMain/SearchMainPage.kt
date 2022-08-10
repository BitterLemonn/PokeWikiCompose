package com.poke.pokewikicompose.ui.searchMain

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ScaffoldState
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.orhanobut.logger.Logger
import com.poke.pokewikicompose.R
import com.poke.pokewikicompose.dataBase.data.bean.PokemonSearchBean
import com.poke.pokewikicompose.ui.SNACK_ERROR
import com.poke.pokewikicompose.ui.popupSnackBar
import com.poke.pokewikicompose.ui.theme.PokeBallRed
import com.poke.pokewikicompose.ui.widget.LazyLoadMoreColumn
import com.poke.pokewikicompose.ui.widget.PokeBallSearchBar
import com.poke.pokewikicompose.ui.widget.PokemonSearchCard
import com.poke.pokewikicompose.ui.widget.WarpLoadingDialog
import me.onebone.toolbar.CollapsingToolbarScaffold
import me.onebone.toolbar.ExperimentalToolbarApi
import me.onebone.toolbar.ScrollStrategy
import me.onebone.toolbar.rememberCollapsingToolbarScaffoldState

val listState = LazyListState(0)

@OptIn(ExperimentalToolbarApi::class)
@Composable
fun SearchMainPage(
    navCtrl: NavController,
    scaffoldState: ScaffoldState,
    viewModel: SearchMainViewModel = viewModel()
) {
    val isFirstInit = rememberSaveable { mutableStateOf(true) }
    val viewStates = viewModel.viewStates
    val loading = remember { mutableStateOf(false) }
    val coroutineState = rememberCoroutineScope()
    val collapsingState = rememberCollapsingToolbarScaffoldState()

    val progress = collapsingState.toolbarState.progress
    val dataList = remember { mutableStateListOf<PokemonSearchBean>() }

    LaunchedEffect(Unit) {
        if (isFirstInit.value) {
            viewModel.dispatch(SearchMainViewAction.GetDataWithState(false))
            isFirstInit.value = false
        }
        viewModel.viewEvent.collect {
            when (it) {
                is SearchMainViewEvent.ShowLoadingDialog -> loading.value = true
                is SearchMainViewEvent.DismissLoadingDialog -> loading.value = false
                is SearchMainViewEvent.ShowToast -> popupSnackBar(
                    coroutineState,
                    scaffoldState,
                    label = SNACK_ERROR,
                    it.msg
                )
                is SearchMainViewEvent.UpdateDataList -> {
                    dataList.clear()
                    dataList.addAll(viewStates.pokemonItemList)
                }
            }
        }
    }
    CollapsingToolbarScaffold(
        modifier = Modifier.fillMaxSize(),
        state = collapsingState,
        scrollStrategy = ScrollStrategy.ExitUntilCollapsed,
        toolbar = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp)
                    .pin()
                    .background(
                        PokeBallRed,
                        shape = RoundedCornerShape(
                            bottomEnd = (15 * progress).dp,
                            bottomStart = (15 * progress).dp
                        )
                    )
            )
            Box(
                modifier = Modifier
                    .road(Alignment.CenterStart, Alignment.TopCenter)
                    .size(
                        width = (90 + (190 - 90) * progress +
                                (20 - 20 * progress)).dp,
                        height = (60 + (65 - 30) * progress).dp
                    )
                    .padding(
                        top = 15.dp,
                        bottom = 15.dp,
                        start = (20 - 20 * progress).dp
                    )
            ) {
                Image(
                    modifier = Modifier.fillMaxSize(),
                    painter = painterResource(R.drawable.logo),
                    contentDescription = "logo",
                    contentScale = ContentScale.Fit
                )
            }

            Box(
                modifier = Modifier
                    .road(Alignment.CenterEnd, Alignment.TopCenter)
                    .offset(y = (100 * progress).dp)
                    .padding(horizontal = 20.dp, vertical = 10.dp)
                    .height((60 + 10 * progress).dp)
                    .width((222 + (268 - 222) * progress + 20).dp)
            ) {
                PokeBallSearchBar(
                    value = "",  // TODO
                    onValueChange = {
                        // TODO
                    },
                    onClick = { Log.e("", "SearchMainPage: 点击了搜索") }
                )
            }

        }
    ) {
        if (loading.value)
            WarpLoadingDialog(text = "正在加载", alpha = 0.1f)
        LazyLoadMoreColumn(
            loadState = loading.value,
            onLoad = {
                viewModel.dispatch(SearchMainViewAction.GetDataWithState(false))
            }
        ) {
            LazyColumn(
                state = listState,
                modifier = Modifier.fillMaxWidth(),
                contentPadding = PaddingValues(vertical = 10.dp, horizontal = 5.dp),
                verticalArrangement = Arrangement.SpaceEvenly
            ) {
                if (!loading.value && dataList.size > 0) {
                    items(items = dataList) {
                        PokemonSearchCard(it)
                        Spacer(Modifier.height(10.dp))
                    }
                } else if (dataList.size == 0)
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

    }
}