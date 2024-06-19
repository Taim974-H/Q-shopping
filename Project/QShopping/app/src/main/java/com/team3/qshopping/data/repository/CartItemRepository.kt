package com.team3.qshopping.data.repository

import android.content.ContentValues
import android.util.Log
import com.google.firebase.firestore.AggregateSource
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.team3.qshopping.data.remote.QShoppingAPI
import com.team3.qshopping.data.remote.models.AnnotatedCartItem
import com.team3.qshopping.data.remote.models.CartItem
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.tasks.await

object CartItemRepository {
    private val db by lazy { Firebase.firestore }
    val collection by lazy { db.collection("cartItems") }

    suspend fun update(cartItem: CartItem) {
        collection.whereEqualTo("id", cartItem.id)
            .get()
            .await()
            .forEach { collection.document(it.id).set(cartItem) }
    }

    suspend fun getUserCartItems(userId: Int): List<AnnotatedCartItem> {
        val cartItems = collection
            .whereEqualTo("userId", userId)
            .get().await().toObjects(CartItem::class.java)
        return annotateCartItems(cartItems)
    }

    suspend fun delete(userId: Int, productId: Int) {
        collection
            .whereEqualTo("userId", userId)
            .whereEqualTo("productId", productId)
            .get().await().forEach {
                collection.document(it.id).delete().await()
            }
    }

    suspend fun delete(cartItemId: Int) {
        collection.whereEqualTo("id", cartItemId).get().await().forEach {
            collection.document(it.id).delete().await()
        }
    }

    suspend fun deleteUserCartItems(userId: Int) {
        collection.whereEqualTo("userId", userId).get().await().forEach {
            collection.document(it.id).delete().await()
        }
    }

    suspend fun insert(userId: Int, productId: Int, quantity: Int) {
        val newCartItem = CartItem(
            id = newId(),
            quantity = quantity,
            userId = userId,
            productId = productId
        )
        collection.add(newCartItem).await()
    }

    operator fun invoke() {
        val countQuery = collection.count()
        countQuery.get(AggregateSource.SERVER).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                if (task.result.count == 0L) {
                    val cartItems = runBlocking { QShoppingAPI.instance.readCartItems() }
                    println(cartItems)

                    cartItems.forEach {
                        CoroutineScope(Dispatchers.IO).launch {
                            insertInit(it)
                        }
                    }
                }
            } else {
                Log.d(ContentValues.TAG, "Count failed: ", task.exception)
            }
        }
    }

    private suspend fun insertInit(cartItem: CartItem) {
        collection.add(cartItem).await()
    }

    private suspend fun newId(): Int {
        val result = collection
            .orderBy("id", Query.Direction.DESCENDING)
            .limit(1)
            .get()
            .await()
            .toObjects(CartItem::class.java)

        return if (result.size > 0) {
            result[0].id + 1
        } else {
            1
        }
    }

    private suspend fun annotateCartItems(cartItems: List<CartItem>): List<AnnotatedCartItem> {
        return cartItems.map {
            val product = ProductRepository.getProductById(it.productId)
            AnnotatedCartItem(
                id = it.id, userId = it.userId, quantity = it.quantity, product = product
            )
        }
    }
}