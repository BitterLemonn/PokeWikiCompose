package com.poke.pokewikicompose.ui.collection

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ScaffoldState
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.insets.ui.Scaffold
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.orhanobut.logger.Logger
import com.poke.pokewikicompose.dataBase.data.bean.PokemonSearchBean
import com.poke.pokewikicompose.ui.SNACK_ERROR
import com.poke.pokewikicompose.ui.popupSnackBar
import com.poke.pokewikicompose.ui.theme.PokeBallRed
import com.poke.pokewikicompose.ui.widget.PokemonCollectionCard
import com.poke.pokewikicompose.ui.widget.TitleBar
import com.poke.pokewikicompose.utils.DETAIL_PAGE
import com.poke.pokewikicompose.utils.ERROR
import com.poke.pokewikicompose.utils.INIT
import com.zj.mvi.core.observeState

@Composable
fun CollectionPage(
    scaffoldState: ScaffoldState,
    navCtrl: NavController,
    viewModel: CollectionViewModel = viewModel()
) {
    rememberSystemUiController().setStatusBarColor(
        PokeBallRed, darkIcons = MaterialTheme.colors.isLight
    )

    val collectionList = remember { mutableStateListOf<PokemonSearchBean>() }
    val viewStates = viewModel.viewStates
    val lifecycleOwner = LocalLifecycleOwner.current
    val coroutine = rememberCoroutineScope()
    LaunchedEffect(Unit) {
        viewStates.let { state ->
            state.observeState(lifecycleOwner, CollectionViewStates::collection) {
                collectionList.clear()
                collectionList.addAll(it)
                Logger.d(collectionList)
            }
            state.observeState(lifecycleOwner, CollectionViewStates::delState) {
                if (it != INIT) {
                    if (it == ERROR) {
                        popupSnackBar(
                            coroutine,
                            scaffoldState,
                            SNACK_ERROR,
                            "取消收藏失败"
                        )
                    }
                    viewModel.dispatch(CollectionViewActions.ResetState)
                }
            }
        }
        viewModel.dispatch(CollectionViewActions.GetCollection)
    }
    Scaffold(
        scaffoldState = scaffoldState,
        topBar = { TitleBar(title = "我的收藏", onBackClick = { navCtrl.popBackStack() }) }
    ) { paddingValues ->
        if (collectionList.size > 0)
            LazyColumn(
                modifier = Modifier.padding(
                    horizontal = 5.dp,
                    vertical = paddingValues.calculateTopPadding()
                ),
            ) {
                item { Spacer(modifier = Modifier.height(16.dp)) }
                items(collectionList) {
                    PokemonCollectionCard(
                        item = it,
                        onClickCard = { navCtrl.navigate("$DETAIL_PAGE/${it.pokemon_id}") },
                        onDelItem = {
                            collectionList.remove(it)
                            viewModel.dispatch(CollectionViewActions.DelCollection(it.pokemon_id.toInt()))
                        }
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                }
            }
    }
}

@Composable
@Preview
fun CollectionPagePreview() {
    CollectionPage(rememberScaffoldState(), rememberNavController())
}