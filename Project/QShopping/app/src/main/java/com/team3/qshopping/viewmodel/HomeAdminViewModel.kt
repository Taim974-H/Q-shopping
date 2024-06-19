package com.team3.qshopping.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
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

class HomeAdminViewModel : ViewModel() {
    var uiState by mutableStateOf(HomeAdminState())
        private set
    private var categoriesListener: ListenerRegistration? = null
    private var productsListener: ListenerRegistration? = null

    init {
        categoriesListener = CategoryRepository.collection.addSnapshotListener { _, error ->
            if (error != null) {
                return@addSnapshotListener
            } else {
                setCategories()
            }
        }

        productsListener = ProductRepository.collection.addSnapshotListener { _, error ->
            if (error != null) {
                return@addSnapshotListener
            } else {
                setCategories()
            }
        }

        /*viewModelScope.launch {
            getAllCategories().collectLatest { categories ->
                categories.forEach { category ->
                    productsByCategory.value[category] = ProductRepository.getProductByCategory(
                        category.id,
                        userId = Globals.user!!.id
                    )
                }
            }
        }*/
    }

    private fun setCategories() {
        viewModelScope.launch(Dispatchers.IO) {
            val productsByCategory: MutableMap<Category, List<AnnotatedProduct>> = mutableMapOf()
            val categories = CategoryRepository.getAll()

            categories.forEach {
                productsByCategory[it] = ProductRepository.getProductByCategory(
                    categoryId = it.id,
                    userId = Globals.user!!.id
                )
            }

            uiState = uiState.copy(productsByCategory = productsByCategory)
        }
    }

    override fun onCleared() {
        println("*******************************")
        println("clearing categories listener")
        super.onCleared()
        categoriesListener?.remove()
        productsListener?.remove()
    }
}

data class HomeAdminState(
    val productsByCategory: Map<Category, List<AnnotatedProduct>> = mapOf()
)