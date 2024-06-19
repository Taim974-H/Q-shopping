package com.team3.qshopping.view.composables

import android.graphics.Paint
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.core.graphics.ColorUtils
import com.team3.qshopping.ui.theme.LightGrey
import kotlinx.coroutines.delay

// from https://github.com/m-derakhshan/ComposeChart/
@ExperimentalAnimationApi
@Composable
fun BarChart(
    data: Map<String, Double>,
    title: String = "Graph",
    barCornersRadius: Float = 25f,
    barColor: Color = MaterialTheme.colors.primary,
    barWidth: Float = 50f,
    height: Dp,
    labelOffset: Float = 60f,
    labelColor: Color = Color.Black,
    collapsable: Boolean = true,
    closeIcon: ImageVector = Icons.Default.KeyboardArrowUp,
) {
    var isExpanded by remember { mutableStateOf(true) }
    var screenSize by remember {
        mutableStateOf(Size.Zero)
    }

    var chosenBar by remember {
        mutableStateOf(-1)
    }
    var chosenBarKey by remember {
        mutableStateOf("")
    }

    val cardHeight by animateDpAsState(
        targetValue = if (isExpanded) height else 50.dp, animationSpec = tween(
            1000, easing = LinearOutSlowInEasing
        )
    )

    val rotate by animateFloatAsState(
        targetValue = if (isExpanded) 0f else 180f, animationSpec = tween(
            700, easing = LinearOutSlowInEasing
        )
    )

    LaunchedEffect(chosenBar) {
        delay(3000)
        chosenBarKey = ""
    }
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(cardHeight)
            .animateContentSize()
            .shadow(3.dp, shape = RoundedCornerShape(25.dp))
            .background(color = LightGrey, shape = RoundedCornerShape(25.dp))
    ) {
        if (collapsable) {
            TextButton(onClick = { isExpanded = !isExpanded }) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight()
                        .padding(start = 10.dp)
                ) {
                    Icon(
                        imageVector = closeIcon,
                        contentDescription = "Close chart",
                        modifier = Modifier.rotate(rotate)
                    )
                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center,
                        text = title,
                        color = Color.Gray,
                    )
                }
            }
        } else {
            Text(
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center,
                text = title,
                color = Color.Gray,
            )
        }
        Spacer(modifier = Modifier.width(30.dp))
        Canvas(modifier = Modifier
            .fillMaxSize()
            .alpha(if (cardHeight < height) (cardHeight - 90.dp) / height else 1f)
            .padding(
                top = 65.dp, bottom = 20.dp, start = 50.dp, end = 50.dp
            )
            .pointerInput(Unit) {
                this.detectTapGestures(onPress = {
                    chosenBar = detectPosition(
                        screenSize = screenSize,
                        offset = it,
                        listSize = data.size,
                        itemWidth = barWidth
                    )
                    if (chosenBar >= 0) {
                        chosenBarKey = data.toList()[chosenBar].first
                    }
                })
            }, onDraw = {
            screenSize = size
            val spaceBetweenBars = (size.width - (data.size * barWidth)) / (data.size - 1)
            val maxBarHeight = data.values.maxOf { it }
            val barScale = size.height / maxBarHeight
            val paint = Paint().apply {
                this.color = labelColor.toArgb()
                textAlign = Paint.Align.CENTER
                textSize = 40f
            }

            var spaceStep = 0f

            for (item in data) {
                val topLeft = Offset(
                    x = spaceStep,
                    y = (size.height - item.value * barScale - labelOffset).toFloat()
                )
                //--------------------(draw bars)--------------------//
                drawRoundRect(
                    color = barColor, topLeft = topLeft, size = Size(
                        width = barWidth, height = size.height - topLeft.y - labelOffset
                    ), cornerRadius = CornerRadius(barCornersRadius, barCornersRadius)
                )
                //--------------------(showing the x axis labels)--------------------//
                drawContext.canvas.nativeCanvas.drawText(
                    item.key, spaceStep + barWidth / 2, size.height, paint
                )
                //--------------------(showing the bar label)--------------------//
                if (chosenBarKey == item.key) {
                    val localLabelColor = Color(
                        ColorUtils.blendARGB(
                            Color.White.toArgb(), barColor.toArgb(), 0.4f
                        )
                    )
                    drawRoundRect(
                        color = localLabelColor,
                        topLeft = Offset(x = topLeft.x - 40f, y = topLeft.y - 100),
                        size = Size(140f, 80f),
                        cornerRadius = CornerRadius(15f, 15f)
                    )
                    val path = Path().apply {
                        moveTo(topLeft.x + 50f, topLeft.y - 20)
                        lineTo(topLeft.x + 25f, topLeft.y)
                        lineTo(topLeft.x, topLeft.y - 20)
                        lineTo(topLeft.x + 50f, topLeft.y - 20)
                    }
                    drawIntoCanvas { canvas ->
                        canvas.drawOutline(outline = Outline.Generic(path = path),
                            paint = androidx.compose.ui.graphics.Paint().apply {
                                color = localLabelColor
                            })
                    }

                    drawContext.canvas.nativeCanvas.drawText(
                        "%.2f".format(item.value), topLeft.x + 25, topLeft.y - 50, paint
                    )
                }
                spaceStep += spaceBetweenBars + barWidth
            }
        })
    }
}


private fun detectPosition(screenSize: Size, offset: Offset, listSize: Int, itemWidth: Float): Int {
    val spaceBetweenBars = (screenSize.width - (listSize * itemWidth)) / (listSize - 1)
    var spaceStep = 0f
    for (i in 0 until listSize) {
        if (offset.x in spaceStep..(spaceStep + itemWidth)) {
            return i
        }
        spaceStep += spaceBetweenBars + itemWidth
    }
    return -1
}
