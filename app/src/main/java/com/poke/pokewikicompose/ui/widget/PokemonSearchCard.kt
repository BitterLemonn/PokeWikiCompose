package com.poke.pokewikicompose.ui.widget

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
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
import coil.compose.AsyncImage
import coil.compose.AsyncImagePainter
import coil.compose.SubcomposeAsyncImage
import coil.compose.SubcomposeAsyncImageContent
import com.poke.pokewikicompose.dataBase.data.bean.PokemonSearchBean
import com.poke.pokewikicompose.ui.theme.PokeBallRed
import com.poke.pokewikicompose.utils.AppCache
import com.poke.pokewikicompose.utils.AppContext
import java.io.File

@Composable
fun PokemonSearchCard(
    modifier: Modifier = Modifier,
    item: PokemonSearchBean,
    onClick: () -> Unit
) {
    val imageCacheItem = AppCache.getPathItem(item.pokemon_id.toInt())

    Card(
        modifier = modifier
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
                    AsyncImage(
                        modifier = Modifier.size(50.dp),
                        model = imageCacheItem?.let {
                            it.smallPath?.let { path -> File(path) } ?: item.img_url
                        } ?: item.img_url,
                        contentDescription = "pokemon image"
                    )
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
                    horizontalArrangement = Arrangement.End
                ) {
                    items(item.pokemon_type) {
                        Spacer(modifier = Modifier.width(10.dp))
                        PokemonTag(text = it, isColored = true)
                    }
                    item {
                        Spacer(modifier = Modifier.width(10.dp))
                        PokemonTag(text = item.pokemon_id)
                    }
                }

            }
        }
    }
}

@Composable
@Preview
private fun PokemonSearchCardPreview() {
    val typeArrayList = ArrayList<String>()
    typeArrayList.add("草")
    typeArrayList.add("毒")
    PokemonSearchCard(
        item = PokemonSearchBean(
            img_url = "",
            pokemon_id = "3",
            pokemon_type = typeArrayList,
            pokemon_name = "妙蛙花"
        )
    ){}
}