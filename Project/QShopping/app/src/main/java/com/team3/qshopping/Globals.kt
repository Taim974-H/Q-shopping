package com.team3.qshopping

import android.content.Context
import com.team3.qshopping.data.local.QShoppingDatabase
import com.team3.qshopping.data.remote.models.User

object Globals {
    lateinit var database: QShoppingDatabase
        private set

    fun provide(context: Context) {
        database = QShoppingDatabase.getInstance(context)
    }

    var user: User? = null
}