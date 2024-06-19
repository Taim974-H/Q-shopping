package com.team3.qshopping.view.composables.inputFields

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp

@Composable
fun NormalTextField(
    modifier: Modifier = Modifier,
    value: String,
    placeholder: String = "",
    keyboardOption: KeyboardOptions = KeyboardOptions.Default,
    leadingIcon: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
    singleLine: Boolean = true,
    onValueChange: (String) -> Unit
) {
    val vsTransformation =
        if (keyboardOption.keyboardType == KeyboardType.Password) PasswordVisualTransformation()
        else VisualTransformation.None

    TextField(
        modifier = Modifier
            .then(modifier)
            .fillMaxWidth(),
        value = value,

        onValueChange = onValueChange,
        singleLine = singleLine,
        placeholder = { Text(placeholder) },
        colors = TextFieldDefaults.textFieldColors(
            backgroundColor = Color(0xFFF5F6FA),
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            disabledIndicatorColor = Color.Transparent
        ),
        shape = RoundedCornerShape(12.dp),
        keyboardOptions = keyboardOption,
        visualTransformation = vsTransformation,
        leadingIcon = leadingIcon,
        trailingIcon = trailingIcon,
        maxLines = 5
    )
}