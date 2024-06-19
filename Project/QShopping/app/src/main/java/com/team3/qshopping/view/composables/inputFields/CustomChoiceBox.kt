package com.team3.qshopping.view.composables.inputFields

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.team3.qshopping.R
import com.team3.qshopping.view.conditional

@Composable
fun <T> CustomChoiceBox(
    currentChoice: MutableState<T>,
    choices: Map<T, String>,
    placeholder: String = "Select",
    enabled: Boolean = true,
) {
    var expanded by remember { mutableStateOf(false) }
    Box {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(shape = RoundedCornerShape(12.dp))
                .conditional(enabled) { clickable { expanded = !expanded } }
                .conditional(enabled) {
                    background(Color(0xFFF5F6FA), shape = RoundedCornerShape(12.dp))
                }
                .conditional(!enabled) {
                    background(Color.LightGray, shape = RoundedCornerShape(12.dp))
                }
                .padding(vertical = 16.dp, horizontal = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                choices[currentChoice.value] ?: placeholder,
                style = MaterialTheme.typography.body2
            )
            Icon(
                modifier = Modifier.rotate(if (expanded) 180f else 0f),
                painter = painterResource(id = R.drawable.ic_arrowdown),
                contentDescription = "arrow"
            )
        }
        DropdownMenu(expanded = expanded,
            onDismissRequest = { expanded = false }) {
            choices.map {
                DropdownMenuItem(onClick = {
                    currentChoice.value = it.key
                    expanded = false
                }) {
                    Text(it.value)
                }
            }
        }
    }
}
