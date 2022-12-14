package com.poke.pokewikicompose.ui.search

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.lt.compose_views.flow_layout.FlowLayout
import com.poke.pokewikicompose.R
import com.poke.pokewikicompose.dataBase.GlobalDataBase
import com.poke.pokewikicompose.ui.theme.PokeBallRed
import com.poke.pokewikicompose.ui.widget.PokeBallSearchBar
import com.poke.pokewikicompose.ui.widget.PokemonTag
import com.poke.pokewikicompose.utils.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun SearchPage(
    navCtrl: NavController,
    scaffoldState: ScaffoldState,
) {
    val localInfo = GlobalDataBase.database.localSettingDao()
    val localSetting = AppContext.localSetting

    val coroutine = rememberCoroutineScope()

    var searchKey by remember { mutableStateOf("") }
    var searchMode by remember { mutableStateOf(PokemonSearchMode.NAME) }
    var isClearHistory by remember { mutableStateOf(false) }

    val searchHistory = remember { mutableStateListOf<String>() }

    LaunchedEffect(Unit) {
        coroutine.launch(Dispatchers.IO) {
            val localHistory = localInfo.getLocalSettingWithUserID(AppContext.userData.userId)!!
                .searchHistory
            searchHistory.addAll(localHistory)
        }
    }

    LaunchedEffect(isClearHistory) {
        isClearHistory = false
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
                                if (searchKey in searchHistory)
                                    searchHistory.remove(searchKey)
                                searchHistory.add(0, searchKey)
                                coroutine.launch(Dispatchers.IO) {
                                    localInfo.updateLocalSetting(
                                        setting = localSetting.copy(searchHistory = searchHistory.toList())
                                    )
                                }
                                navCtrl.navigate("$SEARCH_DETAIL_PAGE/$searchKey/$searchMode")
                            }
                        }
                    )
                }
            }
        }
    ) { paddingValues ->
        Box(modifier = Modifier.fillMaxSize().padding(paddingValues)) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(vertical = 10.dp, horizontal = 20.dp)
            ) {
                if (searchHistory.size > 0 && !isClearHistory) {
                    // ????????????
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.Start,
                        verticalArrangement = Arrangement.Top
                    ) {
                        // ??????????????????
                        Box(modifier = Modifier.fillMaxWidth()) {
                            Text(
                                text = "????????????:",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                textAlign = TextAlign.Center,
                                modifier = Modifier.align(Alignment.CenterStart)
                            )
                            Box(
                                modifier = Modifier.size(40.dp).align(Alignment.CenterEnd),
                                contentAlignment = Alignment.CenterEnd
                            ) {
                                Image(
                                    painter = painterResource(R.drawable.black_delete),
                                    contentDescription = "clear search history",
                                    modifier = Modifier
                                        .size(20.dp)
                                        .clip(CircleShape)
                                        .clickable {
                                            searchHistory.clear()
                                            coroutine.launch(Dispatchers.IO) {
                                                localInfo.updateLocalSetting(
                                                    setting = localSetting.copy(searchHistory = searchHistory.toList())
                                                )
                                            }
                                            isClearHistory = true
                                        }
                                )
                            }
                        }
                        // ??????????????????
                        FlowLayout(
                            maxLines = 3,
                            horizontalMargin = 10.dp,
                            verticalMargin = 10.dp
                        ) {
                            for (item in searchHistory) {
                                PokemonTag(
                                    text = item,
                                    isColored = true,
                                    fontSize = 16,
                                    tagWidth = 60
                                ) {
                                    searchKey = item
                                    searchMode = PokemonSearchMode.NAME
                                    if (searchKey in searchHistory)
                                        searchHistory.remove(searchKey)
                                    searchHistory.add(0, searchKey)
                                    coroutine.launch(Dispatchers.IO) {
                                        localInfo.updateLocalSetting(
                                            setting = localSetting.copy(searchHistory = searchHistory.toList())
                                        )
                                    }
                                    navCtrl.navigate("$SEARCH_DETAIL_PAGE/$searchKey/$searchMode")
                                }
                            }
                        }
                    }
                    Spacer(Modifier.height(50.dp))
                }
                // ??????
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.Start,
                    verticalArrangement = Arrangement.Top
                ) {
                    Text(
                        text = "??????:",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Start,
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    FlowLayout(
                        horizontalMargin = 10.dp,
                        verticalMargin = 10.dp
                    ) {
                        for (item in PokemonTypeList) {
                            PokemonTag(
                                text = item,
                                fontSize = 16,
                                isColored = true,
                                tagWidth = 65
                            ) {
                                searchKey = item
                                searchMode = PokemonSearchMode.TYPE
                                navCtrl.navigate("$SEARCH_DETAIL_PAGE/$searchKey/$searchMode")
                                if (item in searchHistory)
                                    searchHistory.remove(item)
                                searchHistory.add(0, item)
                                coroutine.launch(Dispatchers.IO) {
                                    localInfo.updateLocalSetting(
                                        setting = localSetting.copy(searchHistory = searchHistory.toList())
                                    )
                                }
                            }
                        }
                    }
                }
                Spacer(Modifier.height(50.dp))
                // ??????
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.Start,
                    verticalArrangement = Arrangement.Top
                ) {
                    Text(
                        text = "??????:",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Start,
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    FlowLayout(
                        horizontalMargin = 10.dp,
                        verticalMargin = 10.dp
                    ) {
                        for (item in PokemonGenList) {
                            PokemonTag(
                                text = item,
                                fontSize = 16,
                                isColored = true
                            ) {
                                searchKey = item
                                searchMode = PokemonSearchMode.GEN
                                navCtrl.navigate("$SEARCH_DETAIL_PAGE/$searchKey/$searchMode")
                                if (item in searchHistory)
                                    searchHistory.remove(item)
                                searchHistory.add(0, item)
                                coroutine.launch(Dispatchers.IO) {
                                    localInfo.updateLocalSetting(
                                        setting = localSetting.copy(searchHistory = searchHistory.toList())
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
@Preview
fun SearchPagePreview() {
    SearchPage(rememberNavController(), rememberScaffoldState())
}