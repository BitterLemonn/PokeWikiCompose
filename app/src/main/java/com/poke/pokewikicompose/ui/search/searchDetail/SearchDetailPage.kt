package com.poke.pokewikicompose.ui.search.searchDetail

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Card
import androidx.compose.material.Scaffold
import androidx.compose.material.ScaffoldState
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.poke.pokewikicompose.R
import com.poke.pokewikicompose.dataBase.data.bean.PokemonSearchBean
import com.poke.pokewikicompose.ui.SNACK_ERROR
import com.poke.pokewikicompose.ui.popupSnackBar
import com.poke.pokewikicompose.ui.theme.PokeBallRed
import com.poke.pokewikicompose.ui.widget.PokeBallSearchBar
import com.poke.pokewikicompose.ui.widget.PokemonSearchDetailCard
import com.poke.pokewikicompose.ui.widget.WarpLoadingDialog
import com.poke.pokewikicompose.utils.AppContext
import com.poke.pokewikicompose.utils.DETAIL_PAGE
import com.poke.pokewikicompose.utils.PokemonSearchMode
import com.zj.mvi.core.observeEvent
import com.zj.mvi.core.observeState

@Composable
fun SearchDetailPage(
    navCtrl: NavController,
    scaffoldState: ScaffoldState,
    key: String,
    mode: PokemonSearchMode,
    viewModel: SearchDetailViewModel = viewModel()
) {
    val viewStates = viewModel.viewStates
    val lifecycleOwner = LocalLifecycleOwner.current
    val coroutine = rememberCoroutineScope()

    var isShowLoading by remember { mutableStateOf(false) }
    val searchHistory = remember { mutableStateListOf<String>() }
    var searchKey by remember { mutableStateOf(key) }
    var searchMode by remember { mutableStateOf(mode) }
    val searchResult = remember { mutableStateListOf<PokemonSearchBean>() }

    LaunchedEffect(Unit) {
        searchHistory.addAll(AppContext.searchHistory)
        viewStates.observeState(lifecycleOwner, SearchDetailViewStates::searchResult) {
            searchResult.clear()
            searchResult.addAll(it)
        }
        viewModel.viewEvents.observeEvent(lifecycleOwner) {
            when (it) {
                is SearchDetailViewEvents.ShowToast -> popupSnackBar(
                    coroutine,
                    scaffoldState,
                    SNACK_ERROR,
                    it.msg
                )
                is SearchDetailViewEvents.ShowLoadingDialog -> isShowLoading = true
                is SearchDetailViewEvents.DismissLoadingDialog -> isShowLoading = false
            }
        }
        viewModel.dispatch(SearchDetailViewAction.GetSearch)
    }
    LaunchedEffect(searchKey) {
        if (searchKey.isNotEmpty())
            viewModel.dispatch(SearchDetailViewAction.UpdateSearchKey(searchKey))
    }
    LaunchedEffect(searchMode) {
        viewModel.dispatch(SearchDetailViewAction.UpdateSearchMode(searchMode))
    }
    LaunchedEffect(searchHistory) {
        AppContext.searchHistory.clear()
        AppContext.searchHistory.addAll(searchHistory)
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        scaffoldState = scaffoldState,
        topBar = {
            Card(
                backgroundColor = PokeBallRed,
                elevation = 5.dp,
                shape = RectangleShape
            ) {
                Row(
                    modifier = Modifier.padding(10.dp),
                    horizontalArrangement = Arrangement.SpaceAround,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Image(
                        painter = painterResource(R.drawable.white_back),
                        contentDescription = "back button",
                        modifier = Modifier
                            .padding(10.dp)
                            .size(33.dp)
                            .clip(CircleShape)
                            .clickable {
                                navCtrl.popBackStack()
                            }
                    )
                    Spacer(Modifier.width(20.dp))
                    PokeBallSearchBar(
                        value = searchKey,
                        onValueChange = {
                            searchKey = if (it.length > 6) it.substring(0, 6) else it
                        },
                        onSearch = {
                            if (searchKey.isNotEmpty()) {
                                searchMode = PokemonSearchMode.NAME
                                searchHistory.add(searchKey)
                            }
                        }
                    )
                }
            }
        }
    ) { paddingValues ->
        Box(modifier = Modifier.padding(paddingValues).fillMaxSize()) {
            if (searchResult.size > 0)
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(10.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalArrangement = Arrangement.SpaceEvenly
                ) {
                    items(searchResult) {
                        PokemonSearchDetailCard(it) {
                            navCtrl.navigate("$DETAIL_PAGE/${it.pokemon_id}")
                        }
                    }
                }
        }
    }
    if (isShowLoading)
        WarpLoadingDialog(text = "正在搜索")
}

@Composable
@Preview
fun SearchDetailPagePreview() {
    SearchDetailPage(
        rememberNavController(),
        rememberScaffoldState(),
        "",
        PokemonSearchMode.NAME
    )
}