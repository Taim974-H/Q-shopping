package com.team3.qshopping.data.local.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import java.time.LocalDateTime

@Entity(
    foreignKeys = [
        ForeignKey(
            entity = User::class,
            parentColumns = arrayOf("id"),
            childColumns = arrayOf("user_id"),
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class Order(
    @PrimaryKey(autoGenerate = true) var id: Int = 0,
    @ColumnInfo(name = "status") var status: String,
    @ColumnInfo(
        name = "timestamp",
        defaultValue = "CURRENT_TIMESTAMP"
    ) val timestamp: LocalDateTime,
    @ColumnInfo(name = "status_change_data") var statusDate: LocalDateTime?,
    @ColumnInfo(name = "user_id") val userId: Int,
    @ColumnInfo(name = "address_line") val addressLine: String,
    @ColumnInfo(name = "city") val city: String,
    @ColumnInfo(name = "country") val country: String,
    @ColumnInfo(name = "po_box") val poBox: Int,
    @ColumnInfo(name = "phone_number") val phoneNumber: Long,
    @ColumnInfo(name = "name_on_card") val nameOnCard: String,
    @ColumnInfo(name = "card_number") val cardNumber: Long,
    @ColumnInfo(name = "cvv") val cvv: Int,
    @ColumnInfo(name = "expiry_year") val expiryYear: Int,
    @ColumnInfo(name = "expiry_month") val expiryMonth: Int,
) {
    constructor() : this(
        id = 0,
        status = "",
        timestamp = LocalDateTime.now(),
        statusDate = null,
        userId = 0,
        addressLine = "",
        city = "",
        country = "",
        poBox = 0,
        phoneNumber = 0,
        nameOnCard = "",
        cardNumber = 0,
        cvv = 0,
        expiryYear = 0,
        expiryMonth = 0
    )
}