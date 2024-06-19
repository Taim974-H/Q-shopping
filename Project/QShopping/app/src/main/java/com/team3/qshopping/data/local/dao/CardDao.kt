package com.team3.qshopping.data.local.dao

import androidx.room.*
import com.team3.qshopping.data.local.models.Card
import kotlinx.coroutines.flow.Flow

@Dao
interface CardDao {
    @Query("select * from card where holder_id = :holderId")
    fun getAllByHolderId(holderId: Int): Flow<List<Card>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(card: Card)

    @Delete
    fun delete(card: Card)

    @Query("SELECT COUNT(*) FROM card")
    fun count(): Int

    @Insert
    suspend fun insertAll(users: List<Card>)
}