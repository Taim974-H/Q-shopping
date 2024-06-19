package com.team3.qshopping.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.ListenerRegistration
import com.team3.qshopping.Globals
import com.team3.qshopping.data.remote.models.AnnotatedProduct
import com.team3.qshopping.data.repository.ProductRepository
import com.team3.qshopping.data.repository.WishListItemRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class WishListViewModel : ViewModel() {
    private var productsListener: ListenerRegistration? = null
    var wishListProducts: List<AnnotatedProduct> by mutableStateOf(emptyList())
        private set

    init {
        productsListener = WishListItemRepository.collection.addSnapshotListener { _, error ->
            if (error != null) {
                return@addSnapshotListener
            } else {
                setProducts()
            }
        }
    }

    private fun setProducts() {
        viewModelScope.launch(Dispatchers.IO) {
            wishListProducts = ProductRepository.getUserWishListProducts(Globals.user!!.id)
        }
    }

}