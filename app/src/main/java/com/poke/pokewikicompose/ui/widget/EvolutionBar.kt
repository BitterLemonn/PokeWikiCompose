package com.poke.pokewikicompose.ui.widget

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.orhanobut.logger.Logger
import com.poke.pokewikicompose.R
import com.poke.pokewikicompose.dataBase.data.bean.PokemonEvolutionBean

@Composable
fun EvolutionBar(
    itemList: List<PokemonEvolutionBean>,
    nowIndex: Int,
    onItemClick: (Int) -> Unit = {}
) {
    val rememberIndex = remember { mutableStateOf(nowIndex) }
    LaunchedEffect(nowIndex) {
        Logger.d("now Index: $nowIndex")
        rememberIndex.value = nowIndex
    }
    LazyRow(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 25.dp),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.Top
    ) {
        for (item in itemList) {
            item {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(modifier = Modifier.size(50.dp)) {
                        if (rememberIndex.value != itemList.indexOf(item))
                            Image(
                                modifier = Modifier
                                    .size(50.dp)
                                    .clickable { onItemClick.invoke(item.id) },
                                painter =
                                painterResource(R.drawable.pokemon_evu_unselect_bg),
                                contentDescription = ""
                            )
                        else
                            Image(
                                modifier = Modifier
                                    .size(50.dp),
                                painter =
                                painterResource(R.drawable.pokemon_detail_bg),
                                contentDescription = ""
                            )
                        AsyncImage(
                            modifier = Modifier.size(50.dp),
                            model = item.img_url,
                            contentDescription = item.name
                        )
                    }
                    Spacer(modifier = Modifier.height(5.dp))
                    Text(
                        text = item.name,
                        fontSize = 10.sp
                    )
                    Spacer(modifier = Modifier.height(5.dp))
                    Text(
                        text = if (item.min_level != 0) "LV ${item.min_level}" else "????????????",
                        fontSize = 10.sp
                    )
                }
            }
            if (item != itemList.last()) {
                item {
                    Divider(
                        modifier = Modifier
                            .width(30.dp)
                            .clip(RoundedCornerShape(1.dp))
                            .padding(top = 25.dp),
                        thickness = 2.dp,
                        color = Color.LightGray
                    )
                }
            }
        }
    }
}

@Composable
@Preview
private fun EvolutionBarPreview() {
    val list = ArrayList<PokemonEvolutionBean>()
    list.add(
        PokemonEvolutionBean(
            id = 1,
            img_url = "http://192.168.2.12:8080/image/small/1.png",
            min_level = 1,
            name = "????????????"
        )
    )
    list.add(
        PokemonEvolutionBean(
            id = 2,
            img_url = "http://192.168.2.12:8080/image/small/2.png",
            min_level = 16,
            name = "?????????"
        )
    )
    list.add(
        PokemonEvolutionBean(
            id = 3,
            img_url = "http://192.168.2.12:8080/image/small/3.png",
            min_level = 32,
            name = "?????????"
        )
    )
    EvolutionBar(list, 1)
}