package com.team3.qshopping.data.local.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import java.time.LocalDateTime

@Entity(
    foreignKeys = [ForeignKey(
        entity = User::class,
        parentColumns = arrayOf("id"),
        childColumns = arrayOf("holder_id"),
        onDelete = ForeignKey.CASCADE
    )]
)
data class Card(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "number") val number: String,
    @ColumnInfo(name = "ccv") val ccv: String,
    @ColumnInfo(name = "expiry_date") val expiryDate: LocalDateTime,
    @ColumnInfo(name = "holder_id") val holderId: Int,
)