package com.team3.qshopping.view.composables.buttons

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun CancelSaveButtonsRow (
    onCancel: () -> Unit,
    onSave: () -> Unit,
    horizontalArrangement: Arrangement.Horizontal = Arrangement.SpaceBetween,
    verticalAlignment: Alignment.Vertical = Alignment.CenterVertically,
    buttonWidth: Dp = 110.dp
) {
    Row(
        horizontalArrangement = horizontalArrangement,
        verticalAlignment = verticalAlignment,
        modifier = Modifier.fillMaxWidth()
    ) {
        Box(modifier = Modifier.width(buttonWidth)) {
            GradientButton(text = "Cancel") { onCancel() }
        }
        Box(modifier = Modifier.width(buttonWidth)) {
            GradientButton(text = "Save!") { onSave() }
        }
    }
}