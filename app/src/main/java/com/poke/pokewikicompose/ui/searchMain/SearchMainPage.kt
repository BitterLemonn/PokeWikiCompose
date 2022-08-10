package com.poke.pokewikicompose.ui.searchMain

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ScaffoldState
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import com.poke.pokewikicompose.R
import com.poke.pokewikicompose.ui.theme.PokeBallRed
import com.poke.pokewikicompose.ui.widget.PokeBallSearchBar
import com.poke.pokewikicompose.ui.widget.PokemonSearchCard
import com.poke.pokewikicompose.ui.widget.WarpLoadingDialog
import com.poke.pokewikicompose.utils.LOADING
import me.onebone.toolbar.CollapsingToolbarScaffold
import me.onebone.toolbar.ExperimentalToolbarApi
import me.onebone.toolbar.ScrollStrategy
import me.onebone.toolbar.rememberCollapsingToolbarScaffoldState

@OptIn(ExperimentalToolbarApi::class)
@Composable
fun SearchMainPage(
    navCtrl: NavController?,
    scaffoldState: ScaffoldState?,
    viewModel: SearchMainViewModel = viewModel()
) {
    val viewStates = viewModel.viewStates
    val loading = remember { mutableStateOf(false) }

    val collapsingState = rememberCollapsingToolbarScaffoldState()
    val progress = collapsingState.toolbarState.progress

    LaunchedEffect(Unit) {
        viewModel.dispatch(SearchMainViewAction.GetDataWithStateTest)
        viewModel.viewEvent.collect {
            when (it) {
                is SearchMainViewEvent.ShowLoadingDialog -> loading.value = true
                is SearchMainViewEvent.DismissLoadingDialog -> loading.value = false
                else -> {}
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
        SwipeRefresh(
            modifier = Modifier.wrapContentSize(),
            state = rememberSwipeRefreshState(viewStates.loadingState == LOADING),
            onRefresh = { viewModel.dispatch(SearchMainViewAction.GetDataWithStateTest) }
        ) {
            LazyColumn(
                modifier = Modifier.fillMaxWidth(),
                contentPadding = PaddingValues(vertical = 10.dp, horizontal = 5.dp),
                verticalArrangement = Arrangement.SpaceEvenly
            ) {
                if (!loading.value) {
                    if (viewStates.pokemonItemList != null)
                        items(items = viewStates.pokemonItemList) {
                            PokemonSearchCard(it)
                            Spacer(Modifier.height(10.dp))
                        }
                    else if (!loading.value)
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
}

@Preview
@Composable
private fun PreviewMain() {
    SearchMainPage(navCtrl = null, scaffoldState = null)
}