package com.poke.pokewikicompose.ui.widget

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.annotation.ExperimentalCoilApi
import coil.compose.AsyncImagePainter
import coil.compose.SubcomposeAsyncImage
import coil.compose.SubcomposeAsyncImageContent
import com.poke.pokewikicompose.data.bean.PokemonSearchBean
import com.poke.pokewikicompose.ui.theme.PokeBallRed

@OptIn(ExperimentalCoilApi::class)
@Composable
fun PokemonSearchCard(
    item: PokemonSearchBean,
    onClick: () -> Unit = {
        Log.d("TAG", "PokemonSearchCard: 点击")
    }
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .height(60.dp),
        shape = RoundedCornerShape(10.dp),
        elevation = 5.dp
    ) {
        Button(
            onClick = onClick,
            colors = ButtonDefaults.buttonColors(
                backgroundColor = Color.Transparent,
                contentColor = Color.White
            ),
            elevation = ButtonDefaults.elevation(
                defaultElevation = 0.dp,
                pressedElevation = 0.dp,
                disabledElevation = 0.dp,
                hoveredElevation = 0.dp,
                focusedElevation = 0.dp
            ),
            shape = RoundedCornerShape(10.dp),
            contentPadding = PaddingValues()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 10.dp)
            ) {
                Row(
                    modifier = Modifier
                        .align(Alignment.CenterVertically)
                        .weight(1f)
                ) {
                    SubcomposeAsyncImage(
                        modifier = Modifier.size(50.dp),
                        model = item.img_url,
                        contentDescription = "pokemon image"
                    ) {
                        val state = painter.state
                        if (state is AsyncImagePainter.State.Loading || state is AsyncImagePainter.State.Error) {
                            CircularProgressIndicator(
                                color = PokeBallRed,
                                strokeWidth = 3.dp
                            )
                        } else {
                            SubcomposeAsyncImageContent()
                        }
                    }
                    Spacer(modifier = Modifier.width(15.dp))
                    Text(
                        modifier = Modifier.align(Alignment.CenterVertically),
                        text = item.pokemon_name,
                        color = Color.Black,
                        fontSize = 14.sp
                    )
                }
                LazyRow(
                    Modifier
                        .align(Alignment.CenterVertically)
                        .weight(1f),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    for (type in item.pokemon_type) {
                        item {
                            PokemonTag(text = type, isColored = true)
                        }
                    }
                    item {
                        PokemonTag(text = item.pokemon_id)
                    }
                }

            }
        }
    }
}

@Composable
@Preview
fun preview() {
    val typeArrayList = ArrayList<String>()
    typeArrayList.add("草")
    typeArrayList.add("毒")
    PokemonSearchCard(
        PokemonSearchBean(
            img_url = "",
            pokemon_id = "#003",
            pokemon_type = typeArrayList,
            pokemon_name = "妙蛙花"
        )
    )
}