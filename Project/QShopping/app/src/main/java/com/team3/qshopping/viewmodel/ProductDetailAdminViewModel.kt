package com.team3.qshopping.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.team3.qshopping.Globals
import com.team3.qshopping.data.remote.models.AnnotatedProduct
import com.team3.qshopping.data.remote.models.RemoteProduct
import com.team3.qshopping.data.repository.ProductRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ProductDetailAdminViewModel(
    productId: Int,
) :
    ViewModel() {
    var product: AnnotatedProduct? by mutableStateOf(null)
        private set

    init {
        viewModelScope.launch(Dispatchers.IO) {
            product = ProductRepository.getAnnotatedProductById(Globals.user!!.id, productId)
        }
    }

    fun updateProduct(price: Double, stock: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            if (product != null) {
                ProductRepository.updateProduct(
                    RemoteProduct(
                        id = product!!.id,
                        title = product!!.title,
                        description = product!!.description,
                        image = product!!.image,
                        price = price,
                        stock = stock,
                        discountRate = product!!.discountRate,
                        dateAdded = product!!.dateAdded,
                        categoryId = product!!.categoryId

                    )
                )
            }
        }
    }
}