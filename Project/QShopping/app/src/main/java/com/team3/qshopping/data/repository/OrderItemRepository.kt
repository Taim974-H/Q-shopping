package com.team3.qshopping.data.repository

import android.content.ContentValues
import android.content.Context
import android.util.Log
import com.google.firebase.firestore.AggregateSource
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.team3.qshopping.data.local.models.OrderItem
import com.team3.qshopping.data.remote.QShoppingAPI
import com.team3.qshopping.data.remote.models.Order
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.tasks.await
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json

object OrderItemRepository {
    private val db by lazy { Firebase.firestore }
    val collection by lazy { db.collection("orderItems") }

    suspend fun create(orderItem: OrderItem) {
        orderItem.id = newId()
        println(orderItem)
        collection.add(orderItem).await()
    }

    suspend fun getAllByOrderId(orderId: Int): MutableList<OrderItem> {
        return collection
            .whereEqualTo("orderId", orderId)
            .get()
            .await()
            .toObjects(OrderItem::class.java)
    }

    suspend fun insert(orderItem: OrderItem) {
        create(orderItem)
        //database.orderItemDao().insert(orderItem)
    }

    suspend fun findOrderItem(productId: Int, userId: Int): String? {
        val orders = OrderRepository.collection
            .whereEqualTo("userId", userId)
            .whereEqualTo("status", "Delivered")
            .get().await().toObjects(Order::class.java)

        orders.forEach { order ->
            val orderItems = getAllByOrderId(order.id)
            println(order)

            orderItems.forEach { orderItem ->
                println(orderItem)
                if (orderItem.productId == productId) {
                    return order.status
                }
            }
        }

        return null

        val orderItems = collection
            .whereEqualTo("productId", productId)
            .get()
            .await()
            .toObjects(OrderItem::class.java)

        return null

        return if (orderItems.size > 0) {
            OrderRepository.getStatusByOrderId(orderItems[0].orderId)
        } else {
            null
        }

        // return database.orderItemDao().findOrderItem(productId, userId)
    }

    suspend fun delete(orderItem: OrderItem) {
        collection.whereEqualTo("id", orderItem.id).get().await().forEach {
            collection.document(it.id).delete().await()
        }
        //database.orderItemDao().delete(orderItem)
    }

    operator fun invoke() {
        val countQuery = collection.count()
        countQuery.get(AggregateSource.SERVER).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                if (task.result.count == 0L) {
                    val orderItems = runBlocking { QShoppingAPI.instance.readOrderItems() }
                    println(orderItems)

                    orderItems.forEach {
                        CoroutineScope(Dispatchers.IO).launch {
                            insertInit(it)
                        }
                    }
                }
            } else {
                Log.d(ContentValues.TAG, "Count failed: ", task.exception)
            }
        }
    }

    private suspend fun insertInit(orderItem: OrderItem) {
        collection.add(orderItem).await()
    }

    private suspend fun newId(): Int {
        val result = collection
            .orderBy("id", Query.Direction.DESCENDING)
            .limit(1)
            .get()
            .await()
            .toObjects(OrderItem::class.java)

        return if (result.size > 0) {
            result[0].id + 1
        } else {
            1
        }
    }

    fun getJSON(context: Context): List<OrderItem> {
        val json =
            context.assets.open("orderItems.json").bufferedReader().use { it.readText() }
        return Json.decodeFromString(json)
    }
}