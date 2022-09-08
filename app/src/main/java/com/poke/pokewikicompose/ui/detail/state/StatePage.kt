package com.poke.pokewikicompose.ui.detail.state

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Card
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.poke.pokewikicompose.dataBase.data.bean.PokemonStateBean
import com.poke.pokewikicompose.ui.theme.*
import com.poke.pokewikicompose.ui.widget.PokeStateBar

@Composable
fun StatePage(
    stateBean: PokemonStateBean
) {
    Column(
        modifier = Modifier
            .verticalScroll(rememberScrollState())
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Card(
            modifier = Modifier
                .defaultMinSize(minHeight = 350.dp)
                .padding(horizontal = 25.dp),
            shape = RoundedCornerShape(16.dp),
            elevation = 10.dp,
            backgroundColor = Color.White
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(),
                verticalArrangement = Arrangement.SpaceEvenly,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                PokeStateBar(
                    color = HP,
                    text = "HP",
                    num = stateBean.HP
                )
                PokeStateBar(
                    color = ATK,
                    text = "ATK",
                    num = stateBean.ATK
                )
                PokeStateBar(
                    color = DEF,
                    text = "DEF",
                    num = stateBean.DEF
                )
                PokeStateBar(
                    color = SpicATK,
                    text = "SATK",
                    num = stateBean.SATK
                )
                PokeStateBar(
                    color = SpicDEF,
                    text = "SDEF",
                    num = stateBean.SDEF
                )
                PokeStateBar(
                    color = SPD,
                    text = "SPD",
                    num = stateBean.SPD
                )

            }
        }
        Spacer(modifier = Modifier.height(30.dp))
    }

}

@Composable
@Preview
private fun StatePagePreview() {
    StatePage(stateBean = PokemonStateBean())
}