package com.team3.qshopping.data.local.models

import androidx.room.*
import kotlinx.serialization.Serializable

@Entity(
    tableName = "cart_item",
    foreignKeys = [
        ForeignKey(
            entity = User::class,
            parentColumns = arrayOf("id"),
            childColumns = arrayOf("user_id"),
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = Product::class,
            parentColumns = arrayOf("id"),
            childColumns = arrayOf("product_id"),
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index(value = ["user_id", "product_id"], unique = true)]
)
@Serializable
data class CartItem(
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0,
    @ColumnInfo(name = "quantity") var quantity: Int,
    @ColumnInfo(name = "user_id") val userId: Int,
    @ColumnInfo(name = "product_id") val productId: Int,
) {
    constructor() : this(id = 0, quantity = 0, userId = 0, productId = 0)
}