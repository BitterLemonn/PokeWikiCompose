package com.poke.pokewikicompose.ui.main

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ScaffoldState
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
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
import com.example.pokewiki.bean.PokemonSearchBean
import com.poke.pokewikicompose.R
import com.poke.pokewikicompose.ui.theme.PokeBallRed
import com.poke.pokewikicompose.ui.widget.PokemonSearchCard
import com.poke.pokewikicompose.ui.widget.PokeBallSearchBar
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
    val coroutineState = rememberCoroutineScope()
    val searchItems = remember { ArrayList<PokemonSearchBean>() }

    val collapsingState = rememberCollapsingToolbarScaffoldState()
    val progress = collapsingState.toolbarState.progress

    LaunchedEffect(Unit) {
//        viewModel.dispatch(SearchMainViewAction.GetDataWithState(false))
        val typeArrayList = ArrayList<String>()
        typeArrayList.add("草")
        typeArrayList.add("毒")
        for (i in 0..100) {
            searchItems.add(
                PokemonSearchBean(
                    img_url = "https://cdn.jsdelivr.net/gh/PokeAPI/sprites@master/sprites/pokemon/3.png",
                    pokemon_name = "妙蛙花",
                    pokemon_id = "#003",
                    pokemon_type = typeArrayList
                )
            )
        }
    }

    CollapsingToolbarScaffold(
        modifier = Modifier,
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
                    onClick = { Log.e("", "SearchMainPage: 点击了搜索")}
                )
            }


        }
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxWidth(),
            contentPadding = PaddingValues(vertical = 10.dp, horizontal = 5.dp),
            verticalArrangement = Arrangement.SpaceEvenly
        ) {
            if (searchItems.size > 0)
                for (item in searchItems) {
                    item {
                        PokemonSearchCard(item)
                        Spacer(Modifier.height(10.dp))
                    }
                }
            else
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

@Preview
@Composable
private fun PreviewMain() {
    SearchMainPage(navCtrl = null, scaffoldState = null)
}