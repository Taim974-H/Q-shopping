package com.team3.qshopping.data.local.dao

import androidx.room.*
import com.team3.qshopping.data.local.models.Product
import com.team3.qshopping.data.local.models.WishListItem
import kotlinx.coroutines.flow.Flow

@Dao
interface WishListItemDao {
    @Query("SELECT * FROM wish_list_item WHERE user_id = :userId")
    fun getAllByUserId(userId: Int): Flow<List<WishListItem>>

    @Query("SELECT *  FROM Product L LEFT join  wish_list_item R ON L.id=(R.product_id = :productId) Where R.product_id is NULL")
    fun getWishListProducts(productId: Int): Flow<List<Product>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(wishListItem: WishListItem)

    @Delete
    suspend fun delete(wishListItem: WishListItem)

    @Query("DELETE FROM wish_list_item WHERE user_id = :userId AND product_id = :productId")
    suspend fun delete(userId: Int, productId: Int): Int

    @Query("SELECT COUNT(*) FROM wish_list_item")
    fun count(): Int

    @Insert
    suspend fun insertAll(users: List<WishListItem>)
}