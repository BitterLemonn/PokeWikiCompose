package com.poke.pokewikicompose.ui.widget

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.TweenSpec
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.poke.pokewikicompose.ui.theme.*

@Composable
fun PokeStateBar(
    color: Color,
    text: String,
    num: Int
) {
    val len = remember { mutableStateOf(0) }
    LaunchedEffect(num) {
        len.value = num
    }
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = text,
            color = Color.Black,
            fontSize = 14.sp,
            modifier = Modifier.width(35.dp),
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.width(5.dp))
        Box(modifier = Modifier.height(40.dp)) {
            Text(
                text = num.toString(),
                color = Color(0xFF7E7E7E),
                fontSize = 10.sp,
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(end = 5.dp)
            )
            Box(
                modifier = Modifier
                    .width(190.dp)
                    .height(12.dp)
                    .clip(shape = RoundedCornerShape(100.dp))
                    .border(width = 1.dp, color = Color.Black, shape = RoundedCornerShape(100.dp))
                    .background(color = Color(0xFFEFEFEF))
                    .align(Alignment.Center)
            ) {
                Box(
                    modifier = Modifier
                        .clip(shape = RoundedCornerShape(100.dp))
                        .animateContentSize(
                            animationSpec = TweenSpec(
                                durationMillis = 400,
                                easing = LinearEasing
                            )
                        )
                        .width((190.0 / 255.0 * len.value).dp)
                        .height(12.dp)
                        .background(color = color)
                )
            }
        }
    }

}

@Composable
@Preview
private fun PokeStateBarPreview() {
    Column(
        verticalArrangement = Arrangement.SpaceEvenly
    ) {
        PokeStateBar(
            color = HP,
            text = "HP",
            num = 45
        )
        PokeStateBar(
            color = ATK,
            text = "ATK",
            num = 49
        )
        PokeStateBar(
            color = DEF,
            text = "DEF",
            num = 49
        )
        PokeStateBar(
            color = SpicATK,
            text = "SATK",
            num = 65
        )
        PokeStateBar(
            color = SpicDEF,
            text = "SDEF",
            num = 65
        )
    }
}