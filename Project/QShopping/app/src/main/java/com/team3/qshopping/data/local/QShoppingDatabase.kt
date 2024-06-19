package com.team3.qshopping.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.team3.qshopping.data.local.converters.LocalDateUTCToTimeStampConverter
import com.team3.qshopping.data.local.dao.*
import com.team3.qshopping.data.local.models.*
import com.team3.qshopping.data.repository.*

@Database(
    entities = [
        Address::class, Card::class, CartItem::class, WishListItem::class, Category::class,
        Order::class, OrderItem::class, Payment::class, Product::class, Review::class, User::class
    ],
    version = 4,
    exportSchema = false
)
@TypeConverters(LocalDateUTCToTimeStampConverter::class)
//Local db is archived
abstract class QShoppingDatabase : RoomDatabase() {
    abstract fun addressDao(): AddressDao
    abstract fun cardDao(): CardDao
    abstract fun cartItemDao(): CartItemDao
    abstract fun wishListItemDao(): WishListItemDao
    abstract fun categoryDao(): CategoryDao
    abstract fun orderDao(): OrderDao
    abstract fun orderItemDao(): OrderItemDao
    abstract fun paymentDao(): PaymentDao
    abstract fun productDao(): ProductDao
    abstract fun reviewDao(): ReviewDao
    abstract fun userDao(): UserDao

    companion object {
        @Volatile
        private var INSTANCE: QShoppingDatabase? = null
        fun getInstance(context: Context): QShoppingDatabase {
            synchronized(this) {
                var instance = INSTANCE
                if (instance == null) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        QShoppingDatabase::class.java,
                        "QShopping_database"
                    ).fallbackToDestructiveMigration().build()
                    instance.populateInitialData(context)
                    INSTANCE = instance
                }
                return instance
            }
        }
    }

    private fun populateInitialData(context: Context) {
//        CoroutineScope(Dispatchers.IO).launch {
//            if (userDao().count() == 0)
//                userDao().insertAll(UserRepository.getJSONUsers(context))
//            if (categoryDao().count() == 0)
//                categoryDao().insertAll(CategoryRepository.getJSONCategories(context))
//
//            if (productDao().count() == 0)
//                productDao().insertAll(ProductRepository.getJSONProducts(context))
//
//            if (addressDao().count() == 0)
//                addressDao().insertAll(AddressRepository.getJSON(context))
//            if (cardDao().count() == 0)
//                cardDao().insertAll(CardRepository.getJSON(context))
//            if (cartItemDao().count() == 0)
//                cartItemDao().insertAll(CartItemRepository.getJSON(context))
//            if (orderDao().count() == 0)
//                orderDao().insertAll(OrderRepository.getJSON(context))
//            if (reviewDao().count() == 0)
//                reviewDao().insertAll(ReviewRepository.getJSON(context))
//
//            if (paymentDao().count() == 0)
//                paymentDao().insertAll(PaymentRepository.getJSON(context))
//            if (orderItemDao().count() == 0)
//                orderItemDao().insertAll(OrderItemRepository.getJSON(context))
//        }
    }
}