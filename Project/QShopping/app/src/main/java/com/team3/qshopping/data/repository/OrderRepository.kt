package com.team3.qshopping.data.repository

import android.content.ContentValues
import android.content.Context
import android.util.Log
import com.google.firebase.firestore.AggregateSource
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.team3.qshopping.data.local.models.Order
import com.team3.qshopping.data.local.models.OrderItem
import com.team3.qshopping.data.remote.QShoppingAPI
import com.team3.qshopping.data.remote.models.AnnotatedOrder
import com.team3.qshopping.data.remote.models.RemoteProduct
import com.team3.qshopping.fetchWhereIdIn
import com.team3.qshopping.localToRemoteOrder
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.tasks.await
import kotlinx.serialization.json.*
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import com.team3.qshopping.data.remote.models.Order as RemoteOrder

object OrderRepository {
    private val db by lazy { Firebase.firestore }
    val collection by lazy { db.collection("orders") }

    suspend fun create(order: Order): Int {
        val remoteOrder = localToRemoteOrder(order)
        remoteOrder.id = newId()
        collection.add(remoteOrder).await()
        return remoteOrder.id
    }

    private fun remoteToLocalOrder(remoteOrders: MutableList<RemoteOrder>): List<Order> {
        val localOrders = mutableListOf<Order>()
        for (remoteOrder in remoteOrders) {
            localOrders.add(com.team3.qshopping.remoteToLocalOrder(remoteOrder))
        }
        return localOrders
    }

    suspend fun getStatusByOrderId(orderId: Int): String? {
        val order =
            collection.whereEqualTo("id", orderId).get().await().toObjects(Order::class.java)

        return if (order.size > 0) {
            order[0].status
        } else {
            null
        }
    }

    suspend fun getAllByUserId(userId: Int): List<Order> {
        val remoteOrders = collection.whereEqualTo("userId", userId).get().await()
            .toObjects(RemoteOrder::class.java)
        return remoteToLocalOrder(remoteOrders)
    }

    suspend fun insert(order: Order): Int {
        return create(order)
//        return database.orderDao().insert(order).toInt()
    }

    suspend fun getOrdersWithItems(
        orderStatus: OrderStatus?,
        startTime: LocalDateTime?,
        endTime: LocalDateTime?,
    ): List<AnnotatedOrder> {
        val status = if (orderStatus == null || orderStatus == OrderStatus.ALL) null
        else orderStatus.status

        var query: Query = collection
        if (status != null) query = query.whereEqualTo("status", status)
        if (startTime != null) query = query.whereGreaterThanOrEqualTo(
            "timestamp", startTime.format(DateTimeFormatter.ISO_DATE_TIME)
        )
        if (endTime != null) query = query.whereLessThanOrEqualTo(
            "timestamp", endTime.format(DateTimeFormatter.ISO_DATE_TIME)
        )

        val remoteOrders = query.get().await().toObjects(RemoteOrder::class.java)
        return annotateOrders(remoteOrders)
    }

    private suspend fun annotateOrders(
        remoteOrders: MutableList<RemoteOrder>
    ): List<AnnotatedOrder> {
        val orderItemsByOrder = db.collection("orderItems")
            .get()
            .await()
            .toObjects(OrderItem::class.java)
            .groupBy { it.orderId }
        return remoteOrders.map { order ->
            val orderItems = orderItemsByOrder.getOrDefault(order.id, emptyList())

            val orderProducts = fetchWhereIdIn(
                orderItems.map { it.productId },
                db.collection("products"),
                RemoteProduct::class.java
            )
            AnnotatedOrder(order,
                orderItems,
                orderProducts,
                orderItems.sumOf { it.quantity },
                orderItems.sumOf { it.price * it.quantity }
            )
        }
    }

    suspend fun getAll(): List<Order> {
        val remoteOrders = collection.get().await()
            .toObjects(com.team3.qshopping.data.remote.models.Order::class.java)
        return remoteToLocalOrder(remoteOrders)

        // database.orderDao().getAllOrders()
    }

    suspend fun update(order: Order) {
        collection.whereEqualTo("id", order.id).get().await().forEach {
            val fid = it.id
            val remoteOrder = localToRemoteOrder(order)
            collection.document(fid).set(remoteOrder).await()
        }
        //database.orderDao().update(order)
    }

    operator fun invoke() {
        val countQuery = collection.count()
        countQuery.get(AggregateSource.SERVER).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                if (task.result.count == 0L) {
                    val orders = runBlocking { QShoppingAPI.instance.readOrders() }
                    println(orders)

                    orders.forEach {
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

    private suspend fun insertInit(order: com.team3.qshopping.data.remote.models.Order) {
        collection.add(order).await()
    }

    private suspend fun newId(): Int {
        val result = collection.orderBy("id", Query.Direction.DESCENDING).limit(1).get().await()
            .toObjects(RemoteOrder::class.java)

        return if (result.size > 0) {
            result[0].id + 1
        } else {
            1
        }
    }

    fun getJSON(context: Context): List<Order> {
        val orders = arrayListOf<Order>()
        val ordersJson = context.assets.open("orders.json").bufferedReader().use { it.readText() }
        val json = Json.parseToJsonElement(ordersJson).jsonArray
        json.forEach {
            val order = it.jsonObject
            val statusDateString: String = order["statusDate"]?.jsonPrimitive?.content!!
            val statusDate = if (statusDateString == "null") {
                null
            } else {
                LocalDateTime.parse(statusDateString, DateTimeFormatter.ISO_LOCAL_DATE_TIME)
            }
            orders.add(
                Order(
                    id = order["id"]?.jsonPrimitive?.int!!,
                    status = order["status"]?.jsonPrimitive?.content!!,
                    timestamp = LocalDateTime.parse(
                        order["timestamp"]?.jsonPrimitive?.content!!,
                        DateTimeFormatter.ISO_LOCAL_DATE_TIME
                    ),
                    statusDate = statusDate,
                    userId = order["userId"]?.jsonPrimitive?.int!!,
                    addressLine = order["addressLine"]?.jsonPrimitive?.content!!,
                    city = order["city"]?.jsonPrimitive?.content!!,
                    country = order["country"]?.jsonPrimitive?.content!!,
                    poBox = order["poBox"]?.jsonPrimitive?.int!!,
                    phoneNumber = order["phoneNumber"]?.jsonPrimitive?.long!!,
                    nameOnCard = order["nameOnCard"]?.jsonPrimitive?.content!!,
                    cardNumber = order["cardNumber"]?.jsonPrimitive?.long!!,
                    cvv = order["cvv"]?.jsonPrimitive?.int!!,
                    expiryMonth = order["expiryMonth"]?.jsonPrimitive?.int!!,
                    expiryYear = order["expiryYear"]?.jsonPrimitive?.int!!,
                )
            )
        }
        return orders
    }
}


enum class OrderStatus(val status: String) {
    ALL("All"), PROCESSING("Processing"), SHIPPING("Shipping"), DELIVERED("Delivered");

    companion object {
        private val map = OrderStatus.values().associateBy(OrderStatus::status)
        fun fromString(status: String): OrderStatus = map[status]!!
    }
}