package com.team3.qshopping.view.composables

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.team3.qshopping.ui.theme.SubtitleTextColor
import com.team3.qshopping.view.conditional

@Composable
fun DividerTextDividerRow(
    text: String = "",
    textColor: Color = SubtitleTextColor,
    clickable: Boolean = false,
    onClick: () -> Unit = {}
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Divider(
            modifier = Modifier.weight(1f),
            color = SubtitleTextColor,
            thickness = 0.5.dp
        )
        Text(
            text,
            style = MaterialTheme.typography.subtitle2,
            color = textColor,
            modifier = Modifier.conditional(clickable) { clickable { onClick() } }
        )
        Divider(
            modifier = Modifier.weight(1f),
            color = SubtitleTextColor,
            thickness = 0.5.dp
        )
    }

}