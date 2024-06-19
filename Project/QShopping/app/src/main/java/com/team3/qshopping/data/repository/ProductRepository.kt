package com.team3.qshopping.data.repository

import android.content.ContentValues
import android.util.Log
import com.google.firebase.firestore.AggregateSource
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.team3.qshopping.Globals
import com.team3.qshopping.data.local.models.*
import com.team3.qshopping.data.remote.QShoppingAPI
import com.team3.qshopping.data.remote.models.AnnotatedProduct
import com.team3.qshopping.data.remote.models.RemoteProduct
import com.team3.qshopping.fetchWhereIdIn
import kotlinx.coroutines.*
import kotlinx.coroutines.tasks.await
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter

object ProductRepository {
    private val db by lazy { Firebase.firestore }
    val collection by lazy { db.collection("products") }

    operator fun invoke() {
        val countQuery = collection.count()
        countQuery.get(AggregateSource.SERVER).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                if (task.result.count == 0L) {
                    runBlocking { delay(1000) }
                    val products = runBlocking { QShoppingAPI.instance.readProducts() }
                    println(products)

                    CoroutineScope(Dispatchers.IO).launch {
                        products.forEach { insertInit(it) }
                    }
                }
            } else {
                Log.d(ContentValues.TAG, "Count failed: ", task.exception)
            }
        }
    }

    suspend fun getProductById(productId: Int): RemoteProduct {
        val remoteProducts = collection
            .whereEqualTo("id", productId)
            .get()
            .await()
            .toObjects(RemoteProduct::class.java)
        return remoteProducts[0]
    }

    suspend fun getAnnotatedProductById(userId: Int, productId: Int): AnnotatedProduct {
        val remoteProducts = collection
            .whereEqualTo("id", productId)
            .get()
            .await()
            .toObjects(RemoteProduct::class.java)
        return annotateProduct(userId, remoteProducts[0])
    }

    suspend fun getProductByCategory(categoryId: Int, userId: Int?): List<AnnotatedProduct> {
        val remoteProducts = collection
            .whereEqualTo("categoryId", categoryId)
            .get()
            .await()
            .toObjects(RemoteProduct::class.java)
        return annotateProducts(userId!!, remoteProducts)
    }

    suspend fun searchProducts(query: String, userId: Int?): List<AnnotatedProduct> {
        //FireStore does not have a string contains operation
        //this is a workaround taken from https://stackoverflow.com/a/49925860/14200676
        Log.d("STATE", "searching...")
        val remoteProducts = collection
            .whereGreaterThanOrEqualTo("title", query)
            .whereLessThanOrEqualTo("title", query + "\uF8FF")
//            .orderBy("title")
//            .startAt("%${query}%")
//            .endAt(query + "\uf8ff")
            .get()
            .await()
            .toObjects(RemoteProduct::class.java)
        return annotateProducts(userId!!, remoteProducts)
    }

    suspend fun getTrendingProducts(userId: Int?): List<AnnotatedProduct> {
        val remoteProducts = collection
            .get()
            .await()
            .toObjects(RemoteProduct::class.java)
        val orderCountByProduct = db
            .collection("orderItems")
            .get()
            .await()
            .toObjects(OrderItem::class.java)
            .groupingBy { it.productId }
            .eachCount()
        return annotateProducts(userId!!, remoteProducts)
            .sortedByDescending { orderCountByProduct.getOrDefault(it.id, 0) }
    }

    suspend fun getOnSaleProducts(userId: Int?): List<AnnotatedProduct> {
        val remoteProducts = collection
            .whereGreaterThan("discountRate", 0)
            .get()
            .await()
            .toObjects(RemoteProduct::class.java)
        return annotateProducts(userId!!, remoteProducts)
    }

    suspend fun getNewlyAddedProducts(userId: Int?): List<AnnotatedProduct> {
        val currentTime = LocalDateTime.now().atOffset(ZoneOffset.UTC)
        val oneYearAgo = currentTime.minusYears(1)
        val remoteProducts = collection
            .whereGreaterThan("dateAdded", oneYearAgo.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME))
            .whereLessThan("dateAdded", currentTime.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME))
            .get()
            .await()
            .toObjects(RemoteProduct::class.java)
        return annotateProducts(userId!!, remoteProducts)
    }

    suspend fun getUserWishListProducts(userId: Int): List<AnnotatedProduct> {
        val wishListItems = db.collection("wishlistItems")
            .whereEqualTo("userId", userId)
            .get()
            .await()
            .toObjects(WishListItem::class.java)
        val products = fetchWhereIdIn(
            wishListItems.map { it.productId },
            collection,
            RemoteProduct::class.java
        )
        return annotateProducts(userId, products)
    }

    suspend fun insertProduct(
        title: String,
        image: String,
        description: String,
        unitPrice: Double,
        stock: Int,
        discountRate: Double,
        dateAdded: LocalDateTime,
        categoryId: Int
    ) {
        val newProduct = RemoteProduct(
            id = newId(),
            title = title,
            image = image,
            description = description,
            price = unitPrice,
            stock = stock,
            discountRate = discountRate,
            dateAdded = dateAdded.format(DateTimeFormatter.ISO_DATE_TIME),
            categoryId = categoryId
        )
        collection.add(newProduct).await()
    }

    suspend fun updateProduct(product: RemoteProduct) {
        collection.whereEqualTo("id", product.id).get().await().forEach {
            collection.document(it.id).set(product).await()
        }
    }

    suspend fun deleteProduct(productId: Int) {
        collection.whereEqualTo("id", productId).get().await().forEach {
            collection.document(it.id).delete().await()
        }
    }

    private suspend fun insertInit(product: RemoteProduct) {
        collection.add(product).await()
    }

    private suspend fun newId(): Int {
        val result = collection
            .orderBy("id", Query.Direction.DESCENDING)
            .limit(1)
            .get()
            .await()
            .toObjects(RemoteProduct::class.java)

        return if (result.size > 0) result[0].id + 1 else 1
    }

    private suspend fun annotateProducts(
        userId: Int,
        products: List<RemoteProduct>
    ): List<AnnotatedProduct> {
        val cartItems = db.collection("cartItems")
            .whereEqualTo("userId", userId)
            .get()
            .await()
            .toObjects(CartItem::class.java)
            .associate { it.productId to true }
        val reviewsByProduct = db.collection("reviews")
            .get()
            .await()
            .toObjects(Review::class.java)
            .groupBy { it.productId }
        return products.map { product ->
            val productReviews = reviewsByProduct.getOrDefault(product.id, emptyList())
            val isInWishlist = WishListItemRepository.isInWishlist(Globals.user!!.id, product.id)
            val reviewScore = if (productReviews.isNotEmpty()) {
                (productReviews.sumOf { it.rating }) / productReviews.size.toDouble()
            } else {
                0.0
            }

            AnnotatedProduct(
                product,
                productReviews,
                cartItems.getOrDefault(product.id, false),
                isInWishlist,
                productReviews.size,
                reviewScore
            )
        }
    }

    private suspend fun annotateProduct(
        userId: Int,
        product: RemoteProduct
    ): AnnotatedProduct {
        val isInCart = !db.collection("cartItems")
            .whereEqualTo("userId", userId)
            .whereEqualTo("productId", product.id)
            .get()
            .await()
            .isEmpty
        val productReviews = db.collection("reviews")
            .whereEqualTo("productId", product.id)
            .get()
            .await()
            .toObjects(Review::class.java)
        val isInWishlist = WishListItemRepository.isInWishlist(Globals.user!!.id, product.id)
        val reviewScore = if (productReviews.size > 0) {
            (productReviews.sumOf { it.rating }) / productReviews.size.toDouble()
        } else {
            0.0
        }

        return AnnotatedProduct(
            product,
            productReviews,
            isInCart,
            isInWishlist,
            productReviews.size,
            reviewScore
        )
    }

    fun filterProducts(
        productsList: List<AnnotatedProduct>,
        category: Category?,
        rating: Int,
        priceRange: ClosedFloatingPointRange<Float>,
    ): List<AnnotatedProduct> {
        val filteredProducts = productsList
            .filter { prod ->
                category == null || prod.categoryId == category.id
            }.filter { prod ->
                prod.reviewScore >= rating
            }.filter { prod ->
                val discountedPrice = prod.price - prod.price * prod.discountRate
                discountedPrice > priceRange.start && discountedPrice <= priceRange.endInclusive
            }
        return filteredProducts
    }
}