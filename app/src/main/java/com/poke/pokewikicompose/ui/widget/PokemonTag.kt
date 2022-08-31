package com.poke.pokewikicompose.ui.widget

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.poke.pokewikicompose.utils.GetColorByText
import kotlin.math.max

@Composable
fun PokemonTag(
    text: String = "#001",
    fontSize: Int = 11,
    tagWidth: Int = 40,
    isColored: Boolean = false
) {
    val color =
        if (!isColored) Color.Transparent
        else GetColorByText(text)
    val size = if (isColored) max(fontSize * text.length + 15, tagWidth) else tagWidth
    Surface(
        modifier = Modifier
            .size(width = size.dp, height = tagWidth.dp / 3 * 2),
        shape = RoundedCornerShape(100.dp),
        color = color,
        border = if (!isColored) BorderStroke(1.dp, Color.Black) else null,
    ) {
        Text(
            text = if (isColored) text else changeInt(text.toInt()),
            fontSize = fontSize.sp,
            color = if (!isColored) Color.Black else Color.White,
            textAlign = TextAlign.Center,
            modifier = Modifier.wrapContentSize()
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
private fun PokemonTagPreview(){
    PokemonTag()
}