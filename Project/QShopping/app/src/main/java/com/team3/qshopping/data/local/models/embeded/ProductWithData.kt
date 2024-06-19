package com.team3.qshopping.data.local.models.embeded

import androidx.room.Embedded
import androidx.room.Relation
import com.team3.qshopping.data.local.models.Product
import com.team3.qshopping.data.local.models.Review

data class ProductWithData(
    @Embedded val product: Product,
    @Relation(
        parentColumn = "id",
        entityColumn = "product_id"
    )
    val reviews: List<Review>,
    val review_count: Int,
    val review_score: Double,
    val is_in_cart: Boolean,
    val is_in_wish_list: Boolean,
)