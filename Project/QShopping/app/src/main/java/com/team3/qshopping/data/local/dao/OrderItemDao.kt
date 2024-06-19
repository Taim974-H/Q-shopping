package com.team3.qshopping.data.local.dao

import androidx.room.*
import com.team3.qshopping.data.local.models.OrderItem

@Dao
interface OrderItemDao {
    @Query("select * from order_item where order_id = :orderId")
    fun getAllByOrderId(orderId: Int): List<OrderItem>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(orderItem: OrderItem)

    @Delete
    fun delete(orderItem: OrderItem)

    @Query("SELECT COUNT(*) FROM order_item")
    fun count(): Int

    @Insert
    suspend fun insertAll(users: List<OrderItem>)

    @Query(
        "SELECT o.status FROM order_item as i INNER JOIN `Order` as o ON i.order_id = o.id " +
                "WHERE o.user_id = :userId AND i.product_id = :productId"
    )
    suspend fun findOrderItem(productId: Int, userId: Int): String?
}