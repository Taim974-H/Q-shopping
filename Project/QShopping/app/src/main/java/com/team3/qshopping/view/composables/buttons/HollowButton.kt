package com.team3.qshopping.view.composables.buttons

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.team3.qshopping.ui.theme.ButtonTextColor


@Composable
fun HollowButton(text: String, onClick: () -> Unit, trailingIcon: ImageVector? = null) {
    OutlinedButton(
        modifier = Modifier
            .width(125.dp)
            .height(41.dp),
        colors = ButtonDefaults.outlinedButtonColors(backgroundColor = Color.White),
        border = BorderStroke(1.dp, Color(0xFFFEAA2B)),
        shape = RoundedCornerShape(20),
        onClick = onClick,
    ) {
        Text(

            text = text,
            style = MaterialTheme.typography.body2,
            fontWeight = FontWeight.SemiBold,
            color = Color(0xFFFEAA2B),
        )
        trailingIcon?.let {
            Spacer(modifier = Modifier.width(10.dp))
            Icon(
                imageVector = it,
                tint = ButtonTextColor,
                contentDescription = "Button trailing icon"
            )
        }
    }
}