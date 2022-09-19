package com.poke.pokewikicompose.ui.widget

import androidx.compose.foundation.Image
import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.poke.pokewikicompose.R
import com.poke.pokewikicompose.dataBase.data.bean.PokemonSearchBean

@Composable
fun PokemonSearchDetailCard(
    item: PokemonSearchBean,
    onClick: () -> Unit
) {
    Card(
        elevation = 5.dp,
        modifier = Modifier
            .padding(10.dp)
            .clickable(
                interactionSource = MutableInteractionSource(),
                indication = LocalIndication.current
            ) {
              onClick.invoke()
            },
        shape = RoundedCornerShape(10.dp),
        backgroundColor = Color(0xFFD4E5F8)
    ) {
        Column(modifier = Modifier.fillMaxWidth().padding(bottom = 10.dp)) {
            Box(modifier = Modifier.fillMaxWidth().aspectRatio(1f).padding(5.dp)) {
                Image(
                    painter = painterResource(R.drawable.pokemon_detail_bg),
                    contentDescription = "",
                    modifier = Modifier.fillMaxWidth()
                        .clip(CircleShape)
                )
                AsyncImage(
                    model = item.img_url,
                    contentDescription = item.pokemon_name,
                    modifier = Modifier.fillMaxSize()
                )
            }
            Spacer(modifier = Modifier.height(5.dp))
            Text(
                text = item.pokemon_name,
                color = Color.Black,
                fontSize = 12.sp,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(5.dp))
            LazyRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                items(item.pokemon_type) {
                    PokemonTag(text = it, fontSize = 10, isColored = true)
                }
                item {
                    PokemonTag(text = item.pokemon_id)
                }
            }
        }
    }
}

@Composable
@Preview
fun PokemonSearchDetailCardPreview() {
    val typeArrayList = ArrayList<String>()
    typeArrayList.add("草")
    typeArrayList.add("毒")
    PokemonSearchDetailCard(
        PokemonSearchBean(
            img_url = "",
            pokemon_id = "3",
            pokemon_type = typeArrayList,
            pokemon_name = "妙蛙花"
        )
    ) {}
}