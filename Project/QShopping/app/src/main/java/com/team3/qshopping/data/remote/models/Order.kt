package com.team3.qshopping.data.remote.models

data class Order(
    var id: Int,
    val status: String,
    val timestamp: String,
    val statusDate: String?,
    val addressLine: String,
    val city: String,
    val country: String,
    val poBox: Int,
    val phoneNumber: Long,
    val nameOnCard: String,
    val cardNumber: Long,
    val cvv: Int,
    val expiryYear: Int,
    val expiryMonth: Int,
    val userId: Int,
) {
    constructor() : this(
        id = 0,
        status = "",
        timestamp = "",
        statusDate = null,
        addressLine = "",
        city = "",
        country = "",
        poBox = 0,
        phoneNumber = 0,
        nameOnCard = "",
        cardNumber = 0,
        cvv = 0,
        expiryYear = 0,
        expiryMonth = 0,
        userId = 0
    )
}