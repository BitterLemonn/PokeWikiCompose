package com.poke.pokewikicompose.ui.main

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ScaffoldState
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import com.poke.pokewikicompose.R
import kotlinx.coroutines.launch

@OptIn(ExperimentalPagerApi::class)
@Composable
fun MainPage(
    mainPageList: List<@Composable () -> Unit>?,
    navCtrl: NavController?,
    scaffoldState: ScaffoldState?,
) {
    val selectedIndex = remember { mutableStateOf(0) }
    val coroutineScope = rememberCoroutineScope()
    val pageState = rememberPagerState()
    LaunchedEffect(Unit) {
        selectedIndex.value = 1
        pageState.scrollToPage(1)
    }
    Box(
        modifier = Modifier
            .fillMaxSize(),
    ) {
        Column {
            HorizontalPager(
                verticalAlignment = Alignment.Top,
                count = 3,
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                state = pageState
            ) { page ->
                selectedIndex.value = page
                mainPageList?.let {
                    mainPageList[page].invoke()
                }
            }
            Box(
                modifier = Modifier
                    .background(Color.Transparent)
                    .fillMaxWidth()
                    .border(
                        width = 1.dp,
                        color = Color.Black,
                        shape = RoundedCornerShape(
                            topStart = 16.dp,
                            topEnd = 16.dp
                        )
                    )
                    .height(45.dp)
                    .clip(
                        RoundedCornerShape(
                            topStart = 16.dp,
                            topEnd = 16.dp
                        )
                    )
                    .background(Color.White)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.Center),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    Box(modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .clickable {
                            if (pageState.currentPage != 0)
                                coroutineScope.launch {
                                    pageState.scrollToPage(0)
                                }
                        }) {
                        Image(
                            painter = painterResource(R.drawable.about),
                            contentDescription = "about us",
                            modifier = Modifier
                                .size(22.dp)
                                .align(Alignment.Center)
                        )
                    }
                    Spacer(modifier = Modifier.width(15.dp))
                    Box(modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .clickable {
                            if (pageState.currentPage != 2)
                                coroutineScope.launch {
                                    pageState.scrollToPage(2)
                                }
                        }
                    ) {
                        Image(
                            painter = painterResource(R.drawable.my),
                            contentDescription = "my profile",
                            modifier = Modifier
                                .size(22.dp)
                                .align(Alignment.Center)
                        )
                    }
                }
            }

        }
        Image(
            painter = painterResource(R.drawable.search_btn),
            contentDescription = "home button",
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 7.dp)
                .size(55.dp)
                .clip(CircleShape)
                .clickable {
                    if (pageState.currentPage != 1)
                        coroutineScope.launch {
                            selectedIndex.value = 1
                            pageState.scrollToPage(1, 0f)
                        }
                }
        )

    }
}

@Composable
@Preview
private fun MainPagePreview() {
    MainPage(null, null, null)
}