package com.team3.qshopping.view.composables.buttons

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.team3.qshopping.ui.theme.ButtonTextColor
import com.team3.qshopping.ui.theme.Peach
import com.team3.qshopping.ui.theme.QShoppingTheme

@Composable
fun MatteButton(text: String, trailingIcon: ImageVector? = null, widthFraction: Float = 1f, onClick: () -> Unit) {
    TextButton(
        modifier = Modifier
            .fillMaxWidth(widthFraction)
            .clip(shape = RoundedCornerShape(20))
            .height(48.dp)
            .background(Peach),
        onClick = onClick
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 10.dp),
            horizontalArrangement = if (trailingIcon != null) {
                Arrangement.SpaceBetween
            } else {
                Arrangement.Center
            }
        ) {
            Text(
                text = text, style = MaterialTheme.typography.button, color = ButtonTextColor
            )
            trailingIcon?.let {
                Icon(
                    imageVector = it,
                    tint = ButtonTextColor,
                    contentDescription = "Button trailing icon"
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MatteButtonPreview() {
    QShoppingTheme {
        Column(
            modifier = Modifier.padding(10.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {

            // No trailing icon
            MatteButton(
                text = "Placeholder",
                onClick = {}
            )

            // With a trailing icon
            MatteButton(
                text = "Placeholder",
                trailingIcon = Icons.Filled.ArrowForward,
                onClick = {}
            )
        }
    }
}