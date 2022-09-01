package com.poke.pokewikicompose.ui.widget

import android.support.annotation.DrawableRes
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.animateSizeAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.orhanobut.logger.Logger
import com.poke.pokewikicompose.R

@Composable
fun BottomNaviBar(
    itemList: List<NaviItem>,
    chooseIndex: Int = 0,
    onChangeIndex: (Int) -> Unit = {}
) {
    val rememberChoose = remember { mutableStateOf(chooseIndex) }
    LaunchedEffect(chooseIndex) {
        rememberChoose.value = chooseIndex
        Logger.d("now bottom init index: $rememberChoose")
    }

    LazyRow(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        items(itemList) { item ->
            Column(
                modifier = Modifier
                    .height(60.dp)
                    .width(40.dp)
                    .padding(vertical = 5.dp)
                    .clickable {
                        rememberChoose.value = itemList.indexOf(item)
                        onChangeIndex.invoke(rememberChoose.value)
                    },
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceAround
            ) {
                val imageSize = animateSizeAsState(
                    targetValue =
                    if (rememberChoose.value == itemList.indexOf(item)) Size(40f, 40f)
                    else Size(30f, 30f)
                )
                Image(
                    painter = painterResource(item.itemImg),
                    contentDescription = item.text,
                    modifier = Modifier
                        .size(imageSize.value.width.dp, imageSize.value.height.dp)
                )
                if (rememberChoose.value != itemList.indexOf(item))
                    Text(
                        text = item.text,
                        fontSize = 10.sp
                    )
            }
        }
    }
}

data class NaviItem(
    @DrawableRes val itemImg: Int,
    val text: String
)

@Composable
@Preview
private fun BottomNaviBarPreview() {
    val itemList = ArrayList<NaviItem>()
    itemList.add(NaviItem(R.drawable.pokemon_info, "信息"))
    itemList.add(NaviItem(R.drawable.pokemon_state, "状态"))
    itemList.add(NaviItem(R.drawable.pokemon_move, "招式"))
    BottomNaviBar(itemList)
}