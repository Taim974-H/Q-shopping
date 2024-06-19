package com.team3.qshopping.data.remote.models

import com.team3.qshopping.data.local.models.OrderItem
import java.time.LocalDateTime

class AnnotatedOrder(
    var id: Int = 0,
    var status: String,
    val timestamp: LocalDateTime,
    var statusDate: LocalDateTime?,
    val userId: Int,
    val addressLine: String,
    val city: String,
    val country: String,
    val poBox: Int,
    val phoneNumber: Long,
    val nameOnCard: String,
    val cardNumber: Long,
    val cvv: Int,
    val expiryYear: Int,
    val expiryMonth: Int,

    val items: List<OrderItem>,
    val products: List<RemoteProduct>,
    val itemCount: Int,
    val totalPrice: Double,
) {
    constructor(
        order: Order,
        items: List<OrderItem>,
        products: List<RemoteProduct>,
        itemCount: Int,
        totalPrice: Double,
    ) : this(
        order.id,
        order.status,
        LocalDateTime.parse(order.timestamp),
        LocalDateTime.parse(order.statusDate),
        order.userId,
        order.addressLine,
        order.city,
        order.country,
        order.poBox,
        order.phoneNumber,
        order.nameOnCard,
        order.cardNumber,
        order.cvv,
        order.expiryYear,
        order.expiryMonth,
        items,
        products,
        itemCount,
        totalPrice
    )
}