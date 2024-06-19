package com.team3.qshopping.viewmodel

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.team3.qshopping.Globals
import com.team3.qshopping.data.local.models.Category
import com.team3.qshopping.data.remote.models.AnnotatedProduct
import com.team3.qshopping.data.repository.CategoryRepository
import com.team3.qshopping.data.repository.ProductRepository
import kotlinx.coroutines.launch

data class ProductFilters(
    var selectedCategory: Category?,
    var selectedRating: Int,
    var selectedPriceRange: ClosedFloatingPointRange<Float>,
)


class HomeViewModel : ViewModel() {
    var searchText = mutableStateOf("")

    var trendingProducts: MutableState<List<AnnotatedProduct>?> = mutableStateOf(null)
    var onSaleProducts: MutableState<List<AnnotatedProduct>?> = mutableStateOf(null)
    var newlyAddedProducts: MutableState<List<AnnotatedProduct>?> = mutableStateOf(null)
    var searchProducts: MutableState<List<AnnotatedProduct>> = mutableStateOf(emptyList())
    var categories: MutableState<List<Category>> = mutableStateOf(emptyList())
        private set

    var filterPopupOpen: MutableState<Boolean> = mutableStateOf(false)
    val productFilters: MutableState<ProductFilters> =
        mutableStateOf(ProductFilters(null, 0, 1f..1000f))


    init {
        viewModelScope.launch {
            categories.value = CategoryRepository.getAll()
            onSaleProducts.value = ProductRepository.getOnSaleProducts(userId = Globals.user!!.id)
            trendingProducts.value =
                ProductRepository.getTrendingProducts(userId = Globals.user!!.id)
            newlyAddedProducts.value =
                ProductRepository.getNewlyAddedProducts(userId = Globals.user!!.id)
//            snapshotFlow { searchText.value }.onEach {
            searchProducts.value =
                ProductRepository.searchProducts(searchText.value, Globals.user!!.id)
//            }
        }
    }

    fun filterProducts(
        productsList: List<AnnotatedProduct>?
    ): List<AnnotatedProduct>? {
        if (productsList == null)
            return null
        val (category, rating, priceRange) = productFilters.value
        return ProductRepository.filterProducts(productsList, category, rating, priceRange)
    }

    fun searchProducts(
        query: String
    ) {
        viewModelScope.launch {
            val result = ProductRepository.searchProducts(query, Globals.user!!.id)
            searchProducts.value = result
        }
    }
}
