package com.team3.qshopping.data.local.models.embeded

import androidx.room.Embedded
import androidx.room.Relation
import com.team3.qshopping.data.local.models.CartItem
import com.team3.qshopping.data.local.models.Product

data class CartItemWithProduct(
    @Embedded val cartItem: CartItem,
    @Relation(parentColumn = "product_id", entityColumn = "id")
    val product: Product
)