package com.team3.qshopping

import com.google.firebase.firestore.Query
import com.team3.qshopping.data.local.models.Order
import com.team3.qshopping.data.local.models.User
import kotlinx.coroutines.tasks.await
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter

fun utcToLocal(dateTime: LocalDateTime): LocalDateTime {
    return LocalDateTime.ofInstant(dateTime.toInstant(ZoneOffset.UTC), ZoneId.systemDefault())
}

fun remoteToLocalOrder(order: com.team3.qshopping.data.remote.models.Order): Order {
    val statusDate = order.statusDate
    return Order(
        id = order.id,
        status = order.status,
        timestamp = LocalDateTime.parse(
            order.timestamp,
            DateTimeFormatter.ISO_LOCAL_DATE_TIME
        ),
        statusDate = statusDate?.let {
            LocalDateTime.parse(
                statusDate,
                DateTimeFormatter.ISO_LOCAL_DATE_TIME
            )
        },
        userId = order.userId,
        addressLine = order.addressLine,
        city = order.city,
        country = order.country,
        poBox = order.poBox,
        phoneNumber = order.phoneNumber,
        nameOnCard = order.nameOnCard,
        cardNumber = order.cardNumber,
        cvv = order.cvv,
        expiryYear = order.expiryYear,
        expiryMonth = order.expiryMonth
    )
}

fun localToRemoteOrder(order: Order): com.team3.qshopping.data.remote.models.Order {
    return com.team3.qshopping.data.remote.models.Order(
        id = order.id,
        status = order.status,
        timestamp = order.timestamp.toString(),
        statusDate = order.statusDate?.toString(),
        addressLine = order.addressLine,
        city = order.city,
        country = order.country,
        poBox = order.poBox,
        phoneNumber = order.phoneNumber,
        nameOnCard = order.nameOnCard,
        cvv = order.cvv,
        cardNumber = order.cardNumber,
        expiryYear = order.expiryYear,
        expiryMonth = order.expiryMonth,
        userId = order.userId
    )
}

fun remoteToLocalUser(user: com.team3.qshopping.data.remote.models.User): User {
    return User(
        id = user.id,
        fullName = user.fullName,
        userName = user.userName,
        email = user.email,
        password = user.password,
        isAdmin = user.admin,
        profilePicture = user.profilePicture
    )
}

//Firebase does not allow WHERE id IN operation for more than 10 elements
//this function splits a list of ids into 10s and performs the query for each
suspend fun <T> fetchWhereIdIn(
    ids: List<Int>,
    query: Query,
    typeClass: Class<T>,
    idField: String = "id"
): List<T> {
    val chunkedIds = ids.chunked(10)
    val results = chunkedIds.map { idChunk ->
        query.whereIn(idField, idChunk)
            .get()
            .await()
            .toObjects(typeClass)
    }
    return results.flatten()
}