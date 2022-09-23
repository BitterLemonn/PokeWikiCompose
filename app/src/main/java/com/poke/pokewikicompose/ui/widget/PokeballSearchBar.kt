package com.poke.pokewikicompose.ui.widget

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.poke.pokewikicompose.R

@OptIn(ExperimentalMaterialApi::class)
@Composable
//@Preview
fun PokeBallSearchBar(
    modifier: Modifier = Modifier,
    value: String = "",
    onValueChange: (String) -> Unit,
    onClick: (() -> Unit)? = null,
    onSearch: (() -> Unit)? = null
) {
    Surface(
        shape = RoundedCornerShape(100.dp),
        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp),
        onClick = onClick ?: {}
    ) {
        Row(
            modifier = modifier,
            verticalAlignment = Alignment.CenterVertically
        ) {
            TextField(
                modifier = Modifier
                    .padding(vertical = 0.dp)
                    .weight(1f),
                colors = TextFieldDefaults
                    .textFieldColors(
                        backgroundColor = Color.Transparent,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        cursorColor = Color.DarkGray,
                        disabledIndicatorColor = Color.Transparent,
                        disabledPlaceholderColor = Color.Transparent
                    ),
                singleLine = true,
                value = value,
                onValueChange = onValueChange,
                textStyle = LocalTextStyle.current.copy(fontSize = 14.sp),
                enabled = onClick == null,
                keyboardActions = KeyboardActions(
                    onDone = { onSearch?.invoke() }
                )
            )
            Button(
                modifier = Modifier.size(50.dp),
                onClick = onSearch ?: {},
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = Color.Transparent,
                    contentColor = Color.Transparent,
                    disabledBackgroundColor = Color.Transparent,
                    disabledContentColor = Color.Transparent
                ),
                contentPadding = PaddingValues(),
                elevation = ButtonDefaults.elevation(
                    defaultElevation = 0.dp,
                    pressedElevation = 0.dp,
                    hoveredElevation = 0.dp,
                    disabledElevation = 0.dp,
                    focusedElevation = 0.dp
                ),
                shape = CircleShape,
                enabled = onClick == null
            ) {
                Image(
                    painter = painterResource(R.drawable.search_pokeball),
                    contentDescription = "search button",
                    modifier = Modifier.size(30.dp)
                )
            }
            Spacer(modifier = Modifier.width(10.dp))
        }
    }
}