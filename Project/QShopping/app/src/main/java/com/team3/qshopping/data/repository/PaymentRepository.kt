package com.team3.qshopping.data.repository

import android.content.Context
import com.team3.qshopping.Globals.database
import com.team3.qshopping.data.local.models.Payment
import kotlinx.coroutines.flow.Flow
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json

object PaymentRepository {
    suspend fun getAllByOrderId(orderId: Int): Flow<List<Payment>> =
        database.paymentDao().getAllByOrderId(orderId)

    suspend fun insert(payment: Payment) = database.paymentDao().insert(payment)

    suspend fun delete(payment: Payment) = database.paymentDao().delete(payment)

    fun getJSON(context: Context): List<Payment> {
        val json =
            context.assets.open("payments.json").bufferedReader().use { it.readText() }
        return Json.decodeFromString(json)
    }
}