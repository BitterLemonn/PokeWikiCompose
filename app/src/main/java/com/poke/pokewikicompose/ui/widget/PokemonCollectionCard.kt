package com.poke.pokewikicompose.ui.widget

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.poke.pokewikicompose.R
import com.poke.pokewikicompose.dataBase.data.bean.PokemonSearchBean
import com.poke.pokewikicompose.ui.theme.PokeBallRed
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

private val swipeDistance = 70.dp

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun PokemonCollectionCard(item: PokemonSearchBean, onDelItem: () -> Unit, onClickCard: () -> Unit) {
    val swipeDistance = with(LocalDensity.current) { swipeDistance.toPx() }
    val swipeState = rememberSwipeableState(false)

    val coroutine = rememberCoroutineScope()

    Card(
        modifier = Modifier
            .height(70.dp)
            .fillMaxWidth()
            .swipeable(
                anchors = mapOf(
                    0f to true,
                    swipeDistance * 2 to false
                ),
                thresholds = { _, _ -> FractionalThreshold(0.5f) },
                orientation = Orientation.Horizontal,
                state = swipeState
            )
            .clickable { onClickCard.invoke() },
        elevation = 5.dp,
        shape = RoundedCornerShape(10.dp)
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                AsyncImage(
                    model = item.img_url,
                    contentDescription = "",
                    modifier = Modifier
                        .padding(horizontal = 10.dp, vertical = 5.dp)
                        .size(60.dp)
                )
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .padding(vertical = 5.dp)
                ) {
                    Text(
                        text = item.pokemon_name,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )
                    Spacer(modifier = Modifier.height(5.dp))
                    LazyRow(
                        horizontalArrangement = Arrangement.Start,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        items(item.pokemon_type) {
                            PokemonTag(text = it, isColored = true)
                            Spacer(modifier = Modifier.width(10.dp))
                        }
                    }
                }
            }
            Box(
                modifier = Modifier
                    .width(swipeDistance.dp)
                    .offset {
                        IntOffset(
                            swipeState.offset.value.roundToInt(),
                            0
                        )
                    }
                    .align(Alignment.CenterEnd)
                    .fillMaxHeight()
                    .clip(RoundedCornerShape(10.dp))
            ) {
                Row(
                    modifier = Modifier.fillMaxHeight(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxHeight()
                            .clickable(
                                enabled = true,
                                indication = null,
                                interactionSource = MutableInteractionSource(),
                                onClick = {
                                    coroutine.launch { swipeState.snapTo(!swipeState.currentValue) }
                                }
                            ),
                        contentAlignment = Alignment.CenterStart
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.more_info),
                            contentDescription = "delete",
                            modifier = Modifier
                                .padding(start = 5.dp, top = 5.dp, bottom = 5.dp)
                                .size(28.dp)
                                .fillMaxHeight()
                        )
                    }
                    Box(
                        modifier = Modifier
                            .width(swipeDistance.dp / 2)
                            .fillMaxHeight()
                            .background(PokeBallRed)
                    ) {
                        Button(
                            onClick = {
                                onDelItem.invoke()
                                coroutine.launch { swipeState.snapTo(false) }
                            },
                            modifier = Modifier.fillMaxSize(),
                            elevation = ButtonDefaults.elevation(
                                defaultElevation = 0.dp,
                                disabledElevation = 0.dp,
                                hoveredElevation = 0.dp,
                                focusedElevation = 0.dp
                            ),
                            colors = ButtonDefaults.buttonColors(
                                backgroundColor = PokeBallRed
                            )
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.collection_del),
                                contentDescription = "delete button",
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
@Preview
fun PokemonCollectionCardPreview() {
    val type = ArrayList<String>()
    type.add("草")
    type.add("毒")
    PokemonCollectionCard(
        PokemonSearchBean(
            pokemon_id = "1",
            pokemon_name = "妙蛙种子",
            img_url = "192.168.0.105:8080/image/small/1.png",
            pokemon_type = type
        ),
        onClickCard = {},
        onDelItem = {}
    )
}