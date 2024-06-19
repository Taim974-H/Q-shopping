package com.team3.qshopping.data.remote.models

data class User(
    var id: Int,
    val fullName: String = "",
    val userName: String = "",
    val email: String = "",
    val password: String = "",
    val admin: Boolean = false,
    val profilePicture: String? = null
) {
    constructor() : this(
        id = 0,
        fullName = "",
        userName = "",
        email = "",
        password = "",
        admin = false,
        profilePicture = null
    )
}