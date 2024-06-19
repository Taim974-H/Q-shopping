package com.team3.qshopping.view.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.team3.qshopping.R
import com.team3.qshopping.data.remote.models.AnnotatedProduct
import com.team3.qshopping.ui.theme.LightGrey
import com.team3.qshopping.ui.theme.SubtitleTextColor
import com.team3.qshopping.view.LoadingSpinner
import com.team3.qshopping.view.composables.FilterPopup
import com.team3.qshopping.view.composables.ProductCard
import com.team3.qshopping.view.composables.buttons.FilterButton
import com.team3.qshopping.view.composables.buttons.TopNavButton
import com.team3.qshopping.view.composables.inputFields.SearchBar
import com.team3.qshopping.view.composables.layout.CollapsingHeader
import com.team3.qshopping.view.composables.layout.Section
import com.team3.qshopping.view.conditional
import com.team3.qshopping.view.nav.Category
import com.team3.qshopping.view.nav.Screen
import com.team3.qshopping.view.nav.categories
import com.team3.qshopping.viewmodel.HomeViewModel
import com.team3.qshopping.viewmodel.customViewModelFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun HomeScreen(
    navController: NavController,
    scaffoldState: ScaffoldState,
    scaffoldScope: CoroutineScope,
) {
    val homeViewModel: HomeViewModel = viewModel(factory = customViewModelFactory {
        HomeViewModel()
    })

    var searchText by homeViewModel.searchText
    val trendingProducts by homeViewModel.trendingProducts
    val onSaleProducts by homeViewModel.onSaleProducts
    val newlyAddedProducts by homeViewModel.newlyAddedProducts
    var queryProducts by homeViewModel.searchProducts

    var filterPopupOpen by homeViewModel.filterPopupOpen
    val productFilters = homeViewModel.productFilters
    Surface {
        Box(
            modifier = Modifier
                .fillMaxSize()
        ) {
            FilterPopup(
                setOpened = { filterPopupOpen = it },
                opened = filterPopupOpen,
                categories = homeViewModel.categories.value,
                initRating = productFilters.value.selectedRating,
                initPriceRange = productFilters.value.selectedPriceRange,
                apply = { cat, rat, ran ->
                    productFilters.value = productFilters.value.copy(
                        selectedCategory = cat,
                        selectedRating = rat,
                        selectedPriceRange = ran
                    )
                }
            )
            CollapsingHeader(
                start = {
                    MenuButton {
                        scaffoldScope.launch { scaffoldState.drawerState.open() }
                    }
                }, middle = {
                    Image(
                        painter = painterResource(R.drawable.ic_logo),
                        contentDescription = "Q Shopping Logo",
                        modifier = Modifier.size(100.dp, 100.dp)
                    )
                }, end = {
                    FilterButton(
                        onClick = {
                            filterPopupOpen = true
                        }
                    )
                },
                body = {
                    val scrollState = rememberScrollState()
                    Column(modifier = Modifier
                        .fillMaxSize()
                        .conditional(searchText.isBlank()) {
                            verticalScroll(scrollState)
                        }) {

                        Column {
                            Text(text = "Hello!", style = MaterialTheme.typography.h2)
                            Text(
                                text = "Welcome to Q Shopping.",
                                style = MaterialTheme.typography.subtitle2,
                                color = SubtitleTextColor
                            )
                        }
                        Spacer(modifier = Modifier.height(10.dp))
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(
                                    top = 15.dp, bottom = 15.dp
                                )
                        ) {
                            SearchBar(value = searchText, onValueChange = { search ->
                                searchText = search
                                homeViewModel.searchProducts(search)
                            })
                        }
                        Section(title = "Choose Category") {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(10.dp),
                            ) {
                                categories.forEach { category ->
                                    CategoryButton(
                                        category = category, modifier = Modifier.weight(1f)
                                    ) {
                                        navController.navigate("${Screen.Category.route}/${category.id}")
                                    }
                                }
                            }
                        }
                        Spacer(modifier = Modifier.height(20.dp))
                        if (searchText.isBlank()) {
                            Column(verticalArrangement = Arrangement.spacedBy(20.dp)) {
                                ProductCarousel(
                                    "Trending",
                                    homeViewModel.filterProducts(trendingProducts),
                                    navController
                                )
                                ProductCarousel(
                                    "On Sale!",
                                    homeViewModel.filterProducts(onSaleProducts),
                                    navController
                                )
                                ProductCarousel(
                                    "New Additions",
                                    homeViewModel.filterProducts(newlyAddedProducts),
                                    navController
                                )
                            }
                        } else {
                            LazyVerticalGrid(columns = GridCells.Adaptive(minSize = 128.dp)) {
                                items(queryProducts) { product ->
                                    ProductCard(
                                        product,
                                        navController
                                    )
                                }

                            }
                        }
                        Spacer(modifier = Modifier.height(20.dp))
                    }
                })
        }
    }
}

@Composable
fun MenuButton(onNavigate: () -> Unit) {
    TopNavButton(onPress = { onNavigate() },
        icon = {
            Image(
                painter = painterResource(id = R.drawable.ic_menu), contentDescription = "menu"
            )
        })
}

@Composable
fun CategoryButton(
    category: Category, modifier: Modifier, onClick: () -> Unit
) {
    IconButton(
        onClick = onClick, modifier = Modifier
            .height(42.dp)
            .background(
                color = LightGrey, shape = RoundedCornerShape(10.dp)
            )
            .then(modifier)
    ) {
        Image(
            painter = painterResource(id = category.icon), contentDescription = category.name
        )
    }
}

@Composable
private fun ProductCarousel(
    title: String,
    products: List<AnnotatedProduct>?,
    navController: NavController
) {
    if (products == null) {
        Section(title = title) { LoadingSpinner() }
    } else if (products.isNotEmpty()) {
        Section(title = title) {
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                items(products) { product ->
                    ProductCard(
                        annotatedProduct = product,
                        navController
                    )
                }
            }
        }
    }
}

