package com.team3.qshopping.data.remote.models

data class RemoteProduct(
    var id: Int,
    var title: String,
    var description: String,
    var image: String,
    var price: Double,
    var stock: Int,
    var discountRate: Double,
    var dateAdded: String,
    var categoryId: Int,
) {
    constructor() : this(
        id = 0,
        title = "",
        description = "",
        image = "",
        price = 0.0,
        stock = 0,
        discountRate = 0.0,
        dateAdded = "",
        categoryId = 0
    )
}