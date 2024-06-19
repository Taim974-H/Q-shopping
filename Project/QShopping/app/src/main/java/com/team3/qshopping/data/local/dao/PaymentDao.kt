package com.team3.qshopping.data.local.dao

import androidx.room.*
import com.team3.qshopping.data.local.models.Payment
import kotlinx.coroutines.flow.Flow

@Dao
interface PaymentDao {
    @Query("select * from payment where order_id = :orderId")
    fun getAllByOrderId(orderId: Int): Flow<List<Payment>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(payment: Payment)

    @Delete
    fun delete(payment: Payment)

    @Query("SELECT COUNT(*) FROM payment")
    fun count(): Int

    @Insert
    suspend fun insertAll(users: List<Payment>)
}