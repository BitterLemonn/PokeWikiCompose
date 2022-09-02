package com.poke.pokewikicompose.ui.detail.info

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.poke.pokewikicompose.dataBase.data.bean.PokemonIntroBean
import com.poke.pokewikicompose.ui.theme.Grass
import com.poke.pokewikicompose.ui.widget.EvolutionBar
import com.poke.pokewikicompose.ui.widget.PokemonInfoText

@Composable
fun InfoPage(
    typeColor: Color,
    introBean: PokemonIntroBean,
    evoIndex: Int = 0,
    onEvoItemClick: (Int) -> Unit = {}
) {
    val scrollState = rememberScrollState()
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .verticalScroll(scrollState),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        EvolutionBar(
            itemList = introBean.poke_evolution,
            nowIndex = evoIndex,
            onItemClick = { onEvoItemClick.invoke(it) }
        )
        Spacer(modifier = Modifier.height(10.dp))
        // 类别 与 栖息地
        LazyRow(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 25.dp)
                .height(60.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            item {
                PokemonInfoText(
                    color = typeColor,
                    topText = "类别",
                    contentText = introBean.genus
                )
            }
            introBean.habitat?.let {
                item {
                    Card(
                        modifier = Modifier.width(1.dp).fillMaxHeight().padding(vertical = 10.dp),
                        backgroundColor = typeColor,
                        shape = RoundedCornerShape(2.dp)
                    ) {}
                }
                item {
                    PokemonInfoText(
                        color = typeColor,
                        topText = "栖息地",
                        contentText = it
                    )
                }
            }
        }
        Spacer(modifier = Modifier.height(10.dp))
        // 特性 与 形状
        LazyRow(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 10.dp)
                .height(60.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            // 普通特性
            for (ability in introBean.general_abilities) {
                if (ability != introBean.general_abilities.first()) {
                    item {
                        Card(
                            shape = RoundedCornerShape(2.dp),
                            backgroundColor = typeColor,
                            modifier = Modifier
                                .width(1.dp)
                                .fillMaxHeight()
                                .padding(vertical = 10.dp),
                        ) {}
                    }
                }
                item {
                    PokemonInfoText(
                        color = typeColor,
                        contentText = ability,
                        bottomText = "特性"
                    )
                }
            }
            // 隐藏特性
            introBean.hidden_abilities?.let {
                for (ability in introBean.hidden_abilities) {
                    item {
                        Card(
                            shape = RoundedCornerShape(2.dp),
                            backgroundColor = typeColor,
                            modifier = Modifier
                                .width(1.dp)
                                .fillMaxHeight()
                                .padding(vertical = 10.dp),
                        ) {}
                    }
                    item {
                        PokemonInfoText(
                            color = typeColor,
                            contentText = ability,
                            bottomText = "隐藏特性"
                        )
                    }
                }
            }
            // 形状
            item {
                Card(
                    shape = RoundedCornerShape(2.dp),
                    backgroundColor = typeColor,
                    modifier = Modifier
                        .width(1.dp)
                        .fillMaxHeight()
                        .padding(vertical = 10.dp),
                ) {}
            }
            item {
                PokemonInfoText(
                    color = typeColor,
                    contentText = introBean.shape,
                    bottomText = "形状"
                )
            }
        }
        Spacer(modifier = Modifier.height(20.dp))
        introBean.intro_text?.let {
            Card(
                modifier = Modifier
                    .padding(horizontal = 30.dp, vertical = 10.dp)
                    .defaultMinSize(minWidth = 230.dp),
                backgroundColor = Color.White,
                elevation = 10.dp,
                shape = RoundedCornerShape(16.dp)
            ) {
                Text(
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp),
                    text = it.replace("\n", ""),
                    fontSize = 14.sp,
                    color = Color(0xFF7E7E7E)
                )
            }
        }
        Spacer(modifier = Modifier.height(30.dp))
    }
}

@Composable
@Preview
private fun InfoPagePreview() {
    InfoPage(Grass, PokemonIntroBean())
}