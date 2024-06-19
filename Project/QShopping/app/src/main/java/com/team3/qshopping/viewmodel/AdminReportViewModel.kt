package com.team3.qshopping.viewmodel

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.team3.qshopping.data.remote.models.AnnotatedOrder
import com.team3.qshopping.data.repository.OrderRepository
import com.team3.qshopping.data.repository.OrderStatus
import kotlinx.coroutines.launch
import java.time.LocalDate

class AdminReportViewModel : ViewModel() {
    val startDate: MutableState<LocalDate?> = mutableStateOf(null)
    val endDate: MutableState<LocalDate?> = mutableStateOf(null)
    val orderStatus: MutableState<OrderStatus> = mutableStateOf(OrderStatus.ALL)

    var countByStatus: MutableState<Map<OrderStatus, Int>> =
        mutableStateOf(OrderStatus.values().associateWith { 0 })
    var totalPriceByStatus: MutableState<Map<OrderStatus, Double>> =
        mutableStateOf(OrderStatus.values().associateWith { 0.0 })

    var orders: MutableState<List<AnnotatedOrder>> = mutableStateOf(emptyList())
    var productCount: MutableState<Int> = mutableStateOf(0)
    var totalPrice: MutableState<Double> = mutableStateOf(0.0)

    fun generateReport() {
        viewModelScope.launch {
            orders.value = OrderRepository.getOrdersWithItems(
                startTime = startDate.value?.atStartOfDay(),
                endTime = endDate.value?.atStartOfDay(),
                orderStatus = orderStatus.value
            )
            val quantityByStatus = OrderStatus.values().associateWith { 0 }.toMutableMap()
            val priceByStatus = OrderStatus.values().associateWith { 0.0 }.toMutableMap()
            for (item in orders.value) {
                quantityByStatus.merge(
                    OrderStatus.fromString(item.status), item.itemCount, Int::plus
                )
                priceByStatus.merge(
                    OrderStatus.fromString(item.status), item.totalPrice, Double::plus
                )
            }
            productCount.value = orders.value.sumOf { it.itemCount }
            totalPrice.value = orders.value.sumOf { it.totalPrice }
            countByStatus.value = quantityByStatus
            totalPriceByStatus.value = priceByStatus
        }
    }
}