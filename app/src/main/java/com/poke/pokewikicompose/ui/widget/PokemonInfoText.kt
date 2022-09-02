package com.poke.pokewikicompose.ui.widget

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.poke.pokewikicompose.ui.theme.Grass

@Composable
fun PokemonInfoText(
    modifier: Modifier = Modifier,
    color: Color,
    topText: String? = null,
    contentText: String,
    bottomText: String? = null
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier.height(60.dp),
        verticalArrangement = Arrangement.SpaceEvenly
    ) {
        if (!topText.isNullOrBlank()) {
            Text(
                text = topText,
                color = Color(0xFF9E9E9E),
                fontSize = 10.sp
            )
        }
        Text(
            text = contentText,
            color = color,
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold
        )
        if (!bottomText.isNullOrBlank()) {
            Text(
                text = bottomText,
                color = Color(0xFF9E9E9E),
                fontSize = 10.sp
            )
        }
    }
}

@Composable
@Preview
private fun PokemonInfoTextPreview() {
    PokemonInfoText(
        color = Grass,
        topText = "类别",
        contentText = "种子宝可梦",
        bottomText = "隐藏特性"
    )
}