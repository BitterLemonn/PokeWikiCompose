package com.poke.pokewikicompose.ui.widget

import android.support.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.poke.pokewikicompose.R
import com.poke.pokewikicompose.ui.theme.BtnText
import com.poke.pokewikicompose.ui.theme.RoyalBlue

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ScreenItemBtn(
    modifier: Modifier = Modifier,
    @DrawableRes leftIcon: Int,
    text: String,
    subText: String = "",
    isShowArrow: Boolean = false,
    onSwitchChange: ((Boolean) -> Unit)? = null,
    switchInitState: Boolean = false,
    onClick: () -> Unit = {}
) {
    val switchState = remember { mutableStateOf(switchInitState) }
    Surface(
        color = Color.White,
        elevation = 10.dp,
        shape = RoundedCornerShape(6.dp),
        modifier = modifier,
        onClick = {
            onClick.invoke()
            if (onSwitchChange != null) {
                switchState.value = !switchState.value
            }
        },
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(
                horizontal = 19.dp,
                vertical = if (onSwitchChange == null) 19.dp
                else 10.dp
            )
        ) {
            Image(
                painter = painterResource(leftIcon),
                contentDescription = text,
                modifier = Modifier.size(27.dp)
            )
            Spacer(modifier = Modifier.width(32.dp))
            Column(verticalArrangement = Arrangement.SpaceEvenly) {
                Text(
                    text = text,
                    color = BtnText,
                    fontSize = 16.sp
                )
                if (subText.isNotBlank()) {
                    Text(
                        text = subText,
                        color = BtnText,
                        fontSize = 12.sp
                    )
                }
            }
            Box(
                modifier = Modifier.weight(1f),
                contentAlignment = Alignment.CenterEnd
            ) {
                if (isShowArrow && onSwitchChange == null)
                    Image(
                        modifier = Modifier.size(20.dp),
                        painter = painterResource(R.drawable.grey_next),
                        contentDescription = "detail"
                    )
                else if (onSwitchChange != null) {
                    Switch(
                        checked = switchState.value,
                        onCheckedChange = {
                            onSwitchChange.invoke(it)
                            switchState.value = it
                        },
                        colors = SwitchDefaults.colors(
                            checkedTrackColor = RoyalBlue,
                            checkedThumbColor = RoyalBlue
                        )
                    )
                }
            }
        }
    }
}

@Composable
@Preview
private fun ScreenItemBtnPreview() {
    ScreenItemBtn(
        leftIcon = R.drawable.update,
        text = "检查更新",
        subText = "123123",
        onSwitchChange = {}
    )
}