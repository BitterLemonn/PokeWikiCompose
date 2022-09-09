package com.poke.pokewikicompose.ui.feedback

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.poke.pokewikicompose.R
import com.poke.pokewikicompose.ui.widget.BottomButtonItem
import com.poke.pokewikicompose.ui.widget.BottomDialog
import com.poke.pokewikicompose.ui.widget.TitleBar
import github.leavesczy.matisse.Matisse
import github.leavesczy.matisse.MatisseContract

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun FeedbackPage(
    navCtrl: NavController,
    scaffoldState: ScaffoldState
) {
    val text = remember { mutableStateOf("") }
    val contentPhoto = remember { mutableStateListOf<String>() }

    val bottomButton = remember { mutableStateListOf<BottomButtonItem>() }
    val isShowBottomDialog = remember { mutableStateOf(false) }
    val launcher = rememberLauncherForActivityResult(MatisseContract()) {
        if (it.isNotEmpty()) {
            for (mediaRes in it) {
                contentPhoto.add(mediaRes.path)
            }
        }
    }
    LaunchedEffect(Unit) {
        bottomButton.add(BottomButtonItem(text = "拍照") {})
        bottomButton.add(BottomButtonItem(text = "从相册中选择") {
            launcher.launch(Matisse(maxSelectable = 3))
            isShowBottomDialog.value = false
        })
    }
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TitleBar(
                title = "意见反馈",
                onBackClick = { navCtrl.popBackStack() },
                rightIcon = R.drawable.send,
                onClickRight = {}
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier.padding(paddingValues)
        ) {
            Card(
                modifier = Modifier
                    .padding(top = 18.dp, start = 16.dp, end = 16.dp)
                    .height(170.dp)
                    .fillMaxWidth(),
                elevation = 5.dp,
                shape = RoundedCornerShape(5.dp),
                backgroundColor = Color.White
            ) {
                TextField(
                    value = text.value,
                    onValueChange = {
                        text.value = it
                    },
                    placeholder = {
                        Text(
                            text = "请输入反馈内容~",
                            fontSize = 14.sp,
                            color = Color(0x8F000000)
                        )
                    },
                    modifier = Modifier.fillMaxSize(),
                    colors = TextFieldDefaults.textFieldColors(
                        backgroundColor = Color.Transparent,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent
                    )
                )
            }
            Spacer(modifier = Modifier.height(20.dp))
            LazyRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (contentPhoto.size < 3)
                    item {
                        Card(
                            modifier = Modifier.padding(start = 5.dp).size(100.dp),
                            shape = RoundedCornerShape(10.dp),
                            elevation = 5.dp,
                            backgroundColor = Color.White,
                            onClick = { isShowBottomDialog.value = true }
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxSize(),
                                verticalArrangement = Arrangement.SpaceEvenly,
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Image(
                                    painter = painterResource(R.drawable.add_picture),
                                    contentDescription = "add picture",
                                    modifier = Modifier.size(30.dp)
                                )
                                Text(
                                    text = "添加照片",
                                    fontSize = 14.sp,
                                    color = Color(0xFF585858)
                                )
                            }
                        }
                    }
            }
        }

        BottomDialog(
            items = bottomButton,
            isShow = isShowBottomDialog
        )
    }
}

@Composable
@Preview
private fun FeedbackPagePreview() {
    FeedbackPage(rememberNavController(), rememberScaffoldState())
}