package com.team3.qshopping.data.local.models.embeded

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import com.team3.qshopping.data.local.models.Order
import com.team3.qshopping.data.local.models.OrderItem
import com.team3.qshopping.data.local.models.Product

data class OrderWithItems(
    @Embedded val order: Order,
    @Relation(
        entity = OrderItem::class,
        parentColumn = "id",
        entityColumn = "order_id",
    )
    val items: List<OrderItem>,

    @Relation(
        parentColumn = "id",
        entityColumn = "id",
        associateBy = Junction(
            OrderItem::class,
            parentColumn = "order_id",
            entityColumn = "product_id"
        )
    )
    val products: List<Product>,


    val item_count: Int,
    val total_price: Double,
)
