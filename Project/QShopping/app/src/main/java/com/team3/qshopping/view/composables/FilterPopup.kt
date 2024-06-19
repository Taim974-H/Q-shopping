package com.team3.qshopping.view.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowDropDown
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.team3.qshopping.data.local.models.Category
import com.team3.qshopping.view.composables.buttons.GradientButton

// Modifiers
val containerStyle = Modifier
    .widthIn(300.dp)
    .background(Color.White)
    .padding(30.dp)

val dropDownInputStyle = Modifier
    .wrapContentSize(Alignment.TopStart)
    .width(150.dp)

@Composable
fun FilterPopup(
    opened: Boolean = true,
    setOpened: (Boolean) -> Unit,
    initRating: Int = 0,
    initPriceRange: ClosedFloatingPointRange<Float> = 200f..800f,
    apply: (category: Category?, rating: Int, priceRange: ClosedFloatingPointRange<Float>) -> Unit,
    categoryEnabled: Boolean = true,
    priceMin: Float = 0f,
    priceMax: Float = 1000f,
    categories: List<Category> = emptyList(),
) {
    var selectedCategory: Category? by remember { mutableStateOf(null) }
    var selectedRating by remember { mutableStateOf(initRating) }
    var selectedPriceRange by remember { mutableStateOf(initPriceRange) }

    val selectCategory = { cat: Category? ->
        selectedCategory = cat
    }
    val selectRating = { rating: Int ->
        selectedRating = rating
    }
    val selectPriceRange = { range: ClosedFloatingPointRange<Float> ->
        selectedPriceRange = range
    }

    if (opened) {
        Dialog(
            onDismissRequest = {
                setOpened(false)
            }
        ) {
            Card(shape = RoundedCornerShape(12.dp)) {
                Column(
                    modifier = containerStyle
                ) {
                    Text("Rating", fontWeight = FontWeight.Bold)
                    RatingSlider(initRating, selectRating)

                    Text("Price Range", fontWeight = FontWeight.Bold)
                    //Slider(value = 10f, valueRange = (0f..100F), onValueChange = {})
                    PriceSlider(priceMin, priceMax, initPriceRange, selectPriceRange)

                    if (categoryEnabled) {
                        Text("Category", fontWeight = FontWeight.Bold)
                        CategoryDropDown(categories, selectedCategory, selectCategory)
                    }

                    Spacer(Modifier.height(10.dp))
                    Row {
                        Spacer(Modifier.weight(0.25f))
                        GradientButton(
                            text = "Apply Filter",
                            widthFraction = 0.5f,
                            onClick = {
                                apply(selectedCategory, selectedRating, selectedPriceRange)
                                setOpened(false)
                            }
                        )
                        Spacer(Modifier.weight(0.25f))
                    }
                }
            }
        }
    }
}

@Composable
fun CategoryDropDown(
    categories: List<Category>,
    selectedCategory: Category?,
    selectCategory: (Category?) -> Unit,
) {
    var expanded by remember { mutableStateOf(false) }
    val items = listOf("All") + categories.map { cat -> cat.name }.toMutableList()
    OutlinedButton(
        modifier = dropDownInputStyle,
        onClick = { expanded = true },
        shape = RoundedCornerShape(10.dp)
    ) {
        Row {
            Text(
                selectedCategory?.name ?: "All"
            )
            Spacer(modifier = Modifier.weight(1f))
            Icon(
                Icons.Rounded.ArrowDropDown,
                contentDescription = "dropdown"
            )
        }
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier
                .background(
                    Color.White
                )
        ) {
            items.forEachIndexed { index, s ->
                DropdownMenuItem(onClick = {
                    if (index == 0)
                        selectCategory(null)
                    else
                        selectCategory(categories[index - 1])
                    expanded = false
                }) {
                    Text(text = s)
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun PriceSlider(
    min: Float,
    max: Float,
    initPriceRange: ClosedFloatingPointRange<Float>,
    selectPriceRange: (ClosedFloatingPointRange<Float>) -> Unit
) {
    var range by remember { mutableStateOf(initPriceRange) }
    Column {
        Text(text = "$${range.start.toInt()} to $${range.endInclusive.toInt()}")
        Row {
            RangeSlider(
                value = range, onValueChange = {
                    range = it
                    selectPriceRange(it)
                },
                valueRange = min..max
            )
        }
    }
}