package com.team3.qshopping.data.remote

import com.team3.qshopping.data.repository.*

fun initialiseCollections() {
    UserRepository()
    CategoryRepository()
    ProductRepository()
    OrderRepository()
    OrderItemRepository()
    CartItemRepository()
    ReviewRepository()
}