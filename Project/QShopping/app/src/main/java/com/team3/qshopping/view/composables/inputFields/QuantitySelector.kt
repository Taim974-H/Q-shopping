package com.team3.qshopping.view.composables.inputFields

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.team3.qshopping.R
import kotlin.math.max
import kotlin.math.min

@Composable
fun QuantitySelector(
    quantity: MutableState<Int>,
    useTextField: Boolean = false,
    minVal: Int = 0,
    maxVal: Int = Int.MAX_VALUE,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.End
    )
    {
        IconButton(
            onClick = { quantity.value = max(minVal, quantity.value - 1) },
            modifier = Modifier
                .statusBarsPadding()
                .background(
                    color = Color(0xFFF5F6FA),
                    shape = CircleShape
                )
                .padding(5.dp)
                .size(20.dp)
        ) {
            Image(
                painter = painterResource(R.drawable.ic_minus),
                contentDescription = "Remove 1"
            )
        }
        Spacer(modifier = Modifier.width(5.dp))
        if (useTextField) {

            NormalTextField(
                modifier = Modifier.width(75.dp),
                value = quantity.value.toString(),
                onValueChange = { quantity.value = it.ifBlank { "0" }.toInt() },
                keyboardOption = KeyboardOptions(keyboardType = KeyboardType.Number)
            )
        } else {
            Text(
                text = "${quantity.value}",
                style = MaterialTheme.typography.body2,
                color = Color(0xFF6A6D72),
                fontSize = 15.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.widthIn(min = 24.dp)
            )
        }

        Spacer(modifier = Modifier.width(5.dp))
        IconButton(
            onClick = { quantity.value = min(quantity.value + 1, maxVal) },
            modifier = Modifier
                .statusBarsPadding()
                .background(
                    color = Color(0xFFF5F6FA),
                    shape = CircleShape
                )
                .padding(5.dp)
                .size(20.dp)
        ) {
            Image(
                painter = painterResource(R.drawable.ic_plus),
                contentDescription = "Remove 1"
            )
        }
    }
}