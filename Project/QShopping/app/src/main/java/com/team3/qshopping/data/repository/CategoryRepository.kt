package com.team3.qshopping.data.repository

import android.content.ContentValues
import android.content.Context
import android.util.Log
import com.google.firebase.firestore.AggregateSource
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.team3.qshopping.data.local.models.Category
import com.team3.qshopping.data.remote.QShoppingAPI
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.tasks.await
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json

object CategoryRepository {
    private val db by lazy { Firebase.firestore }
    val collection by lazy { db.collection("categories") }

    suspend fun create(category: Category) {
        category.id = newId()
        collection.add(category).await()
    }

    suspend fun getAll(): List<Category> {
        return collection.get().await().toObjects(Category::class.java)
    }

    suspend fun getCategory(categoryId: Int): Category? {
        val result = collection
            .whereEqualTo("id", categoryId)
            .get()
            .await()
            .toObjects(Category::class.java)

        return if (result.size > 0) {
            result[0]
        } else {
            null
        }
    }

    operator fun invoke() {

        val countQuery = collection.count()
        countQuery.get(AggregateSource.SERVER).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                if (task.result.count == 0L) {
                    val categories = runBlocking { QShoppingAPI.instance.readCategories() }
                    println(categories)

                    categories.forEach {
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

    private suspend fun insertInit(category: Category) {
        collection.add(category).await()
    }

    private suspend fun newId(): Int {
        val result = collection
            .orderBy("id", Query.Direction.DESCENDING)
            .limit(1)
            .get()
            .await()
            .toObjects(Category::class.java)

        return if (result.size > 0) {
            result[0].id + 1
        } else {
            1
        }
    }

    fun getJSONCategories(context: Context): List<Category> {
        val json = context.assets
            .open("categories.json")
            .bufferedReader()
            .use { it.readText() }
        return Json.decodeFromString(json)
    }
}