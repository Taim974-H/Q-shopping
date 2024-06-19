package com.team3.qshopping.data.remote.models

import com.team3.qshopping.data.local.models.Review

data class AnnotatedProduct(
    var id: Int,
    var title: String,
    var description: String,
    var image: String,
    var price: Double,
    var stock: Int,
    var discountRate: Double,
    var dateAdded: String,
    var categoryId: Int,
    var reviews: List<Review>,
    var reviewCount: Int,
    var reviewScore: Double,
    var isInCart: Boolean,
    var isInWishList: Boolean,
) {
    constructor(
        product: RemoteProduct,
        reviews: List<Review>,
        isInCart: Boolean,
        isInWishList: Boolean,
        reviewCount: Int,
        reviewScore: Double,
    ) : this(
        product.id,
        product.title,
        product.description,
        product.image,
        product.price,
        product.stock,
        product.discountRate,
        product.dateAdded,
        product.categoryId,
        reviews,
        reviewCount,
        reviewScore,
        isInCart,
        isInWishList,
    )

}