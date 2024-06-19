package com.team3.qshopping.view.composables.buttons

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.team3.qshopping.ui.theme.LightGrey

@Composable
fun SmallRectangularButton(imageDrawable: Int, onCLick: () -> Unit) {
    IconButton(
        onClick = { onCLick() },
        modifier = Modifier
            .statusBarsPadding()
            .background(
                color = LightGrey,
                shape = RoundedCornerShape(0.1f)
            )
            .padding(5.dp)
            .size(20.dp)
    ) {
        Image(
            painter = painterResource(imageDrawable),
            colorFilter = ColorFilter.tint(Color.Black),
            contentDescription = "Button"
        )
    }
}