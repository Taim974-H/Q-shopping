package com.team3.qshopping.view.composables

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.team3.qshopping.R
import com.team3.qshopping.data.remote.models.AnnotatedProduct
import com.team3.qshopping.view.CustomAsyncImage
import com.team3.qshopping.viewmodel.ProductCardViewModel
import com.team3.qshopping.viewmodel.customViewModelFactory


@Composable
fun ProductAdminCard(
    productWithData: AnnotatedProduct,
    navController: NavController,
) {
    val productCardViewModel: ProductCardViewModel = viewModel(factory = customViewModelFactory {
        ProductCardViewModel(productWithData)
    })
    val showDeleteConfirm = remember { mutableStateOf(false) }
    Column(
        modifier = Modifier
            .width(190.dp)
            .clickable {
                navController.navigate(route = "productAdmin/${productWithData.id}")
            },
        verticalArrangement = Arrangement.spacedBy(0.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TopButtons(onEdit = {
            //TODO allow parameterization in Screens.Route.route and replace literal
            navController.navigate("updateProduct/${productWithData.id}")
        }, onDelete = {
            showDeleteConfirm.value = true
        })
        ImageAndTitle(productWithData.image, productWithData.title)
        Price(productWithData.price, productWithData.discountRate)
    }
    ConfirmDialogue(shown = showDeleteConfirm, onYesClicked = {
        productCardViewModel.onDeleteButtonClicked(productWithData)
        showDeleteConfirm.value = false
    })
}

@Composable
private fun TopButtons(
    onEdit: () -> Unit,
    onDelete: () -> Unit,
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        IconButton(
            modifier = Modifier
                .size(34.dp)
                .border(
                    1.dp,
                    Color(0xFFDEDEDE),
                    RoundedCornerShape(50)
                )
                .padding(8.dp),
            onClick = onDelete
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_delete),
                contentDescription = "Update product",
                tint = Color.Red,
            )
        }
        IconButton(
            modifier = Modifier
                .size(34.dp)
                .border(
                    1.dp,
                    Color(0xFFDEDEDE),
                    RoundedCornerShape(50)
                )
                .padding(8.dp),
            onClick = onEdit
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_pen),
                contentDescription = "Update product",
            )
        }
    }
}


@Composable
private fun ImageAndTitle(image: String, title: String) {
    CustomAsyncImage(
        model = image,
        contentDescription = "An image of the product",
        modifier = Modifier.height(200.dp)
    )
    Spacer(modifier = Modifier.height(10.dp))
    Text(
        text = title,
        fontWeight = FontWeight.Bold,
        style = MaterialTheme.typography.h4,
        maxLines = 2,
        overflow = TextOverflow.Ellipsis
    )
}

@Composable
private fun Price(unitPrice: Double, discountRate: Double) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Start
    ) {
        if (discountRate > 0) {
            Column {
                val discountedPrice = unitPrice - (unitPrice * discountRate)
                Text(
                    text = "$${"%,.2f".format(unitPrice)}",
                    fontWeight = FontWeight.Bold,
                    textDecoration = TextDecoration.LineThrough,
                    color = Color.Red
                )
                Text(text = "$${"%,.2f".format(discountedPrice)}", fontWeight = FontWeight.Bold)
            }
        } else Text(
            text = "$${unitPrice}",
            fontWeight = FontWeight.Bold
        )
    }
}
