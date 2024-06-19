package com.team3.qshopping.view.composables.inputFields

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.team3.qshopping.ui.theme.QShoppingTheme

@Composable
fun SearchBar(value: String, onValueChange: (String) -> Unit) {

    NormalTextField(
        value = value,
        onValueChange = onValueChange,
        placeholder = "What are you looking for?",
        leadingIcon = {
            Icon(
                modifier = Modifier.clickable { },
                imageVector = Icons.Default.Search, contentDescription = "searchIcon"
            )
        },
    )
//    TextField(
//        value = value,
//        onValueChange = { setValue(it) },
//        modifier = Modifier.fillMaxWidth(),
//        shape = RoundedCornerShape(20),
//        colors = TextFieldDefaults.textFieldColors(
//            textColor = Color(0xFF8F959E),
//            backgroundColor = Color(0xFFF5F6FA),
//            focusedIndicatorColor = Color.Transparent,
//            unfocusedIndicatorColor = Color.Transparent
//        ),
//        placeholder = { Text(text = "What are you looking for?") },
//        leadingIcon = { Icon(
//            modifier = Modifier.clickable { },
//            imageVector = Icons.Default.Search, contentDescription = "searchIcon"
//        ) },
//    )
}

@Preview(showBackground = true)
@Composable
fun SearchBarPreview() {
    var value by remember { mutableStateOf("") }
    val setValue: (String) -> Unit = { value = it }

    QShoppingTheme {
        Row(modifier = Modifier.padding(10.dp)) {

            SearchBar(
                value = value,
                onValueChange = setValue
            )

        }
    }
}