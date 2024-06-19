package com.team3.qshopping.data.remote.models


data class CartItem(
    var id: Int = 0,
    var quantity: Int,
    val userId: Int,
    val productId: Int,
) {
    constructor() : this(id = 0, quantity = 0, userId = 0, productId = 0)
}