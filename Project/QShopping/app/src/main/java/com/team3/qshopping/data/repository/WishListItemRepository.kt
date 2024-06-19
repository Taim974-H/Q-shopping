package com.team3.qshopping.data.repository

import android.content.Context
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.team3.qshopping.data.local.models.WishListItem
import kotlinx.coroutines.tasks.await
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json

object WishListItemRepository {
    private val db by lazy { Firebase.firestore }
    val collection by lazy { db.collection("wishlistItems") }

    suspend fun create(wishlistItem: WishListItem) {
        wishlistItem.id = newId()
        collection.add(wishlistItem).await()
    }

    suspend fun isInWishlist(userId: Int, productId: Int): Boolean {
        return collection
            .whereEqualTo("userId", userId)
            .whereEqualTo("productId", productId)
            .get()
            .await()
            .toObjects(WishListItem::class.java)
            .size > 0
    }

    suspend fun delete(userId: Int, productId: Int) {
        collection.whereEqualTo("userId", userId)
            .whereEqualTo("productId", productId).get().await()
            .forEach { collection.document(it.id).delete().await() }
        //Globals.database.wishListItemDao().delete(userId, productId)
    }

    suspend fun insert(wishlistItem: WishListItem) {
        create(wishlistItem)
        //Globals.database.wishListItemDao().insert(wishlistItem)
    }

    fun getJSON(context: Context): List<WishListItem> {
        val json =
            context.assets.open("wishlistItems.json").bufferedReader().use { it.readText() }
        return Json.decodeFromString(json)
    }

    private suspend fun newId(): Int {
        val result = collection
            .orderBy("id", Query.Direction.DESCENDING)
            .limit(1)
            .get()
            .await()
            .toObjects(WishListItem::class.java)

        return if (result.size > 0) {
            result[0].id + 1
        } else {
            1
        }
    }
}