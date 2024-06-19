package com.team3.qshopping.viewmodel

import androidx.lifecycle.ViewModel
import com.team3.qshopping.Globals
import com.team3.qshopping.data.local.models.Order
import com.team3.qshopping.data.repository.OrderRepository

class OrderTrackViewModel : ViewModel() {

    suspend fun getUserOrders(): List<Order> {
        return OrderRepository.getAllByUserId(Globals.user!!.id)
    }
}