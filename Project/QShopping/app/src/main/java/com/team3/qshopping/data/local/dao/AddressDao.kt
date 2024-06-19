package com.team3.qshopping.data.local.dao

import androidx.room.*
import com.team3.qshopping.data.local.models.Address
import kotlinx.coroutines.flow.Flow

@Dao
interface AddressDao {
    @Query("select * from address where user_id = :userId")
    fun getAllByUserId(userId: Int): Flow<List<Address>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(address: Address)

    @Delete
    fun delete(address: Address)

    @Query("SELECT COUNT(*) FROM address")
    fun count(): Int

    @Insert
    suspend fun insertAll(users: List<Address>)
}