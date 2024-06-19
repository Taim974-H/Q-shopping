package com.team3.qshopping.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.ListenerRegistration
import com.team3.qshopping.data.local.models.Category
import com.team3.qshopping.data.remote.models.RemoteProduct
import com.team3.qshopping.data.repository.CategoryRepository
import com.team3.qshopping.data.repository.ProductRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.ZoneOffset

class AddUpdateProductViewModel(
    val productId: Int? = null
) : ViewModel() {
    lateinit var product: RemoteProduct
    val categoryId = mutableStateOf(1)
    val image = mutableStateOf("")
    var isOnSale = mutableStateOf(false)
    var title = mutableStateOf("")
    var description = mutableStateOf("")
    var stock = mutableStateOf(1)
    var unitPrice = mutableStateOf("")
    val discountRate = mutableStateOf(0.0)

    var categories: List<Category> by mutableStateOf(emptyList())
        private set
    private var categoriesListener: ListenerRegistration? = null

    init {
        categoriesListener = CategoryRepository.collection.addSnapshotListener { _, error ->
            if (error != null) {
                return@addSnapshotListener
            } else {
                setCategories()
            }
        }

        if (productId != null)
            viewModelScope.launch {
                product = ProductRepository.getProductById(productId)
                title.value = product.title
                image.value = product.image
                description.value = product.description
                unitPrice.value = product.price.toString()
                stock.value = product.stock
                discountRate.value = product.discountRate
                categoryId.value = product.categoryId
            }
    }

    private fun setCategories() {
        viewModelScope.launch(Dispatchers.IO) {
            categories = CategoryRepository.getAll()
        }
    }

    fun onAddButtonClicked() {
        viewModelScope.launch(Dispatchers.IO) {
            // TODO validate
            ProductRepository.insertProduct(
                title = title.value,
                image = "",
                description = description.value,
                unitPrice = unitPrice.value.toDouble(),
                stock = stock.value,
                discountRate = discountRate.value,
                dateAdded = LocalDateTime.now(ZoneOffset.UTC),
                categoryId = categoryId.value
            )
        }
    }

    fun onUpdateButtonClicked() {
        viewModelScope.launch(Dispatchers.IO) {
            // TODO validate
            //val product = ProductRepository.getProductById(productId)
            product.title = title.value
            product.description = title.value
            product.description = description.value
            product.price = unitPrice.value.toDouble()
            product.stock = stock.value
            product.discountRate = discountRate.value
            product.categoryId = categoryId.value
            ProductRepository.updateProduct(product)
        }
    }

}