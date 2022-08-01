package com.poke.pokewikicompose.ui.cover

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.poke.pokewikicompose.R
import com.poke.pokewikicompose.utils.COVER_PAGE
import com.poke.pokewikicompose.utils.LOGIN_PAGE
import kotlinx.coroutines.delay

@Composable
fun CoverPage(
    navController: NavController,
    skipType: String = "Cover"
) {
    var second by remember { mutableStateOf(3) }
    // 计时器
    LaunchedEffect(Unit) {
        while (second != 0) {
            delay(1000)
            second -= 1
        }
        onSkipClick(skipType = skipType, navController = navController)
    }

    Column(
        modifier = Modifier
            .background(color = Color.Black)
    ) {
        Button(
            border = BorderStroke(1.dp, Color.White),
            shape = RoundedCornerShape(10.dp),
            modifier = Modifier
                .align(Alignment.End)
                .padding(end = 10.dp, top = 10.dp),
            onClick = {
                onSkipClick(skipType = skipType, navController = navController)
            },
            colors = ButtonDefaults.buttonColors(
                backgroundColor = Color.Transparent,
                contentColor = MaterialTheme.colors.surface
            ),
        ) {
            Text(
                text = if (second != 0) "跳过 ${second}s" else "跳过",
                fontSize = 18.sp
            )
        }
        Column(
            modifier = Modifier
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            Image(
                painter = painterResource(id = R.drawable.cover),
                contentDescription = "cover",
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.6f)
            )
            Image(
                painter = painterResource(id = R.drawable.logo),
                contentDescription = "logo",
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.2f)
            )
        }
    }
}

fun onSkipClick(navController: NavController, skipType: String) {
    if (skipType == "Cover")
        navController.navigate(route = LOGIN_PAGE) {
            popUpTo(COVER_PAGE) { inclusive = true }
        }
}