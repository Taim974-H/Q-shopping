package com.team3.qshopping.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.ListenerRegistration
import com.team3.qshopping.Globals
import com.team3.qshopping.data.local.models.Order
import com.team3.qshopping.data.local.models.OrderItem
import com.team3.qshopping.data.remote.models.RemoteProduct
import com.team3.qshopping.data.repository.OrderItemRepository
import com.team3.qshopping.data.repository.OrderRepository
import com.team3.qshopping.data.repository.ProductRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class OrderViewModel : ViewModel() {
    var orders: List<Order> by mutableStateOf(emptyList())
    private var orderListener: ListenerRegistration? = null

    fun getOrderItems(orderId: Int, setOrderItems: (List<OrderItem>) -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            val orderItems = OrderItemRepository.getAllByOrderId(orderId)
            viewModelScope.launch(Dispatchers.Main) {
                setOrderItems(orderItems)
            }
        }
    }

    fun getProductById(productId: Int, setProduct: (RemoteProduct) -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            val product = ProductRepository.getProductById(productId)
            viewModelScope.launch(Dispatchers.Main) {
                setProduct(product)
            }
        }
    }

    fun updateOrder(order: Order) {
        viewModelScope.launch(Dispatchers.IO) {
            OrderRepository.update(order)
        }
    }

    init {
        val orderCollectionRef = if (Globals.user!!.admin) {
            OrderRepository.collection
        } else {
            OrderRepository.collection.whereEqualTo("userId", Globals.user!!.id)
        }

        orderListener = orderCollectionRef.addSnapshotListener { _, error ->
            if (error != null) {
                return@addSnapshotListener
            } else {
                getOrders()
            }
        }
    }

    private fun getOrders() {
        viewModelScope.launch {
            orders = if (Globals.user!!.admin) {
                OrderRepository.getAll()
            } else {
                OrderRepository.getAllByUserId(Globals.user!!.id)
            }
        }
    }

    override fun onCleared() {
        println("*******************************")
        println("clearing orders listener")
        super.onCleared()
        orderListener?.remove()
    }
}