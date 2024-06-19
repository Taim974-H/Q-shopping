package com.team3.qshopping.view.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.Scaffold
import androidx.compose.material.ScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.team3.qshopping.R
import com.team3.qshopping.data.remote.models.AnnotatedProduct
import com.team3.qshopping.view.composables.ProductAdminCard
import com.team3.qshopping.view.composables.layout.Header
import com.team3.qshopping.view.composables.layout.Section
import com.team3.qshopping.view.nav.Screen
import com.team3.qshopping.viewmodel.HomeAdminViewModel
import com.team3.qshopping.viewmodel.customViewModelFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun HomeScreenAdmin(
    navController: NavController,
    scaffoldState: ScaffoldState,
    scaffoldScope: CoroutineScope,
) {
    val homeAdminViewModel: HomeAdminViewModel = viewModel(factory = customViewModelFactory {
        HomeAdminViewModel()
    })
    val productsByCategory = homeAdminViewModel.uiState.productsByCategory
    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = {
                navController.navigate(Screen.AddProduct.route)
            }) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_plus),
                    contentDescription = "Add product"
                )
            }
            Spacer(modifier = Modifier.height(20.dp))
        },
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            Header(start = {
                MenuButton {
                    scaffoldScope.launch { scaffoldState.drawerState.open() }
                }
            }, middle = {
                Image(
                    painter = painterResource(R.drawable.ic_logo),
                    contentDescription = "Q Shopping Logo",
                    modifier = Modifier.size(100.dp, 100.dp)
                )
            })

            Spacer(modifier = Modifier.height(20.dp))
            Column(verticalArrangement = Arrangement.spacedBy(20.dp)) {
                productsByCategory.entries.forEach { (category, categoryProducts) ->
                    ProductCarousel(category.name, categoryProducts, navController)
                }
            }
            Spacer(modifier = Modifier.height(20.dp))
        }

    }
}

@Composable
private fun ProductCarousel(
    title: String, products: List<AnnotatedProduct>, navController: NavController
) {
    Section(title = title) {
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            items(products) { product ->
                ProductAdminCard(
                    productWithData = product,
                    navController = navController,
                )
            }
        }
    }
}
