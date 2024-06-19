package com.team3.qshopping.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.team3.qshopping.data.local.models.Category
import kotlinx.coroutines.flow.Flow

@Dao
interface CategoryDao {
    @Query("SELECT COUNT(*) from category")
    fun count(): Int

    @Query("SELECT * from category")
    fun getAll(): Flow<List<Category>>

    @Query("SELECT * FROM category WHERE id = :categoryId")
    suspend fun getCategory(categoryId: Int): Category

    @Insert
    fun insertAll(categories: List<Category>)
}