package com.team3.qshopping.view.nav

import androidx.annotation.DrawableRes
import com.team3.qshopping.R

enum class Screen(
    val route: String,
    val parameterizedRoute: String = route,
    val title: String? = null,
    @DrawableRes
    val icon: Int? = null,
    val noPadding: Boolean = false,
) {
    Address(
        route = "address",
        title = "Address"
    ),

    Profile(
        route = "Profile",
        title = "Profile",
        icon = R.drawable.ic_person
    ),

    Home(
        route = "home",
        title = "Home",
        icon = R.drawable.ic_home
    ),

    Category(
        route = "category",
        parameterizedRoute = "category/{categoryId}",
        title = "Category"
    ),

    Product(route = "product/{productId}"),
    ProductAdmin(route = "productAdmin/{productId}"),

    Orders(
        route = "orders",
        title = "Orders",
        icon = R.drawable.ic_orders_logo
    ),

    Favourites(
        route = "favourites",
        title = "Favourites",
        icon = R.drawable.ic_heart
    ),

    Cart(
        route = "cart",
        title = "Cart",
        icon = R.drawable.ic_shopping_bag
    ),

    CardInfo(
        route = "CardInfo",
        title = "Card Info",
        icon = R.drawable.ic_checkout_card
    ),

    AddProduct(
        route = "addProduct",
        title = "Add Product"
    ),

    UpdateProduct(
        route = "updateProduct/{productId}",
        title = "Update Product"
    ),

    AdminReport(
        route = "AdminReport",
        title = "Admin Report",
        icon = R.drawable.ic_chart,
        noPadding = true
    );

    companion object {
        private val map = Screen.values().associateBy(Screen::route)
        fun fromRoute(route: String?): Screen? = map[route]
    }
}
