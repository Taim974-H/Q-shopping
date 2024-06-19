package com.team3.qshopping.data.local.models

import androidx.room.*
import kotlinx.serialization.Serializable

@Entity(
    tableName = "wish_list_item",
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
data class WishListItem(
    @ColumnInfo(name = "user_id") val userId: Int,
    @ColumnInfo(name = "product_id") val productId: Int,
    @PrimaryKey(autoGenerate = true) var id: Int = 0
) {
    constructor() : this(userId = 0, productId = 0, id = 0)
}