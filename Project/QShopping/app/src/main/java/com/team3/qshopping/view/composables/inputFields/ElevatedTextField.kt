package com.team3.qshopping.view.composables.inputFields

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import java.util.*

@Composable
fun ElevatedTextField(
    value: String,
    placeholder: String = "",
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    leadingIcon: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
    singleLine: Boolean = false,
    fillMaxWidth: Boolean = true,
    modifier: Modifier = Modifier,
    onValueChange: (String) -> Unit
) {
    val xModifier = if (fillMaxWidth) {
        modifier.fillMaxWidth()
    } else {
        modifier
    }

    val vsTransformation =
        if (keyboardOptions.keyboardType == KeyboardType.Password) PasswordVisualTransformation()
        else VisualTransformation.None

    TextField(
        value = value,
        onValueChange = onValueChange,
        singleLine = singleLine,
        placeholder = { Text(placeholder) },
        modifier = xModifier
            .border(1.dp, MaterialTheme.colors.background, RoundedCornerShape(30))
            .shadow(
                elevation = 12.dp, shape = RoundedCornerShape(30), spotColor = Color(0xFFD28A00)
            ),
        colors = TextFieldDefaults.textFieldColors(
            backgroundColor = MaterialTheme.colors.background,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            disabledIndicatorColor = Color.Transparent
        ),
        keyboardOptions = keyboardOptions,
        visualTransformation = vsTransformation,
        leadingIcon = leadingIcon,
        trailingIcon = trailingIcon,
    )
}

@Composable
fun MultipleElevatedTextFields(
    inputFields: Map<String, MutableState<String>>, verticalSpacing: Dp = 10.dp
) {
    Column(verticalArrangement = Arrangement.spacedBy(verticalSpacing)) {
        inputFields.map { (placeholder, state) ->
            var value by state
            val keyboardOption: KeyboardOptions =
                when (placeholder.lowercase(Locale.getDefault())) {
                    "email" -> KeyboardOptions(keyboardType = KeyboardType.Email)
                    "password" -> KeyboardOptions(keyboardType = KeyboardType.Password)
                    "phone number" -> KeyboardOptions(keyboardType = KeyboardType.Phone)
                    else -> KeyboardOptions.Default
                }

            ElevatedTextField(placeholder = placeholder,
                value = value,
                keyboardOptions = keyboardOption,
                onValueChange = { value = it }
            )
        }
    }
}