package com.team3.qshopping.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.team3.qshopping.Globals
import com.team3.qshopping.data.remote.models.AnnotatedProduct
import com.team3.qshopping.data.repository.OrderItemRepository
import com.team3.qshopping.data.repository.ProductRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ProductDetailViewModel(
    productId: Int,
) :
    ViewModel() {
    var product = mutableStateOf<AnnotatedProduct?>(null)
        private set

    fun canReview(productId: Int, userId: Int, callback: (Boolean) -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            val status = OrderItemRepository.findOrderItem(productId, userId)
            println(status)
            val ownsProduct = status == "Delivered"
            viewModelScope.launch(Dispatchers.Main) {
                callback(ownsProduct)
            }
        }
    }

    init {
        viewModelScope.launch(Dispatchers.IO) {
            product.value = ProductRepository.getAnnotatedProductById(Globals.user!!.id, productId)
        }
    }
}