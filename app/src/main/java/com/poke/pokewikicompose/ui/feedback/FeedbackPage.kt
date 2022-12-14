package com.poke.pokewikicompose.ui.feedback

import android.net.Uri
import android.os.Environment
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.FileProvider
import androidx.core.net.toUri
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import com.poke.pokewikicompose.R
import com.poke.pokewikicompose.ui.widget.BottomButtonItem
import com.poke.pokewikicompose.ui.widget.BottomDialog
import com.poke.pokewikicompose.ui.widget.TitleBar
import github.leavesczy.matisse.Matisse
import github.leavesczy.matisse.MatisseContract
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.io.File

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun FeedbackPage(
    navCtrl: NavController,
    scaffoldState: ScaffoldState
) {
    val context = LocalContext.current
    val coroutine = rememberCoroutineScope()
    var text by remember { mutableStateOf("") }
    val contentPhoto = remember { mutableStateListOf<Uri>() }
    var pictureURI by remember { mutableStateOf(File("").toUri()) }
    var showDel by remember { mutableStateOf(-1) }

    val bottomButton = remember { mutableStateListOf<BottomButtonItem>() }
    val isShowBottomDialog = remember { mutableStateOf(false) }
    val albumLauncher = rememberLauncherForActivityResult(MatisseContract()) {
        if (it.isNotEmpty()) {
            for (mediaRes in it) {
                contentPhoto.add(mediaRes.uri)
            }
        }
    }
    val cameraLauncher = rememberLauncherForActivityResult(ActivityResultContracts.TakePicture()) {
        if (it)
            contentPhoto.add(pictureURI)
    }
    LaunchedEffect(Unit) {
        val file =
            File("${context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)}/feedbackPic.png")
        pictureURI =
            FileProvider.getUriForFile(context, "com.poke.pokewikicompose.fileprovider", file)

        bottomButton.add(BottomButtonItem(text = "??????") {
            cameraLauncher.launch(pictureURI)
            isShowBottomDialog.value = false
        })
        bottomButton.add(BottomButtonItem(text = "??????????????????") {
            albumLauncher.launch(Matisse(maxSelectable = 3))
            isShowBottomDialog.value = false
        })
    }
    LaunchedEffect(showDel) {
        coroutine.launch {
            delay(2_000)
            showDel = -1
        }
    }
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TitleBar(
                title = "????????????",
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
                    value = text,
                    onValueChange = {
                        text = it
                    },
                    placeholder = {
                        Text(
                            text = "?????????????????????~",
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
                items(contentPhoto) {
                    Card(
                        modifier = Modifier
                            .size(100.dp),
                        shape = RoundedCornerShape(10.dp),
                        elevation = 5.dp,
                        backgroundColor = Color.White
                    ) {
                        AsyncImage(
                            model = it,
                            contentDescription = "feedback picture",
                            modifier = Modifier
                                .size(100.dp)
                                .clip(RoundedCornerShape(10.dp))
                                .clickable { showDel = contentPhoto.indexOf(it) },
                            contentScale = ContentScale.Crop
                        )
                        AnimatedVisibility(
                            visible = showDel == contentPhoto.indexOf(it),
                            enter = fadeIn(),
                            exit = fadeOut()
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.pic_delete),
                                contentDescription = "",
                                modifier = Modifier
                                    .alpha(0.8f)
                                    .clickable { contentPhoto.remove(it) }
                                    .fillMaxSize()
                            )
                        }
                    }
                    Spacer(modifier = Modifier.width(20.dp))
                }
                if (contentPhoto.size < 3)
                    item {
                        Card(
                            modifier = Modifier
                                .padding(start = 5.dp)
                                .size(100.dp),
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
                                    text = "????????????",
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