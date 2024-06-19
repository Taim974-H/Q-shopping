package com.team3.qshopping.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.team3.qshopping.data.local.models.User

@Dao
interface UserDao {
    @Query("select count(*) from user")
    suspend fun count(): Int

    @Query("select count(*) from user where (username = :username and password = :password)")
    suspend fun findUser(username: String, password: String): Int

    @Query("select * from user where (username = :username and password = :password)")
    suspend fun getUser(username: String, password: String): User?

    @Query("select * from user where id = :id")
    suspend fun getUserById(id: Int): User?

    @Query("UPDATE user SET `profile_picture` = :profilePicture where id = :id")
    suspend fun updateProfilePicture(id: Int, profilePicture: String): Int

    @Query("select count(*) from user where username = :username")
    suspend fun countUsername(username: String): Int

    @Query("select count(*) from user where email = :email")
    suspend fun countEmail(email: String): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(user: User)

    @Insert
    suspend fun insertAll(users: List<User>)
}