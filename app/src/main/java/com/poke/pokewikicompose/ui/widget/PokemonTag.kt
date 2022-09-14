package com.poke.pokewikicompose.ui.widget

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.poke.pokewikicompose.utils.getColorByText

@Composable
fun PokemonTag(
    text: String = "1",
    fontSize: Int = 11,
    tagWidth: Int = 40,
    isColored: Boolean = false,
    onClick: (() -> Unit)? = null
) {
    val color =
        if (!isColored) Color.Transparent
        else getColorByText(text)
    Surface(
        modifier = Modifier
            .defaultMinSize(tagWidth.dp)
            .clip(RoundedCornerShape(100.dp))
            .clickable(enabled = onClick != null) {
                onClick?.invoke()
            },
        shape = RoundedCornerShape(100.dp),
        color = color,
        border = if (!isColored) BorderStroke(1.dp, Color.Black) else null,
    ) {
        Text(
            text = if (isColored) text else changeInt(text.toInt()),
            fontSize = fontSize.sp,
            color = if (!isColored) Color.Black else Color.White,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .padding(horizontal = 10.dp, vertical = 5.dp)
                .defaultMinSize(tagWidth.dp),
            maxLines = 1
        )
    }
}

fun changeInt(num: Int): String {
    return if (num < 10) "#00$num"
    else if (num < 100) "#0$num"
    else "#$num"
}

@Composable
@Preview
private fun PokemonTagPreview() {
    PokemonTag()
}