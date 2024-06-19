package com.team3.qshopping.view.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.team3.qshopping.R
import com.team3.qshopping.ui.theme.ButtonTextColor
import com.team3.qshopping.view.composables.ProductCard
import com.team3.qshopping.view.composables.buttons.BackButton
import com.team3.qshopping.view.composables.layout.Header
import com.team3.qshopping.viewmodel.WishListViewModel


@Composable
fun WishlistScreen(
    navController: NavController,
) {
    val wishListViewModel = WishListViewModel()

    Wishlist(navController = navController, wishListViewModel = wishListViewModel)
}

@Composable
fun EmptyWishListScreen() {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "-- No items in wishlist --",
            style = MaterialTheme.typography.h2,
            color = Color.DarkGray
        )
    }
}

@Composable
fun Wishlist(
    navController: NavController,
    wishListViewModel: WishListViewModel
) {
    val wishListProducts = wishListViewModel.wishListProducts
    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Header(
            start = { BackButton { navController.popBackStack() } },
            middle = {
                Column {
                    Image(
                        painter = painterResource(R.drawable.ic_heart),
                        contentDescription = "Wish List Icon",
                        modifier = Modifier
                            .size(50.dp)
                            .align(Alignment.CenterHorizontally)
                    )
                    Text(
                        text = "Wish List",
                        color = ButtonTextColor,
                        fontWeight = FontWeight.SemiBold,
                        style = MaterialTheme.typography.h6,
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    )
                }
            },
        )


        Spacer(modifier = Modifier.height(15.dp))
        if (wishListProducts.isEmpty()) {
            EmptyWishListScreen()
        } else {
            LazyVerticalGrid(
                modifier = Modifier.padding(horizontal = 10.dp),
                columns = GridCells.Adaptive(minSize = 128.dp),
                verticalArrangement = Arrangement.spacedBy(15.dp),
                horizontalArrangement = Arrangement.spacedBy(15.dp)
            ) {
                items(wishListProducts) { product ->
                    ProductCard(
                        annotatedProduct = product,
                        navController
                    )
                }
            }
        }
    }
}

