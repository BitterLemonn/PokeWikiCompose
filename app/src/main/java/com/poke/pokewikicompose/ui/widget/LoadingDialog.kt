package com.poke.pokewikicompose.ui.widget

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.inset
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.decode.GifDecoder
import coil.request.ImageRequest
import com.poke.pokewikicompose.R

@Composable
fun LoadingDialog(
    text: String,
    dialogSize: Int
) {
    Canvas(modifier = Modifier.size(dialogSize.dp)) {
        inset {
            drawRoundRect(
                color = Color.White,
                size = size,
                cornerRadius = CornerRadius(20.0f),
                alpha = 0.5f, // 透明度
            )
        }
    }
    Column(
        verticalArrangement = Arrangement.SpaceEvenly,
        modifier = Modifier
            .size(dialogSize.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(R.drawable.pokeball_loading)
                .decoderFactory(GifDecoder.Factory())
                .build(),
            contentDescription = "loading picture",
            modifier = Modifier.size(90.dp)
        )
        Text(
            text = text,
            fontSize = 14.sp,
            color = Color.Gray
        )
    }
}

@Composable
@Preview
fun LoadingPreview() {
    LoadingDialog("正在登录...", 120)
}