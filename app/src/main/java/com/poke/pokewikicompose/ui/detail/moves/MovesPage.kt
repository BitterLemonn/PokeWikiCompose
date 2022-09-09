package com.poke.pokewikicompose.ui.detail.moves

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.poke.pokewikicompose.dataBase.data.bean.PokemonMoveBean
import com.poke.pokewikicompose.dataBase.data.bean.PokemonMovesBean
import com.poke.pokewikicompose.ui.widget.PokemonMoveCapsule

@Composable
fun MovesPage(
    moves: List<PokemonMoveBean>
) {
    val studyMoves = remember { mutableStateListOf<PokemonMoveBean>() }
    val otherMoves = remember { mutableStateListOf<PokemonMoveBean>() }
    LaunchedEffect(Unit) {
        moves.forEach { move ->
            if (move.level != 0)
                studyMoves.add(move)
            else
                otherMoves.add(move)
        }
    }
    Card(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 20.dp),
        backgroundColor = Color.White,
        shape = RoundedCornerShape(topEnd = 16.dp, topStart = 16.dp)
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(top = 15.dp),
            contentPadding = PaddingValues(horizontal = 10.dp)
        ) {
            item {
                Text(
                    text = "可学会的招式",
                    fontSize = 14.sp,
                    color = Color.Black
                )
                Spacer(modifier = Modifier.height(30.dp))
            }
            items(studyMoves) {
                PokemonMoveCapsule(it)
                Spacer(modifier = Modifier.height(20.dp))
            }
            if (otherMoves.isNotEmpty()) {
                item {
                    Text(
                        text = "其他方式学会的招式",
                        fontSize = 14.sp,
                        color = Color.Black
                    )
                    Spacer(modifier = Modifier.height(30.dp))
                }
                items(otherMoves) {
                    PokemonMoveCapsule(it)
                    Spacer(modifier = Modifier.height(20.dp))
                }
            }
        }
    }
}

@Composable
@Preview
private fun MovesPagePreview() {
    MovesPage(PokemonMovesBean().moves)
}