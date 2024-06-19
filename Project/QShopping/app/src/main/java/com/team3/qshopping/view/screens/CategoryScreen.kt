package com.team3.qshopping.view.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.team3.qshopping.data.local.models.Category
import com.team3.qshopping.ui.theme.ButtonTextColor
import com.team3.qshopping.view.CustomAsyncImage
import com.team3.qshopping.view.composables.FilterPopup
import com.team3.qshopping.view.composables.ProductCard
import com.team3.qshopping.view.composables.buttons.BackButton
import com.team3.qshopping.view.composables.buttons.FilterButton
import com.team3.qshopping.view.composables.inputFields.SearchBar
import com.team3.qshopping.view.composables.layout.Header
import com.team3.qshopping.viewmodel.CategoryViewModel
import com.team3.qshopping.viewmodel.customViewModelFactory

@Composable
fun CategoryScreen(
    navController: NavController,
    categoryId: Int
) {
    val categoryViewModel: CategoryViewModel = viewModel(factory = customViewModelFactory {
        CategoryViewModel(categoryId = categoryId)
    })
    var value by remember { mutableStateOf("") }
    val setValue: (String) -> Unit = { value = it }
    val category by categoryViewModel.category

    var filterOpened by categoryViewModel.filterPopupOpen
    val productFilters = categoryViewModel.productFilters

    Surface {
        FilterPopup(
            setOpened = { filterOpened = it },
            opened = filterOpened,
            categoryEnabled = false,
            initRating = productFilters.value.selectedRating,
            initPriceRange = productFilters.value.selectedPriceRange,
            apply = { _, rat, ran ->
                productFilters.value = productFilters.value.copy(
                    selectedRating = rat,
                    selectedPriceRange = ran
                )
            },
        )
        Column(
            modifier = Modifier
                .fillMaxSize()
        ) {
            if (category != null) {
                CategoryHeader(
                    category = category!!,
                    setfilterOpened = { filterOpened = it }
                ) { navController.popBackStack() }
            }
            Spacer(modifier = Modifier.height(10.dp))
            Box(
                modifier = Modifier.padding(horizontal = 10.dp, vertical = 25.dp)
            ) {
                SearchBar(
                    value = value,
                    onValueChange = setValue
                )
            }

            LazyVerticalGrid(
                modifier = Modifier.padding(horizontal = 10.dp),
                columns = GridCells.Adaptive(minSize = 128.dp),
                verticalArrangement = Arrangement.spacedBy(15.dp),
                horizontalArrangement = Arrangement.spacedBy(15.dp)
            ) {
                items(categoryViewModel.filterProducts()) { product ->
//                items(categoryProducts) { product ->
                    ProductCard(
                        annotatedProduct = product,
                        navController = navController
                    )
                }
            }
        }
    }
}

@Composable
fun CategoryHeader(
    category: Category,
    setfilterOpened: (Boolean) -> Unit,
    onBackPress: () -> Unit
) {
    Header(
        start = { BackButton { onBackPress() } },
        middle = {
            Column {
                CustomAsyncImage(
                    model = category.icon,
                    contentDescription = "Category",
                    modifier = Modifier
                        .size(50.dp)
                        .align(Alignment.CenterHorizontally)
                )
                Text(
                    text = category.name,
                    color = ButtonTextColor,
                    fontWeight = FontWeight.SemiBold,
                    style = MaterialTheme.typography.h6,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
            }
        },
        end = {
            FilterButton { setfilterOpened(true) }
        }
    )
}
