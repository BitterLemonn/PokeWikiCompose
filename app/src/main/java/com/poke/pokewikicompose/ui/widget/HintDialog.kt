package com.poke.pokewikicompose.ui.widget

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.poke.pokewikicompose.ui.theme.PokeBallRed
import com.poke.pokewikicompose.ui.theme.RoyalBlue

@Composable
fun HintDialog(
    hint: String = "",
    onClickCertain: () -> Unit = {},
    onClickCancel: () -> Unit = {},
    highLineCertain: Boolean = true
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colors.onSurface.copy(alpha = 0.1f)),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(10.dp))
                .background(Color.White)
                .sizeIn(
                    minWidth = 250.dp,
                    minHeight = 120.dp,
                    maxWidth = 450.dp,
                    maxHeight = 200.dp
                ),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .matchParentSize()
            ) {
                Box(
                    modifier = Modifier.weight(1f)
                        .wrapContentSize()
                ) {
                    Text(
                        text = hint,
                        fontSize = 16.sp,
                        color = Color.Gray,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.align(Alignment.Center)
                            .padding(horizontal = 10.dp)
                    )
                }
                Box(
                    Modifier
                        .fillMaxWidth()
                        .background(Color.Gray.copy(alpha = 0.3f))
                        .height(1.dp)
                )
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    Button(
                        onClick = onClickCertain,
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.buttonColors(
                            backgroundColor = Color.Transparent
                        ),
                        modifier = Modifier.weight(1f)
                            .fillMaxHeight(),
                        elevation = ButtonDefaults.elevation(0.dp,0.dp,0.dp,0.dp)
                    ) {
                        Text(
                            text = "确定",
                            color = if (highLineCertain) RoyalBlue else Color.Gray,
                            fontSize = 16.sp,
                            textAlign = TextAlign.Center
                        )
                    }
                    Box(
                        modifier = Modifier
                            .width(1.dp)
                            .background(Color.Gray.copy(alpha = 0.3f))
                            .fillMaxHeight()
                            .padding(bottom = 5.dp)
                    )
                    Button(
                        onClick = onClickCancel,
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.buttonColors(
                            backgroundColor = Color.Transparent
                        ),
                        modifier = Modifier.weight(1f)
                            .fillMaxHeight(),
                        elevation = ButtonDefaults.elevation(0.dp,0.dp,0.dp,0.dp)
                    ) {
                        Text(
                            text = "取消",
                            color = if (highLineCertain) Color.Gray else PokeBallRed,
                            fontSize = 16.sp,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
        }
    }
}

@Composable
@Preview
private fun HintDialogPreview() {
    HintDialog(
        hint = "12312312312123123123123232",
        highLineCertain = false
    )
}