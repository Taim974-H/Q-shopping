package com.team3.qshopping.viewmodel

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.ListenerRegistration
import com.team3.qshopping.Globals
import com.team3.qshopping.data.local.models.Review
import com.team3.qshopping.data.local.models.User
import com.team3.qshopping.data.local.models.WishListItem
import com.team3.qshopping.data.local.models.embeded.ProductWithData
import com.team3.qshopping.data.remote.models.AnnotatedProduct
import com.team3.qshopping.data.repository.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ProductCardViewModel(product: AnnotatedProduct) : ViewModel() {
    var product: MutableState<AnnotatedProduct>
        private set
    var reviews: List<Review> by mutableStateOf(emptyList())
        private set
    private var reviewsListener: ListenerRegistration? = null

    init {
        this.product = mutableStateOf(product)
        reviewsListener = ReviewRepository.collection.addSnapshotListener { _, error ->
            if (error != null) {
                return@addSnapshotListener
            } else {
                setReviews()
            }
        }
    }

    private fun setReviews() {
        viewModelScope.launch(Dispatchers.IO) {
            reviews = ReviewRepository.getAllReviewsByProductId(product.value.id)
        }
    }

    fun getUserById(id: Int, setUser: (User?) -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            val user = UserRepository.getUserById(id)
            viewModelScope.launch(Dispatchers.Main) {
                setUser(user)
            }
        }
    }

    fun onCartButtonClicked(quantity: Int = 1) {
        viewModelScope.launch {
            if (!product.value.isInCart) {
                product.value = product.value.copy(isInCart = true)
                CartItemRepository.insert(Globals.user!!.id, product.value.id, quantity)
            } else {
                product.value = product.value.copy(isInCart = false)
                CartItemRepository.delete(Globals.user!!.id, product.value.id)
            }
        }
    }

    fun onWishListButtonClicked() {
        viewModelScope.launch {
            if (!product.value.isInWishList) {
                product.value = product.value.copy(isInWishList = true)
                WishListItemRepository.insert(WishListItem(Globals.user!!.id, product.value.id))
            } else {
                product.value = product.value.copy(isInWishList = false)
                WishListItemRepository.delete(Globals.user!!.id, product.value.id)
            }
        }
    }

    fun onDeleteButtonClicked(productWithData: ProductWithData) {
        viewModelScope.launch(Dispatchers.IO) {
            ProductRepository.deleteProduct(productWithData.product.id)
        }
    }

    fun onDeleteButtonClicked(productWithData: AnnotatedProduct) {
        viewModelScope.launch(Dispatchers.IO) {
            ProductRepository.deleteProduct(productWithData.id)
        }
    }

    fun insertProductReview(review: Review) {
        viewModelScope.launch(Dispatchers.IO) {
            ReviewRepository.create(review)
        }
    }

    fun editProductReview(review: Review) {
        viewModelScope.launch(Dispatchers.IO) {
            ReviewRepository.update(review)
        }
    }

    fun deleteProductReview(review: Review) {
        viewModelScope.launch(Dispatchers.IO) {
            ReviewRepository.delete(review)
        }
    }

    override fun onCleared() {
        super.onCleared()
        reviewsListener?.remove()
    }
}