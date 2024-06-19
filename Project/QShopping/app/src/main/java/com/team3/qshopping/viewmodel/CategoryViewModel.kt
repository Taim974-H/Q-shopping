package com.team3.qshopping.viewmodel

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.ListenerRegistration
import com.team3.qshopping.Globals
import com.team3.qshopping.data.local.models.Category
import com.team3.qshopping.data.remote.models.AnnotatedProduct
import com.team3.qshopping.data.repository.CategoryRepository
import com.team3.qshopping.data.repository.ProductRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class CategoryViewModel(categoryId: Int) : ViewModel() {
    var category: MutableState<Category?> = mutableStateOf(null)
    var categoryProducts: MutableState<List<AnnotatedProduct>> = mutableStateOf(emptyList())
    private var productsListener: ListenerRegistration? = null

    var filterPopupOpen: MutableState<Boolean> = mutableStateOf(false)
    val productFilters: MutableState<ProductFilters> =
        mutableStateOf(ProductFilters(null, 0, 1f..1000f))

    init {
        viewModelScope.launch {
            category.value = CategoryRepository.getCategory(categoryId)
        }

        productsListener = CategoryRepository.collection.addSnapshotListener { _, error ->
            if (error != null) {
                return@addSnapshotListener
            } else {
                setProducts(categoryId)
            }
        }
    }

    private fun setProducts(categoryId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            categoryProducts.value = ProductRepository.getProductByCategory(
                categoryId,
                userId = Globals.user!!.id
            )
        }
    }

    override fun onCleared() {
        println("*******************************")
        println("clearing products listener")
        super.onCleared()
        productsListener?.remove()
    }

    fun filterProducts(): List<AnnotatedProduct> {
        val (_, rating, priceRange) = productFilters.value
        return ProductRepository.filterProducts(
            categoryProducts.value,
            category.value,
            rating,
            priceRange
        )
    }
}