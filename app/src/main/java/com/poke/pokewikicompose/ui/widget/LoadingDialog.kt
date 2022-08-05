package com.poke.pokewikicompose.ui.widget

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
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
private fun LoadingDialog(
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
fun WarpLoadingDialog(
    text: String,
    size: Int = 120,
    alpha: Float = 0.4f
) {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colors.onSurface.copy(alpha = alpha)
    ) {
        Box(modifier = Modifier.wrapContentSize()) {
            LoadingDialog("$text...", size)
        }
    }
}

@Composable
@Preview
private fun LoadingPreview() {
    WarpLoadingDialog("正在登录", 120)
}