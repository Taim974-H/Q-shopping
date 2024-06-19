package com.team3.qshopping.data.local.dao

import androidx.room.*
import com.team3.qshopping.data.local.models.Order
import com.team3.qshopping.data.local.models.embeded.OrderWithItems
import kotlinx.coroutines.flow.Flow

@Dao
interface OrderDao {
    @Query("SELECT COUNT(*) FROM `order`")
    fun count(): Int

    @Query("SELECT COUNT(*) FROM `order` WHERE user_id = :userId")
    fun countByUserId(userId: Int): Int

    @Query("SELECT * FROM `order` WHERE user_id = :userId")
    fun getAllByUserId(userId: Int): Flow<List<Order>>

    @Insert
    suspend fun insert(order: Order): Long

    @Insert
    fun insertAll(orders: List<Order>)

    @Query("SELECT * FROM `order`")
    fun getAllOrders(): Flow<List<Order>>

    @Query("SELECT * FROM `order` WHERE id IN (:orderIds)")
    fun loadAllByIds(orderIds: IntArray): List<Order>

    @Delete
    suspend fun delete(order: Order)

    @Update
    suspend fun update(order: Order)

    @Query("SELECT * FROM `order` WHERE status = :status")
    fun getByStatus(status: String): Flow<List<Order>>

    @Query(
        """SELECT * FROM `order` WHERE
        (:status IS NULL OR status=:status) AND
        (:startTime IS NULL OR timestamp >=:startTime) AND
        (:endTime IS NULL OR timestamp <= :endTime)"""
    )
    fun getOrders(status: String?, startTime: Long?, endTime: Long?): Flow<List<Order>>

    @Query(
        """SELECT *,
             (SELECT sum(quantity) FROM order_item WHERE order_item.order_id = o.id) AS item_count,
             (SELECT sum(price*quantity) FROM order_item WHERE order_item.order_id = o.id) AS total_price
             FROM `order` AS o WHERE (:status IS NULL OR status=:status) AND
                                (:startTime IS NULL OR timestamp >=:startTime) AND
                                (:endTime IS NULL OR timestamp <= :endTime)"""
    )
    fun getOrdersWithItems(
        status: String?,
        startTime: Long?,
        endTime: Long?
    ): Flow<List<OrderWithItems>>
}