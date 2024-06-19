package com.team3.qshopping.view.composables.buttons

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.team3.qshopping.ui.theme.LightGrey

@Composable
fun TopNavButton(
    onPress: () -> Unit,
    horizontalPadding: Dp = 0.dp,
    verticalPadding: Dp = 10.dp,
    icon: @Composable () -> Unit
) {
    IconButton(
        onClick = onPress,
        modifier = Modifier
            .statusBarsPadding()
            .padding(horizontal = horizontalPadding, vertical = verticalPadding)
            .size(36.dp)
            .background(
                color = LightGrey,
                shape = CircleShape
            ),
        content = icon
    )
}

@Preview
@Composable
fun BackButton(onBackPress: () -> Unit = {}) {
    TopNavButton(
        onPress = onBackPress,
    ) {
        Icon(
            imageVector = Icons.Outlined.ArrowBack,
            tint = Color.Black,
            contentDescription = "Back"
        )
    }
}