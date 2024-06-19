package com.team3.qshopping.data.repository

import android.content.Context
import com.team3.qshopping.Globals.database
import com.team3.qshopping.data.local.models.Card
import kotlinx.coroutines.flow.Flow
import kotlinx.serialization.json.*
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

object CardRepository {
    suspend fun getAllByHolderId(holderId: Int): Flow<List<Card>> =
        database.cardDao().getAllByHolderId(holderId)

    suspend fun insert(card: Card) = database.cardDao().insert(card)

    suspend fun delete(card: Card) = database.cardDao().delete(card)

    fun getJSON(context: Context): List<Card> {
        val cards = arrayListOf<Card>()
        val cardsJson =
            context.assets.open("cards.json").bufferedReader().use { it.readText() }
        val json = Json.parseToJsonElement(cardsJson).jsonArray
        json.forEach {
            val card = it.jsonObject
            cards.add(
                Card(
                    id = card["id"]?.jsonPrimitive?.int!!,
                    name = card["name"]?.jsonPrimitive?.content!!,
                    number = card["number"]?.jsonPrimitive?.content!!,
                    ccv = card["ccv"]?.jsonPrimitive?.content!!,
                    expiryDate = LocalDateTime.parse(
                        card["expiryDate"]?.jsonPrimitive?.content!!,
                        DateTimeFormatter.ISO_LOCAL_DATE_TIME
                    ),
                    holderId = card["holderId"]?.jsonPrimitive?.int!!
                )
            )
        }
        return cards
    }
}