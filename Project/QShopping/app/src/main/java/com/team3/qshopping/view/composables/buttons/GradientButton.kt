package com.team3.qshopping.view.composables.buttons

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ShoppingCart
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.team3.qshopping.ui.theme.*
import com.team3.qshopping.view.angledGradientBackground

@Composable
fun GradientButton(
    text: String,
    widthFraction: Float = 1f,
    height: Dp = 48.dp,
    roundCornerPercent: Int = 20,
    toastText: String? = null,
    trailingIcon: ImageVector? = null,
    enabled: Boolean = true,
    onClick: () -> Unit
) {
    val context = LocalContext.current
    TextButton(
        modifier = Modifier
            .fillMaxWidth(widthFraction)
            .clip(shape = RoundedCornerShape(roundCornerPercent))
            .height(height)
            .angledGradientBackground(
                colors = if (enabled) {
                    listOf(
                        ButtonGradientStartColor,
                        ButtonGradientEndColor,
                    )
                } else {
                    listOf(SubtitleTextColor, SubtitleTextColor)
                },
                degrees = 280f
            ),
        enabled = enabled,
        onClick = {
            toastText?.let { Toast.makeText(context, it, Toast.LENGTH_SHORT).show() }
            onClick()
        }
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.button,
            color = ButtonTextColor
        )
        trailingIcon?.let {
            Spacer(modifier = Modifier.width(10.dp))
            Icon(
                imageVector = it,
                tint = ButtonTextColor,
                contentDescription = it.name
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GradientButtonPreview() {
    QShoppingTheme {
        Row(modifier = Modifier.padding(10.dp)) {
            GradientButton(
                text = "Text Placeholder",
                onClick = {},
                //Optional params
                widthFraction = 1f,
                height = 48.dp,
                roundCornerPercent = 20,
                toastText = null,
                trailingIcon = Icons.Outlined.ShoppingCart,
                enabled = false
            )
        }
    }
}
