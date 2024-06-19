package com.team3.qshopping.view.composables

import androidx.compose.foundation.layout.Column
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Slider
import androidx.compose.material.SliderDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.*

@Composable
fun RatingSlider(initRating:Int, selectRating: (Int) -> Unit) {
    var sliderPosition by remember { mutableStateOf(initRating.toFloat()) }
    Column {
        Text(text = sliderPosition.toInt().toString())
        Slider(
            value = sliderPosition,
            onValueChange = { sliderPosition = it },
            valueRange = 0f..5f,
            onValueChangeFinished = {
                selectRating(sliderPosition.toInt())
            },
            steps = 4,
            colors = SliderDefaults.colors(

                thumbColor = MaterialTheme.colors.primary,
                activeTrackColor = MaterialTheme.colors.primary
            )
        )
    }
}