package com.poke.pokewikicompose.ui.widget

import android.support.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.poke.pokewikicompose.R
import com.poke.pokewikicompose.ui.theme.PokeBallRed

@Composable
fun TitleBar(
    color: Color = PokeBallRed,
    title: String = "",
    onBackClick: () -> Unit = {},
    @DrawableRes rightIcon: Int? = null,
    onClickRight: (() -> Unit)? = null
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .height(45.dp),
        color = color,
        elevation = 10.dp,
        shape = RoundedCornerShape(bottomStart = 6.dp, bottomEnd = 6.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 5.dp)
        ) {
            Text(
                modifier = Modifier
                    .align(Alignment.Center),
                text = title,
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp,
            )
            Image(
                painter = painterResource(R.drawable.white_back),
                contentDescription = "back button",
                modifier = Modifier
                    .size(35.dp)
                    .padding(5.dp)
                    .clip(CircleShape)
                    .align(Alignment.CenterStart)
                    .clickable { onBackClick.invoke() }
            )
            rightIcon?.let {
                Image(
                    painter = painterResource(rightIcon),
                    contentDescription = "",
                    modifier = Modifier
                        .size(40.dp)
                        .padding(5.dp)
                        .clip(CircleShape)
                        .align(Alignment.CenterEnd)
                        .clickable { onClickRight?.invoke() }
                )
            }
        }
    }
}

@Preview
@Composable
private fun TitleBarPreview() {
    TitleBar(title = "修改密码")
}