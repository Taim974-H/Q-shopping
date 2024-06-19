package com.team3.qshopping.data.repository

import android.content.ContentValues
import android.content.Context
import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.AggregateSource
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.team3.qshopping.data.local.models.User
import com.team3.qshopping.data.remote.QShoppingAPI
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.tasks.await
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json

object UserRepository {
    private val db by lazy { Firebase.firestore }
    private val auth by lazy { FirebaseAuth.getInstance() }
    val collection by lazy { db.collection("users") }

    suspend fun create(user: User) {
        user.id = newId()
        collection.add(user).await()
    }

    suspend fun readByEmail(email: String): com.team3.qshopping.data.remote.models.User? {
        val user = collection
            .whereEqualTo("email", email)
            .get()
            .await()
            .toObjects(com.team3.qshopping.data.remote.models.User::class.java)

        return if (user.size > 0) {
            user[0]
        } else {
            null
        }
    }

//    suspend fun insert(user: User) = database.userDao().insert(user)

    suspend fun getUserById(id: Int): User? {
        val user = collection
            .whereEqualTo("id", id)
            .get()
            .await()
            .toObjects(User::class.java)

        return if (user.size > 0) {
            user[0]
        } else {
            null
        }
        //return database.userDao().getUserById(id)
    }

    operator fun invoke() {
        val countQuery = collection.count()
        countQuery.get(AggregateSource.SERVER).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                if (task.result.count == 0L) {
                    val users = runBlocking { QShoppingAPI.instance.readUsers() }
                    println(users)

                    users.forEach {
                        auth
                            .createUserWithEmailAndPassword(it.email, it.password)
                            .addOnCompleteListener { task ->
                                if (task.isSuccessful) {
                                    runBlocking { insertInit(it) }
                                } else {
                                    println(task.exception)
                                }
                            }
                    }
                }
            } else {
                Log.d(ContentValues.TAG, "Count failed: ", task.exception)
            }
        }
    }

    private suspend fun insertInit(user: User) {
        collection.add(user).await()
    }

    private suspend fun newId(): Int {
        val result = collection
            .orderBy("id", Query.Direction.DESCENDING)
            .limit(1)
            .get()
            .await()
            .toObjects(User::class.java)

        return if (result.size > 0) {
            result[0].id + 1
        } else {
            1
        }
    }

    fun getJSONUsers(context: Context): List<User> {
        val json =
            context.assets.open("users.json").bufferedReader().use { it.readText() }
        return Json.decodeFromString(json)
    }
}
