@file:Suppress("UNCHECKED_CAST")

package com.team3.qshopping.view.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.team3.qshopping.data.remote.models.AnnotatedProduct
import com.team3.qshopping.view.composables.ProductImage
import com.team3.qshopping.view.composables.buttons.BackButton
import com.team3.qshopping.view.composables.buttons.CancelSaveButtonsRow
import com.team3.qshopping.view.composables.layout.Header
import com.team3.qshopping.viewmodel.ProductDetailAdminViewModel
import com.team3.qshopping.viewmodel.customViewModelFactory

@Composable
fun AdminProductDetailScreen(
    navController: NavController,
    productId: Int
) {
    println("************************************ aaaaa $productId")
    val productDetailAdminViewModel: ProductDetailAdminViewModel =
        viewModel(factory = customViewModelFactory {
            ProductDetailAdminViewModel(productId)
        })

    if (productDetailAdminViewModel.product != null) {
        val productWithData: AnnotatedProduct = productDetailAdminViewModel.product!!
        val price = remember { mutableStateOf(productWithData.price) }
        val stock = remember { mutableStateOf(productWithData.stock) }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            Header(
                start = { BackButton { navController.popBackStack() } },
                middle = {
                    ProductReputation(
                        averageRating = productWithData.reviewScore,
                        totalNumberOfRatings = productWithData.reviewCount
                    )
                }
            )

            Spacer(modifier = Modifier.height(10.dp))
            ProductImage(
                productImageUrl = productWithData.image,
                productName = productWithData.title
            )
            Spacer(modifier = Modifier.height(20.dp))

            HeaderXTextField(
                header = "Price",
                statefulVariable = price,
                leadingIcon = { Text("$") }
            )
            Spacer(modifier = Modifier.height(10.dp))
            HeaderXTextField(header = "Quantity", statefulVariable = stock)

            Spacer(modifier = Modifier.height(20.dp))
            CancelSaveButtonsRow(
                onCancel = { navController.popBackStack() },
                onSave = {
                    //productDetailAdminViewModel.updateProduct(price.value, stock.value)
                    navController.popBackStack()
                },
                horizontalArrangement = Arrangement.spacedBy(20.dp),
                verticalAlignment = Alignment.Bottom,
                buttonWidth = 170.dp
            )
        }
    }
}

@Composable
fun <T> HeaderXTextField(
    header: String,
    leadingIcon: @Composable (() -> Unit)? = null,
    statefulVariable: MutableState<T>
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Text(
            text = header,
            style = MaterialTheme.typography.h2,
            fontWeight = FontWeight.Normal,
            fontSize = 20.sp
        )
        TextField(
            value = statefulVariable.value.toString(),
            onValueChange = { statefulVariable.value = it as T },
            placeholder = { Text(statefulVariable.value.toString()) },
            modifier = Modifier.fillMaxWidth(),
            colors = TextFieldDefaults.textFieldColors(
                backgroundColor = Color(0xFFF5F6FA),
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                disabledIndicatorColor = Color.Transparent
            ),
            shape = RoundedCornerShape(12.dp),
            leadingIcon = leadingIcon,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
        )
    }
}