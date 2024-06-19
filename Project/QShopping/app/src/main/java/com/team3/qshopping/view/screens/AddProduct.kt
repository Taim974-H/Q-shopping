package com.team3.qshopping.view.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Switch
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.team3.qshopping.view.composables.buttons.BackButton
import com.team3.qshopping.view.composables.buttons.GradientButton
import com.team3.qshopping.view.composables.inputFields.CustomChoiceBox
import com.team3.qshopping.view.composables.inputFields.NormalTextField
import com.team3.qshopping.view.composables.inputFields.QuantitySelector
import com.team3.qshopping.view.composables.layout.Header
import com.team3.qshopping.viewmodel.AddUpdateProductViewModel

@Composable
fun AddProductScreen(
    navController: NavController,
) {
    val addProductViewModel: AddUpdateProductViewModel = viewModel()

    Surface {
        Box(
            modifier = Modifier
                .fillMaxSize()
        ) {
            Column(
                modifier = Modifier.verticalScroll(rememberScrollState())
            ) {
                Header(
                    start = { BackButton { navController.popBackStack() } },
                    middle = {
                        Text(
                            text = "Add Product",
                            style = MaterialTheme.typography.h3
                        )
                    }
                )
                Spacer(modifier = Modifier.height(48.dp))
                ProductForm(addProductViewModel)
                Spacer(modifier = Modifier.height(24.dp))
                GradientButton(text = "Add Product") {
                    addProductViewModel.onAddButtonClicked()
                    navController.popBackStack()
                }
                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }
}

@Composable
fun ProductForm(addUpdateProductViewModel: AddUpdateProductViewModel) {
    val itemCategory = addUpdateProductViewModel.categoryId
    var onSale by addUpdateProductViewModel.isOnSale
    var title by addUpdateProductViewModel.title
    var description by addUpdateProductViewModel.description
    val stock = addUpdateProductViewModel.stock
    var unitPrice by addUpdateProductViewModel.unitPrice
    val discountRate = addUpdateProductViewModel.discountRate
    val categories = addUpdateProductViewModel.categories

    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        Text("Category")
        CustomChoiceBox(
            currentChoice = itemCategory,
            choices = categories.associate { it.id to it.name }
        )
        Text("Title")
        NormalTextField(
            value = title,
            onValueChange = { title = it },
        )
        Text("Unit Price")
        NormalTextField(
            value = unitPrice,
            onValueChange = { unitPrice = it },
            leadingIcon = { Text("$") },
            keyboardOption = KeyboardOptions(keyboardType = KeyboardType.Decimal)
        )
        Text("Description")

        NormalTextField(
            modifier = Modifier.height(150.dp),
            value = description,
            onValueChange = { description = it },
            singleLine = false,
        )
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Stock")
            QuantitySelector(stock, useTextField = true)
        }
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("On Sale")
            Switch(checked = onSale, onCheckedChange = {
                onSale = !onSale
                if (!onSale)
                    discountRate.value = 0.0
            })
        }
        CustomChoiceBox(
            currentChoice = discountRate,
            choices = generateSequence(0.0) { it + 0.05 }
                .takeWhile { it <= 1 }
                .associateWith { "%.0f%%".format((it * 100)) },
            enabled = onSale,
            placeholder = "Select discount rate"
        )
        // TODO replace with slider

//                    Slider(
//                        value = discountRate.toFloat(),
//                        onValueChange = { discountRate = it.toDouble() },
//                        enabled = onSale,
//                    )
    }
}
