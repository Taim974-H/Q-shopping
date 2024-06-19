package com.team3.qshopping.view.nav

import androidx.annotation.DrawableRes
import com.team3.qshopping.R

sealed class Category (
    val id: Int,
    val name: String,
    @DrawableRes
    val icon: Int
) {
    object Electronics: Category(
        id = 1,
        name = "Electronics",
        icon = R.drawable.ic_devices
    )
    object Jewelery: Category(
        id = 2,
        name = "Jewelery",
        icon = R.drawable.ic_jewelry
    )
    object Men: Category(
        id = 3,
        name = "Men's clothing",
        icon = R.drawable.ic_men_clothes
    )
    object Women: Category(
        id = 4,
        name = "Women's clothing",
        icon = R.drawable.ic_women_clothes
    )
}

val categories: List<Category> = listOf<Category>(
    Category.Electronics,
    Category.Jewelery,
    Category.Men,
    Category.Women
)