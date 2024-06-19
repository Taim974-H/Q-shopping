package com.team3.qshopping.data.remote

import com.team3.qshopping.data.local.models.Category
import com.team3.qshopping.data.local.models.OrderItem
import com.team3.qshopping.data.local.models.Review
import com.team3.qshopping.data.local.models.User
import com.team3.qshopping.data.remote.models.CartItem
import com.team3.qshopping.data.remote.models.Order
import com.team3.qshopping.data.remote.models.RemoteProduct
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

interface QShoppingAPI {
    @GET("users")
    suspend fun readUsers(): List<User>

    @GET("categories")
    suspend fun readCategories(): List<Category>

    @GET("products2")
    suspend fun readProducts(): List<RemoteProduct>

    @GET("orders")
    suspend fun readOrders(): List<Order>

    @GET("orderItems")
    suspend fun readOrderItems(): List<OrderItem>

    @GET("cartItems")
    suspend fun readCartItems(): List<CartItem>

    @GET("reviews")
    suspend fun readReviews(): List<Review>

    companion object {
        private const val BASE_URL =
            "https://gist.githubusercontent.com/Ahmed-Abdou14/0794ba99306180246df44cb059c183d4/raw/7b3099fecfa457fc8f75800ca057aa834a45c072/"
        val instance: QShoppingAPI by lazy {
            Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(QShoppingAPI::class.java)
        }
    }
}