package com.team3.qshopping.data.remote.models

data class AnnotatedCartItem(
    var id: Int = 0,
    var userId: Int,
    var quantity: Int,
    var product: RemoteProduct,
) {
    val cartItem: CartItem
        get() {
            return CartItem(id = id, userId = userId, quantity = quantity, productId = product.id);
        }
}