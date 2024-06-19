package com.team3.qshopping.data.local.dao

import androidx.room.*
import com.team3.qshopping.data.local.models.CartItem
import com.team3.qshopping.data.local.models.embeded.CartItemWithProduct
import kotlinx.coroutines.flow.Flow

@Dao
interface CartItemDao {
    @Query("SELECT * FROM cart_item WHERE user_id = :userId")
    fun getAllByUserId(userId: Int): Flow<List<CartItem>>

    @Transaction
    @Query("SELECT * FROM cart_item WHERE user_id = :userId")
    fun getCartItemsWithProduct(userId: Int): Flow<List<CartItemWithProduct>>

    @Query("SELECT SUM(quantity * p.unit_price) FROM cart_item, product as p WHERE (user_id = :userId AND p.id = product_id)")
    fun cartPriceById(userId: Int): Double

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(cartItem: CartItem)

    @Delete
    suspend fun delete(cartItem: CartItem)

    @Query("DELETE FROM cart_item WHERE user_id = :userId AND product_id = :productId")
    suspend fun delete(userId: Int, productId: Int): Int

    @Query("DELETE FROM cart_item WHERE user_id = :userId ")
    suspend fun deleteAll(userId: Int): Int

    @Query("SELECT COUNT(*) FROM cart_item")
    fun count(): Int

    @Update
    fun update(cartItem: CartItem)

    @Insert
    suspend fun insertAll(users: List<CartItem>)
}