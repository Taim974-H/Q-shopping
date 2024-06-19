package com.team3.qshopping.data.local.dao

import androidx.room.*
import com.team3.qshopping.data.local.models.Product
import com.team3.qshopping.data.local.models.embeded.ProductWithData
import kotlinx.coroutines.flow.Flow

@Dao
interface ProductDao {

    @Query("SELECT COUNT(*) FROM `product`")
    suspend fun count(): Int

    @Query(
        """SELECT *
        FROM `product` AS p WHERE 
        (:query IS NULL OR instr(lower(p.title), lower(:query)) OR instr(lower(p.description), lower(:query))) and
        (:categoryId IS NULL OR p.category_id = :categoryId) and
        (:discountRateMin IS NULL OR p.discount_rate >= :discountRateMin) and
        (:discountRateMax IS NULL OR p.discount_rate <= :discountRateMax) and
        (:dateAddedStart IS NULL OR p.date_added > :dateAddedStart) and
        (:dateAddedEnd IS NULL OR p.date_added < :dateAddedEnd)"""
    )
    fun getProducts(
        query: String? = null,
        categoryId: Int? = null,
        discountRateMin: Double = 0.0,
        discountRateMax: Double = 1.0,
        dateAddedStart: Long? = null,
        dateAddedEnd: Long? = null,
    ): Flow<List<Product>>


    @Query(
        """SELECT *,
        (SELECT count(review.id) FROM review WHERE review.product_id = p.id) AS review_count,
        (SELECT avg(review.rating) FROM review WHERE review.product_id = p.id) AS review_score,
        (SELECT count(cart_item.id) > 0 FROM cart_item WHERE cart_item.product_id = p.id AND (cart_item.user_id=:userId OR :userId IS NULL)) AS is_in_cart,
        (SELECT count(wish_list_item.id) > 0 FROM wish_list_item WHERE wish_list_item.product_id = p.id  AND (wish_list_item.user_id=:userId OR :userId IS NULL)) AS is_in_wish_list
    FROM `product` AS p WHERE 
        (:query IS NULL OR instr(lower(p.title), lower(:query)) OR instr(lower(p.description), lower(:query))) and
        (:categoryId IS NULL OR p.category_id = :categoryId) and
        (:discountRateMin IS NULL OR p.discount_rate >= :discountRateMin) and
        (:discountRateMax IS NULL OR p.discount_rate <= :discountRateMax) and
        (:dateAddedStart IS NULL OR p.date_added > :dateAddedStart) and
        (:dateAddedEnd IS NULL OR p.date_added < :dateAddedEnd) and
        (NOT :onlyInWishList OR p.id IN (SELECT product_id FROM wish_list_item WHERE user_id = :userId))
        LIMIT :limit"""
    )
    fun getProductsForUser(
        userId: Int? = null,
        query: String? = null,
        categoryId: Int? = null,
        discountRateMin: Double = 0.0,
        discountRateMax: Double = 1.0,
        dateAddedStart: Long? = null,
        dateAddedEnd: Long? = null,
        onlyInWishList: Boolean = false,
        limit: Int = -1     //-1 limit means no limit
    ): Flow<List<ProductWithData>>


    //JOIN `order` ON `order`.id=order_item.order_id
    //`order`.timestamp BETWEEN datetime('now', '-1 year') AND datetime('now')
    @Query(
        """SELECT *,
        (SELECT count(review.id) FROM review WHERE review.product_id = p.id) AS review_count,
        (SELECT avg(review.rating) FROM review WHERE review.product_id = p.id) AS review_score,
        (SELECT count(cart_item.id) > 0 FROM cart_item WHERE cart_item.product_id = p.id AND (cart_item.user_id=:userId OR :userId IS NULL)) AS is_in_cart,
        (SELECT count(wish_list_item.id) > 0 FROM wish_list_item WHERE wish_list_item.product_id = p.id  AND (wish_list_item.user_id=:userId OR :userId IS NULL)) AS is_in_wish_list,
        (SELECT count(order_item.id) FROM order_item WHERE order_item.product_id=p.id ) AS recent_orders_count
    FROM `product` AS p WHERE 
        (:query IS NULL OR instr(lower(p.title), lower(:query)) OR instr(lower(p.description), lower(:query))) and
        (:categoryId IS NULL OR p.category_id = :categoryId) and
        (:discountRateMin IS NULL OR p.discount_rate >= :discountRateMin) and
        (:discountRateMax IS NULL OR p.discount_rate <= :discountRateMax) and
        (:dateAddedStart IS NULL OR p.date_added > :dateAddedStart) and
        (:dateAddedEnd IS NULL OR p.date_added < :dateAddedEnd) and
        (NOT :onlyInWishList OR p.id IN (SELECT product_id FROM wish_list_item WHERE user_id = :userId))
        ORDER BY recent_orders_count DESC
        LIMIT :limit"""
    )
    fun getTrendingProducts(
        userId: Int? = null,
        query: String? = null,
        categoryId: Int? = null,
        discountRateMin: Double = 0.0,
        discountRateMax: Double = 1.0,
        dateAddedStart: Long? = null,
        dateAddedEnd: Long? = null,
        onlyInWishList: Boolean = false,
        limit: Int = -1
    ): Flow<List<ProductWithData>>

    @Insert
    fun insertAll(products: List<Product>)

    @Insert
    suspend fun insert(product: Product)

    @Update
    suspend fun update(product: Product)

    @Delete
    fun delete(product: Product)

    @Query("SELECT * FROM product WHERE id = :productId")
    suspend fun getProduct(productId: Int): Product

    @Query(
        """SELECT *,
        (SELECT count(review.id) FROM review WHERE review.product_id = p.id) as review_count,
        (SELECT avg(review.rating) FROM review WHERE review.product_id = p.id) AS review_score,
        (SELECT count(cart_item.id) > 0 FROM cart_item WHERE cart_item.product_id = p.id AND cart_item.user_id=:userId ) AS is_in_cart,
        (SELECT count(wish_list_item.id) > 0 FROM wish_list_item WHERE wish_list_item.product_id = p.id  AND wish_list_item.user_id=:userId) AS is_in_wish_list
        FROM `product` AS p WHERE p.id = :productId"""
    )
    suspend fun getProductForUser(productId: Int, userId: Int): ProductWithData

}