package com.team3.qshopping.view.screens

import android.widget.Toast
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.ShoppingCart
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import com.team3.qshopping.Globals
import com.team3.qshopping.R
import com.team3.qshopping.data.local.models.Review
import com.team3.qshopping.data.local.models.User
import com.team3.qshopping.data.remote.models.AnnotatedProduct
import com.team3.qshopping.ui.theme.LightGrey
import com.team3.qshopping.view.composables.DividerTextDividerRow
import com.team3.qshopping.view.composables.ProductImage
import com.team3.qshopping.view.composables.ReviewPopup
import com.team3.qshopping.view.composables.buttons.BackButton
import com.team3.qshopping.view.composables.buttons.GradientButton
import com.team3.qshopping.view.composables.buttons.SmallRectangularButton
import com.team3.qshopping.view.composables.layout.Header
import com.team3.qshopping.view.nav.Screen
import com.team3.qshopping.viewmodel.ProductCardViewModel
import com.team3.qshopping.viewmodel.ProductDetailViewModel
import com.team3.qshopping.viewmodel.customViewModelFactory
import kotlin.math.max
import kotlin.math.min

@Composable
fun ProductDetailScreen(
    navController: NavController,
    productId: Int
) {
    val productDetailViewModel: ProductDetailViewModel =
        viewModel(factory = customViewModelFactory {
            ProductDetailViewModel(productId)
        })
    val product by productDetailViewModel.product
    if (product != null) {
        val productCardViewModel: ProductCardViewModel =
            viewModel(factory = customViewModelFactory {
                ProductCardViewModel(product = product!!)
            })
        val productWithData = product!!
        val context = LocalContext.current
        val reviews = productCardViewModel.reviews
        val reviewsOrdered = reviews.sortedWith(
            compareBy { it.authorId != Globals.user!!.id }
        )
        var reviewId = 0
        val userReviewed: Boolean =
            reviews.any {
                if (it.authorId == Globals.user!!.id) {
                    reviewId = it.id
                    true
                } else {
                    false
                }
            }
        val rating = remember { mutableStateOf(0) }
        val title = remember { mutableStateOf("") }
        val text = remember { mutableStateOf("") }
        var filterPopupOpen by remember { mutableStateOf(false) }

        Surface(Modifier) {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                item {
                    Header(
                        start = { BackButton { navController.popBackStack() } },
                        middle = {
                            ProductReputation(
                                averageRating = productWithData.reviewScore,
                                totalNumberOfRatings = productWithData.reviewCount
                            )
                        },
                        end = { FavouriteButton() }
                    )

                    Spacer(modifier = Modifier.height(10.dp))
                    ProductImage(
                        productImageUrl = productWithData.image,
                        productName = productWithData.title
                    )

                    val count = remember { mutableStateOf(1) }

                    Spacer(modifier = Modifier.height(10.dp))
                    PriceXQuantity(
                        price = String.format(
                            "%.2f",
                            productWithData.price *
                                    (1 - productWithData.discountRate)
                        ).toDouble(),
                        maxQuantity = productWithData.stock,
                        count = count.value
                    ) { count.value = it }

                    Spacer(modifier = Modifier.height(15.dp))
                    AddToCartButton(productWithData) {
                        productCardViewModel.onCartButtonClicked(quantity = count.value)
                        if (!productWithData.isInCart) {
                            Toast.makeText(
                                context,
                                "Added to cart",
                                Toast.LENGTH_LONG
                            ).show()
                        }
                        navController.navigate(Screen.Home.route) {
                            popUpTo(navController.graph.findStartDestination().id)
                            launchSingleTop = true
                        }
                    }


                    if (!userReviewed) {
                        Spacer(modifier = Modifier.height(10.dp))
                        AddReviewButton {
                            productDetailViewModel.canReview(
                                productWithData.id,
                                Globals.user!!.id
                            ) { ownsProduct ->
                                if (ownsProduct) {
                                    filterPopupOpen = true
                                } else {
                                    Toast.makeText(
                                        context,
                                        "Only owners a product can review it",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            }
                        }
                    }

                    if (reviews.isNotEmpty()) {
                        Spacer(modifier = Modifier.height(25.dp))
                        Row(modifier = Modifier.padding(horizontal = 10.dp)) {
                            DividerTextDividerRow(text = "Reviews")
                        }
                    }
                }

                items(items = reviewsOrdered) { r ->
                    var author: User? by remember { mutableStateOf(null) }
                    productCardViewModel.getUserById(r.authorId) { author = it }
                    Review(
                        r, author,
                        onDeleteClick = {
                            productCardViewModel.deleteProductReview(r)
                        },
                        onEditClick = {
                            filterPopupOpen = true
                            //productCardViewModel.editProductReview(r)
                        },
                        loadCurrentReview = { loadedRating, loadedTitle, loadedText ->
                            rating.value = loadedRating
                            title.value = loadedTitle
                            text.value = loadedText
                        }
                    )
                }
            }

            ReviewPopup(
                pop = filterPopupOpen,
                onDismissRequest = { filterPopupOpen = false },
                rating = rating,
                title = title,
                text = text
            ) {
                filterPopupOpen = false
                productCardViewModel.insertProductReview(
                    Review(
                        id = reviewId,
                        authorId = Globals.user!!.id,
                        productId = productWithData.id,
                        rating = rating.value,
                        title = title.value,
                        text = text.value
                    )
                )
            }
        }
    }
}

@Composable
fun ProductReputation(averageRating: Double, totalNumberOfRatings: Int) {
    Row(
        modifier = Modifier.padding(horizontal = 10.dp),
        verticalAlignment = Alignment.Bottom
    ) {
        Image(
            painter = painterResource(R.drawable.ic_star),
            contentDescription = "star",
            modifier = Modifier.size(25.dp)
        )
        Spacer(modifier = Modifier.width(5.dp))
        Text(
            text = "$averageRating",
            style = MaterialTheme.typography.h5,
            fontWeight = FontWeight.Normal,
            fontSize = 20.sp,
        )
        Spacer(modifier = Modifier.width(10.dp))
        Text(
            text = "($totalNumberOfRatings)",
            style = MaterialTheme.typography.body1,
            color = Color.Gray,
        )
    }
}

@Composable
private fun FavouriteButton() {
    var isFavourite by remember { mutableStateOf(false) }
    IconButton(
        onClick = { isFavourite = !isFavourite },
        modifier = Modifier
            .statusBarsPadding()
            .padding(horizontal = 16.dp, vertical = 10.dp)
            .size(36.dp)
            .background(
                color = LightGrey,
                shape = CircleShape
            )
    ) {
        Icon(
            imageVector = if (isFavourite) Icons.Outlined.Favorite else Icons.Outlined.FavoriteBorder,
            tint = Color.Black,
            contentDescription = "Back"
        )
    }
}

@Composable
private fun PriceXQuantity(
    price: Double,
    maxQuantity: Int = 1,
    count: Int,
    onChange: (Int) -> Unit
) {
    val totalPrice = price * count
    Row(
        modifier = Modifier.padding(horizontal = 15.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Crossfade(
            targetState = totalPrice,
            modifier = Modifier.align(Alignment.CenterVertically)
        ) {
            Text(
                text = "$$it",
                style = MaterialTheme.typography.subtitle2,
                fontSize = 20.sp,
                color = Color.DarkGray
            )
        }
        Spacer(modifier = Modifier.weight(1f))

        SmallRectangularButton(imageDrawable = R.drawable.ic_minus) {
            onChange(max(min(1, maxQuantity), count - 1))
        }
        Spacer(modifier = Modifier.width(5.dp))
        Crossfade(
            targetState = count,
            modifier = Modifier.align(Alignment.CenterVertically)
        ) {
            Text(
                text = "$it",
                style = MaterialTheme.typography.subtitle2,
                fontSize = 20.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.widthIn(min = 24.dp)
            )
        }
        Spacer(modifier = Modifier.width(5.dp))
        SmallRectangularButton(imageDrawable = R.drawable.ic_plus) {
            onChange(min(count + 1, maxQuantity))
        }
    }
}

@Composable
private fun Rating(rating: Int) {
    Row {
        for (i in 0 until rating) {
            Image(
                painter = painterResource(R.drawable.ic_star),
                contentDescription = "Full Star",
                modifier = Modifier.size(18.dp)
            )
        }
        for (i in 0 until 5 - rating) {
            Image(
                painter = painterResource(R.drawable.ic_star),
                contentDescription = "Full Star",
                modifier = Modifier.size(18.dp),
                colorFilter = ColorFilter.tint(Color.Gray)
            )
        }
    }
}

@Composable
private fun Review(
    review: Review,
    author: User?,
    onDeleteClick: () -> Unit,
    onEditClick: () -> Unit,
    loadCurrentReview: (loadedRating: Int, loadedTitle: String, loadedText: String) -> Unit,
    reviewerImage: Int = R.drawable.ic_person
) {
    Column(modifier = Modifier.padding(horizontal = 10.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Image(
                    painter = painterResource(reviewerImage),
                    contentDescription = "Rater Profile Photo",
                    modifier = Modifier.size(40.dp)
                )
                Spacer(modifier = Modifier.width(5.dp))
                Column {
                    author?.fullName?.let {
                        Text(
                            text = it,
                            style = MaterialTheme.typography.subtitle2
                        )
                    }
                    Rating(review.rating)
                }
            }
            if (Globals.user!!.id == review.authorId) {
                loadCurrentReview(review.rating, review.title, review.text)
                Row(
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    SmallRectangularButton(R.drawable.ic_pen) { onEditClick() }
                    SmallRectangularButton(R.drawable.ic_delete) { onDeleteClick() }
                }
            }
        }
        var maxLines by remember { mutableStateOf(3) }
        Text(
            text = review.title,
            style = MaterialTheme.typography.body1,
            color = colorResource(R.color.black),
            maxLines = 1,
            modifier = Modifier.padding(horizontal = 5.dp)
        )
        Text(
            text = review.text,
            style = MaterialTheme.typography.body2,
            color = colorResource(R.color.black),
            maxLines = maxLines,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier
                .padding(horizontal = 5.dp)
                .clickable { maxLines += 5 },
        )
    }
}


@Composable
private fun AddToCartButton(product: AnnotatedProduct, onPress: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        val enabled = product.stock > 0
        GradientButton(
            text = if (enabled) {
                if (product.isInCart) {
                    "Remove from cart"
                } else {
                    "Add to cart"
                }
            } else {
                "Out of stock"
            },
            widthFraction = 0.93f,
            trailingIcon = if (enabled) {
                Icons.Outlined.ShoppingCart
            } else {
                null
            },
            enabled = enabled
        ) {
            onPress()
        }
    }
}

@Composable
private fun AddReviewButton(onPress: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        GradientButton(
            text = "Add Review",
            widthFraction = 0.93f,
            trailingIcon = Icons.Outlined.Add
        ) {
            onPress()
        }
    }
}