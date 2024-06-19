package com.team3.qshopping.data.remote.models

class AnnotatedWishListItem(
    var id: Int = 0,
    val userId: Int,
    val product: RemoteProduct,
)