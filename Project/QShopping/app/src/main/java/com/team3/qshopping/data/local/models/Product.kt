package com.team3.qshopping.data.local.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import java.time.LocalDateTime

@Entity(
    foreignKeys = [ForeignKey(
        entity = Category::class,
        parentColumns = arrayOf("id"),
        childColumns = arrayOf("category_id"),
        onDelete = ForeignKey.CASCADE
    )]
)
data class Product(
    @ColumnInfo(name = "title") var title: String,
    @ColumnInfo(name = "image") var image: String,
    @ColumnInfo(name = "description") var description: String?,
    @ColumnInfo(name = "unit_price") var unitPrice: Double,
    @ColumnInfo(name = "stock", defaultValue = "0") var stock: Int,
    @ColumnInfo(name = "discount_rate", defaultValue = "0") var discountRate: Double,
    @ColumnInfo(
        name = "date_added",
        defaultValue = "CURRENT_TIMESTAMP"
    ) val dateAdded: LocalDateTime,
    @ColumnInfo(name = "category_id") var categoryId: Int,
    @PrimaryKey(autoGenerate = true) var id: Int = 0
) {
    constructor() : this(
        id = 0,
        title = "",
        image = "",
        description = "",
        unitPrice = 0.0,
        stock = 0,
        discountRate = 0.0,
        dateAdded = LocalDateTime.now(),
        categoryId = 0
    )
}