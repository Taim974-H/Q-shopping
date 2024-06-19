package com.team3.qshopping.data.local.dao

import androidx.room.*
import com.team3.qshopping.data.local.models.Review
import kotlinx.coroutines.flow.Flow

@Dao
interface ReviewDao {
    @Query("select * from review where product_id = :productId")
    fun getAllByProductId(productId: Int): Flow<List<Review>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(review: Review)

    @Update
    fun update(review: Review)

    @Delete
    fun delete(review: Review)

    @Query("SELECT COUNT(*) FROM review")
    fun count(): Int

    @Insert
    suspend fun insertAll(users: List<Review>)
}