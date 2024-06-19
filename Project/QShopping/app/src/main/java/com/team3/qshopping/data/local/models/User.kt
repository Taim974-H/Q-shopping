package com.team3.qshopping.data.local.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable

@Entity
@Serializable
data class User(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id") var id: Int = 0,
    @ColumnInfo(name = "full_name") val fullName: String = "",
    @ColumnInfo(name = "username") val userName: String = "",
    @ColumnInfo(name = "email") val email: String = "",
    @ColumnInfo(name = "password") val password: String = "",
    @ColumnInfo(name = "is_admin") val isAdmin: Boolean = false,
    @ColumnInfo(name = "profile_picture") val profilePicture: String? = null
) {
    constructor() : this(
        id = 0,
        fullName = "",
        userName = "",
        email = "",
        password = "",
        isAdmin = false,
        profilePicture = null
    )
}