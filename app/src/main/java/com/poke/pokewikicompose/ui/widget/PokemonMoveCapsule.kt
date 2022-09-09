package com.poke.pokewikicompose.ui.widget

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.poke.pokewikicompose.dataBase.data.bean.PokemonMoveBean
import com.poke.pokewikicompose.ui.theme.Electric
import com.poke.pokewikicompose.ui.theme.General
import com.poke.pokewikicompose.ui.theme.PokeBallRed
import com.poke.pokewikicompose.ui.theme.Water
import com.poke.pokewikicompose.utils.getColorByText

@Composable
fun PokemonMoveCapsule(
    item: PokemonMoveBean
) {
    Row(
        modifier = Modifier.fillMaxWidth().height(40.dp),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        // 等级 属性标签
        Column(
            modifier = Modifier
                .defaultMinSize(minWidth = 60.dp)
                .fillMaxHeight(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // 等级 属性标签
            Row(
                modifier = Modifier
                    .defaultMinSize(minWidth = 60.dp)
                    .height(16.dp)
                    .clip(RoundedCornerShape(100.dp))
                    .background(color = getColorByText(item.type_name)),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                Box(
                    modifier = Modifier
                        .defaultMinSize(minWidth = 30.dp)
                        .fillMaxHeight()
                        .clip(RoundedCornerShape(100.dp))
                        .background(Color(0xFF57F478))
                ) {
                    Text(
                        text = if (item.level != 0) "LV ${item.level}" else "其他",
                        color = Color.White,
                        fontSize = 8.sp,
                        modifier = Modifier
                            .align(Alignment.Center)
                            .padding(horizontal = 5.dp)
                    )
                }
                Spacer(Modifier.width(3.dp))
                Text(
                    text = item.type_name,
                    color = Color.White,
                    fontSize = 8.sp,
                    modifier = Modifier
                        .padding(end = 5.dp)
                        .defaultMinSize(20.dp),
                    textAlign = TextAlign.Center
                )
            }
            Spacer(modifier = Modifier.height(5.dp))
            Text(
                text = item.move_name,
                color = Color.Black,
                fontSize = 13.sp
            )
        }
        PokemonStateCapsule("威力", item.power)
        PokemonStateCapsule("PP", item.pp)
        PokemonStateCapsule("命中", item.accuracy)
        PokemonStateCapsule(item.damage_type)
    }
}

@Composable
private fun PokemonStateCapsule(
    type: String,
    num: Int? = null
) {
    val color = when (type) {
        "威力" -> PokeBallRed
        "PP" -> Electric
        "命中" -> Water
        "物理" -> PokeBallRed
        "变化" -> General
        "特殊" -> Color(0xFF2266CC)
        else -> Color.Transparent
    }
    Box(
        modifier = Modifier
            .fillMaxHeight()
            .aspectRatio(1f)
            .clip(RoundedCornerShape(8.dp))
            .border(width = 1.dp, color = color, shape = RoundedCornerShape(8.dp))
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .clip(RoundedCornerShape(8.dp))
                .background(color = color),
            verticalArrangement = Arrangement.SpaceEvenly,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = type,
                    color = Color.White,
                    fontSize = 10.sp,
                    textAlign = TextAlign.Center
                )
            }
            if (type == "威力" || type == "PP" || type == "命中")
                Box(
                    modifier = Modifier
                        .background(Color.White)
                        .fillMaxWidth()
                        .clip(
                            RoundedCornerShape(
                                bottomEnd = 8.dp,
                                bottomStart = 8.dp
                            )
                        )
                        .weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = if (num == 0) "——" else num.toString(),
                        textAlign = TextAlign.Center,
                        fontSize = 10.sp,
                        color = Color.Black
                    )
                }
        }
    }
}

@Composable
@Preview
private fun PokemonMoveCapsulePreview() {
    PokemonMoveCapsule(
        PokemonMoveBean(
            type_name = "草",
            move_name = "撞击",
            damage_type = "物理"
        )
    )
//    PokemonStateCapsule("PP", 45)
}