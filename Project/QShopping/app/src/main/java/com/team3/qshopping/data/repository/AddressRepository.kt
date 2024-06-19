package com.team3.qshopping.data.repository

import android.content.Context
import com.team3.qshopping.Globals.database
import com.team3.qshopping.data.local.models.Address
import kotlinx.coroutines.flow.Flow
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json

object AddressRepository {
    fun getAllByUserId(userId: Int): Flow<List<Address>> =
        database.addressDao().getAllByUserId(userId)

    fun insert(address: Address) = database.addressDao().insert(address)

    fun delete(address: Address) = database.addressDao().delete(address)

    fun getJSON(context: Context): List<Address> {
        val json =
            context.assets.open("addresses.json").bufferedReader().use { it.readText() }
        return Json.decodeFromString(json)
    }
}