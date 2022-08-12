package com.poke.pokewikicompose.ui.widget

import android.support.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.poke.pokewikicompose.R
import com.poke.pokewikicompose.ui.theme.BtnText

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ScreenItemBtn(
    modifier: Modifier = Modifier,
    @DrawableRes leftIcon: Int,
    text: String,
    isShowArrow: Boolean = false,
    onClick: () -> Unit = {}
) {
    Surface(
        color = Color.White,
        elevation = 10.dp,
        shape = RoundedCornerShape(6.dp),
        onClick = onClick,
        modifier = modifier
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(19.dp)
        ) {
            Image(
                painter = painterResource(leftIcon),
                contentDescription = text,
                modifier = Modifier.size(27.dp)
            )
            Spacer(modifier = Modifier.width(32.dp))
            Text(
                text = text,
                color = BtnText,
                fontSize = 16.sp
            )
            Box(
                modifier = Modifier.weight(1f),
                contentAlignment = Alignment.CenterEnd
            ) {
                if (isShowArrow)
                    Image(
                        modifier = Modifier.size(20.dp),
                        painter = painterResource(R.drawable.grey_next),
                        contentDescription = "detail"
                    )
            }
        }
    }
}

@Composable
@Preview
private fun ScreenItemBtnPreview() {
    ScreenItemBtn(leftIcon = R.drawable.update, text = "检查更新", isShowArrow = true)
}