package com.team3.qshopping.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.ListenerRegistration
import com.team3.qshopping.Globals
import com.team3.qshopping.data.local.models.Order
import com.team3.qshopping.data.local.models.OrderItem
import com.team3.qshopping.data.remote.models.AnnotatedCartItem
import com.team3.qshopping.data.remote.models.CartItem
import com.team3.qshopping.data.repository.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.time.LocalDateTime

data class CartState(
    val total: Double = 0.0,
    val cartItems: List<AnnotatedCartItem> = emptyList(),
    val showAddressSettings: Boolean = false,
    val addressLine: String = "",
    val city: String = "",
    val country: String = "",
    val poBox: String = "",
    val phoneNumber: String = "",
    val showCardSettings: Boolean = false,
    val nameOnCard: String = "",
    val cardNumber: String = "",
    val cvv: String = "",
    val expiryYear: String = "",
    val expiryMonth: String = ""
)

class CartViewModel : ViewModel() {
    var uiState by mutableStateOf(CartState())
        private set

    private var cartItemListener: ListenerRegistration? = null

    fun onPlaceOrderClicked() {
        viewModelScope.launch(Dispatchers.IO) {
            val orderId = runBlocking { insertOrder() }
            uiState.cartItems.forEach { cartItemWithProduct ->
                runBlocking { insertOrderItem(orderId, cartItemWithProduct) }
            }
            runBlocking { CartItemRepository.deleteUserCartItems(Globals.user!!.id) }
            viewModelScope.launch(Dispatchers.Main) {
                uiState = CartState()
            }
        }
    }

    private suspend fun insertOrder(): Int {
        return OrderRepository.insert(
            Order(
                status = OrderStatus.PROCESSING.status,
                timestamp = LocalDateTime.now(),
                statusDate = LocalDateTime.now(),
                userId = Globals.user!!.id,
                addressLine = uiState.addressLine,
                city = uiState.city,
                country = uiState.country,
                poBox = uiState.poBox.toInt(),
                phoneNumber = uiState.phoneNumber.toLong(),
                nameOnCard = uiState.nameOnCard,
                cardNumber = uiState.cardNumber.toLong(),
                cvv = uiState.cvv.toInt(),
                expiryMonth = uiState.expiryMonth.toInt(),
                expiryYear = uiState.expiryYear.toInt()
            )
        )
    }

    private suspend fun insertOrderItem(orderId: Int, cartItem: AnnotatedCartItem) {
        if (cartItem.quantity > 0) {
            OrderItemRepository.insert(
                OrderItem(
                    price = cartItem.product.price,
                    quantity = cartItem.quantity,
                    productId = cartItem.product.id,
                    orderId = orderId
                )
            )
            val updatedProduct = cartItem.product
            updatedProduct.stock = updatedProduct.stock - cartItem.quantity
            ProductRepository.updateProduct(updatedProduct)
        }
    }

    fun deleteCartItem(cartItemId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            CartItemRepository.delete(cartItemId)
        }
    }

    fun updateCartItem(cartItem: CartItem) {
        viewModelScope.launch(Dispatchers.IO) {
            CartItemRepository.update(cartItem)
        }
    }

    fun onShowCardSettingsChange(showCardSettings: Boolean) {
        uiState = uiState.copy(showCardSettings = showCardSettings)
    }

    fun onNameOnCardChange(nameOnCard: String) {
        if (nameOnCard.all { it.isLetter() }) {
            uiState = uiState.copy(nameOnCard = nameOnCard)
        }
    }

    fun onCardNumberChange(cardNumber: String) {
        if (cardNumber.length < 17 && cardNumber.all { it.isDigit() }) {
            uiState = uiState.copy(cardNumber = cardNumber)
        }
    }

    fun onCvvChange(cvv: String) {
        if (cvv.length < 5 && cvv.all { it.isDigit() }) {
            uiState = uiState.copy(cvv = cvv)
        }
    }

    fun onExpiryYearChange(expiryYear: String) {
        if (expiryYear.length < 5 && expiryYear.all { it.isDigit() }) {
            uiState = uiState.copy(expiryYear = expiryYear)
        }
    }

    fun onExpiryMonthChange(expiryMonth: String) {
        if (expiryMonth.length < 3 && expiryMonth.all { it.isDigit() }) {
            uiState = uiState.copy(expiryMonth = expiryMonth)
        }
    }

    fun onShowAddressSettingsChange(showAddressSettings: Boolean) {
        uiState = uiState.copy(showAddressSettings = showAddressSettings)
    }

    fun onAddressLineChange(addressLine: String) {
        uiState = uiState.copy(addressLine = addressLine)
    }

    fun onCityChange(city: String) {
        if (city.all { it.isLetter() }) {
            uiState = uiState.copy(city = city)
        }
    }

    fun onCountryChange(country: String) {
        if (country.all { it.isLetter() }) {
            uiState = uiState.copy(country = country)
        }
    }

    fun onPOBoxChange(poBox: String) {
        if (poBox.length < 6 && poBox.all { it.isDigit() }) {
            uiState = uiState.copy(poBox = poBox)
        }
    }

    fun onPhoneNumberChange(phoneNumber: String) {
        if (phoneNumber.length < 9 && phoneNumber.all { it.isDigit() }) {
            uiState = uiState.copy(phoneNumber = phoneNumber)
        }
    }

    fun addressIsSet(): Boolean {
        val addressLinePredicate = uiState.addressLine.isNotBlank()
        val cityPredicate = uiState.city.isNotBlank()
        val countryPredicate = uiState.country.isNotBlank()
        val pOBoxPredicate = uiState.poBox.isNotBlank()
        val phoneNumberPredicate = uiState.phoneNumber.isNotBlank()

        return addressLinePredicate && cityPredicate && countryPredicate && pOBoxPredicate && phoneNumberPredicate
    }

    fun cardIsSet(): Boolean {
        val nameOnCardPredicate = uiState.nameOnCard.isNotBlank()
        val cardNumberPredicate = uiState.cardNumber.isNotBlank()
        val expiryMonthPredicate = uiState.expiryMonth.isNotBlank()
        val expiryYearPredicate = uiState.expiryYear.isNotBlank()
        val cardCVVPredicate = uiState.cvv.isNotBlank()

        return nameOnCardPredicate && cardNumberPredicate && expiryMonthPredicate && expiryYearPredicate && cardCVVPredicate
    }

    init {
        cartItemListener = CartItemRepository.collection
            .whereEqualTo("userId", Globals.user!!.id)
            .addSnapshotListener { _, error ->
                if (error != null) {
                    return@addSnapshotListener
                } else {
                    setCartItems()
                }
            }
    }

    private fun setCartItems() {
        viewModelScope.launch(Dispatchers.IO) {
            val annotatedCartItems = CartItemRepository.getUserCartItems(Globals.user!!.id)
            val total = annotatedCartItems.sumOf { it.product.price * it.quantity }

            uiState = uiState.copy(
                total = total,
                cartItems = annotatedCartItems
            )
        }
    }

    override fun onCleared() {
        println("*******************************")
        println("clearing cart item listener")
        super.onCleared()
        cartItemListener?.remove()
    }
}