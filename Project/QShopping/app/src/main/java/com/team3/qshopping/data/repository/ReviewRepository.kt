package com.team3.qshopping.data.repository

import android.content.ContentValues
import android.content.Context
import android.util.Log
import com.google.firebase.firestore.AggregateSource
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.team3.qshopping.data.local.models.Review
import com.team3.qshopping.data.remote.QShoppingAPI
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.tasks.await
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json

object ReviewRepository {
    private val db by lazy { Firebase.firestore }
    val collection by lazy { db.collection("reviews") }

    suspend fun create(review: Review) {
        review.id = newId()
        collection.add(review).await()
    }

    suspend fun getAllReviewsByProductId(productId: Int): List<Review> {
        return collection
            .whereEqualTo("productId", productId)
            .get()
            .await()
            .toObjects(Review::class.java)
    }

    suspend fun update(review: Review) {
        collection
            .whereEqualTo("id", review.id)
            .get()
            .await()
            .forEach {
                collection.document(it.id).set(review).await()
            }
    }

    suspend fun delete(review: Review) {
        collection
            .whereEqualTo("id", review.id)
            .get()
            .await()
            .forEach {
                collection.document(it.id).delete().await()
            }
    }

    operator fun invoke() {
        val countQuery = collection.count()
        countQuery.get(AggregateSource.SERVER).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                if (task.result.count == 0L) {
                    val reviews = runBlocking { QShoppingAPI.instance.readReviews() }
                    println(reviews)

                    CoroutineScope(Dispatchers.IO).launch {
                        reviews.forEach { insertInit(it) }
                    }
                }
            } else {
                Log.d(ContentValues.TAG, "Count failed: ", task.exception)
            }
        }
    }

    private suspend fun insertInit(review: Review) {
        collection.add(review).await()
    }

    private suspend fun newId(): Int {
        val result = collection
            .orderBy("id", Query.Direction.DESCENDING)
            .limit(1)
            .get()
            .await()
            .toObjects(Review::class.java)

        return if (result.size > 0) {
            result[0].id + 1
        } else {
            1
        }
    }

    fun getJSON(context: Context): List<Review> {
        val json =
            context.assets.open("reviews.json").bufferedReader().use { it.readText() }
        return Json.decodeFromString(json)
    }
}