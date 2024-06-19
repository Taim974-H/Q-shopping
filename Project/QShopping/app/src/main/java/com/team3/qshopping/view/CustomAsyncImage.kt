package com.team3.qshopping.view

import androidx.compose.animation.core.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import coil.compose.AsyncImagePainter
import coil.compose.rememberAsyncImagePainter
import com.team3.qshopping.R

//From https://stackoverflow.com/a/69818797/14200676
// custom AsyncImage that lets you use any composable as a placeholder
@Composable
fun CustomAsyncImage(
    modifier: Modifier = Modifier,
    model: String,
    contentDescription: String? = null,
    placeholder: @Composable () -> Unit = { LoadingSpinner() },
) {
    Box(contentAlignment = Alignment.Center) {
        val painter = rememberAsyncImagePainter(model = model)
        Image(
            painter = painter,
            contentScale = ContentScale.FillWidth,
            contentDescription = contentDescription,
            modifier = modifier
        )
        if (painter.state !is AsyncImagePainter.State.Success) {
            placeholder()
        }
    }
}

@Composable
fun LoadingSpinner() {
    val angle by rememberInfiniteTransition().animateFloat(
        initialValue = 0F,
        targetValue = 360F,
        animationSpec = infiniteRepeatable(animation = tween(1000, easing = LinearEasing))
    )
    Image(
        modifier = Modifier.rotate(angle),
        painter = painterResource(R.drawable.ic_loading),
        contentDescription = "Spinner"
    )
}