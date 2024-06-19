package com.team3.qshopping.view.composables

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.team3.qshopping.R
import com.team3.qshopping.data.remote.models.AnnotatedProduct
import com.team3.qshopping.view.CustomAsyncImage
import com.team3.qshopping.viewmodel.ProductCardViewModel
import com.team3.qshopping.viewmodel.customViewModelFactory


@Composable
fun ProductCard(
    annotatedProduct: AnnotatedProduct,
    navController: NavController,
) {
    val productCardViewModel: ProductCardViewModel = viewModel(factory = customViewModelFactory {
        ProductCardViewModel(annotatedProduct)
    }, key = annotatedProduct.id.toString())
    val product by productCardViewModel.product
    Column(
        modifier = Modifier
            .width(190.dp)
            .clickable {
                navController.navigate(route = "product/${product.id}")
            },
        verticalArrangement = Arrangement.spacedBy(0.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TopButtons(
            inCart = product.isInCart,
            inWishList = product.isInWishList,
            onCartButtonClicked = { productCardViewModel.onCartButtonClicked() },
            onWishListButtonClicked = { productCardViewModel.onWishListButtonClicked() },
        )
        ImageAndTitle(product.image, product.title)
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Price(product.price, product.discountRate)
            Ratings(product.reviewCount, product.reviewScore)
        }
    }
}

@Composable
private fun TopButtons(
    inCart: Boolean,
    inWishList: Boolean,
    onCartButtonClicked: () -> Unit,
    onWishListButtonClicked: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .zIndex(1f),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        IconButton(
            modifier = Modifier.size(24.dp),
            onClick = onCartButtonClicked
        ) {
            Image(
                painter = painterResource(id = if (inCart) R.drawable.ic_shopping_bag_filled else R.drawable.ic_shopping_bag),
                contentDescription = "Add to cart",
            )
        }
        IconButton(modifier = Modifier.size(24.dp), onClick = onWishListButtonClicked) {
            Image(
                painter = painterResource(id = if (inWishList) R.drawable.ic_heart_filled else R.drawable.ic_heart),
                contentDescription = "Add to wishlist"
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


@Composable
private fun Ratings(count: Int, score: Double) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Image(painter = painterResource(id = R.drawable.ic_star), contentDescription = "Star")
        Text(
            "${"%.1f".format(score)} (${count})",
            color = Color(0xFFA6A8AA),
            style = MaterialTheme.typography.h4
        )
    }
}
