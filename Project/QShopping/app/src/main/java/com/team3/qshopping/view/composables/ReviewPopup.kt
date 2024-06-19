package com.team3.qshopping.view.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.team3.qshopping.view.composables.buttons.GradientButton
import com.team3.qshopping.view.composables.inputFields.NormalTextField

@Composable
fun ReviewPopup(
    pop: Boolean = true,
    onDismissRequest: () -> Unit,
    rating: MutableState<Int>,
    title: MutableState<String>,
    text: MutableState<String>,
    onSubmitClick: () -> Unit
) {

    if (pop) {
        Dialog(
            onDismissRequest = { onDismissRequest() }
        ) {
            Card(
                shape = RoundedCornerShape(12.dp)
            ) {
                Column(
                    modifier = Modifier
                        .widthIn(300.dp)
                        .heightIn(0.dp, 400.dp)
                        .background(Color.White)
                        .padding(30.dp)
                ) {
                    Text("Rating", fontWeight = FontWeight.Bold)
                    RatingSlider(rating.value) { rating.value = it }

                    Spacer(modifier = Modifier.height(5.dp))

                    Text("Title", fontWeight = FontWeight.Bold)
                    NormalTextField(value = title.value, onValueChange = { title.value = it })

                    Spacer(modifier = Modifier.height(10.dp))

                    Text("Text", fontWeight = FontWeight.Bold)
                    OutlinedTextField(
                        value = text.value,
                        onValueChange = { text.value = it },
                        maxLines = 2
                    )

                    Spacer(modifier = Modifier.height(15.dp))

                    Row(
                        modifier = Modifier.fillMaxSize(),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.Bottom
                    ) {
                        GradientButton(
                            text = "Submit Review",
                            onClick = { onSubmitClick() }
                        )
                    }
                }
            }
        }
    }
}