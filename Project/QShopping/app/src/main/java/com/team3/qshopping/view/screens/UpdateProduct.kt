package com.team3.qshopping.view.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.team3.qshopping.R
import com.team3.qshopping.view.composables.buttons.BackButton
import com.team3.qshopping.view.composables.buttons.GradientButton
import com.team3.qshopping.view.composables.layout.Header
import com.team3.qshopping.viewmodel.AddUpdateProductViewModel
import com.team3.qshopping.viewmodel.customViewModelFactory

@Composable
fun UpdateProduct(
    navController: NavController,
    productId: Int
) {
    val updateProductViewModel: AddUpdateProductViewModel =
        viewModel(factory = customViewModelFactory {
            AddUpdateProductViewModel(productId = productId)
        })

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
                        Column(
                            modifier = Modifier.clickable { },
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Icon(
                                painter = painterResource(R.drawable.ic_pen),
                                contentDescription = "Upload image",
                                tint = Color.Gray,
                                modifier = Modifier.size(40.dp)
                            )
                            Text(
                                text = "Add Image",
                                style = MaterialTheme.typography.h3
                            )
                        }
                    }
                )
                Spacer(modifier = Modifier.height(48.dp))
                ProductForm(updateProductViewModel)
                Spacer(modifier = Modifier.height(24.dp))
                GradientButton(text = "Update Product") {
                    updateProductViewModel.onUpdateButtonClicked()
                    navController.popBackStack()
                }
                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }
}