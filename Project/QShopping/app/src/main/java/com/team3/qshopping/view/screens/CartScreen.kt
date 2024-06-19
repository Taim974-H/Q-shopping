package com.team3.qshopping.view.screens

import android.annotation.SuppressLint
import android.widget.Toast
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import com.google.android.material.R.drawable.material_ic_keyboard_arrow_right_black_24dp
import com.team3.qshopping.R
import com.team3.qshopping.data.remote.models.AnnotatedCartItem
import com.team3.qshopping.ui.theme.ButtonGradientEndColor
import com.team3.qshopping.ui.theme.ButtonGradientStartColor
import com.team3.qshopping.ui.theme.ButtonTextColor
import com.team3.qshopping.ui.theme.LightGrey
import com.team3.qshopping.view.CustomAsyncImage
import com.team3.qshopping.view.angledGradientBackground
import com.team3.qshopping.view.composables.buttons.BackButton
import com.team3.qshopping.view.composables.buttons.GradientButton
import com.team3.qshopping.view.composables.inputFields.ElevatedTextField
import com.team3.qshopping.view.composables.layout.Header
import com.team3.qshopping.view.nav.Screen
import com.team3.qshopping.viewmodel.CartViewModel
import kotlin.math.max
import kotlin.math.min
import kotlin.math.roundToInt

@Composable
fun CartScreen(
    navController: NavController,
    cartViewModel: CartViewModel = viewModel()
) {
    val openDialog = remember { mutableStateOf(false) }
    val total = cartViewModel.uiState.total
    val cartItems = cartViewModel.uiState.cartItems

    Column(
        modifier = Modifier.verticalScroll(rememberScrollState())
    ) {
        OrderScreenHeader(navController)
        if (cartItems.isEmpty()) {
            EmptyScreen()
        } else {
            OrderFrame(cartItems, cartViewModel)
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Bottom
            ) {
                PaymentFrame {
                    //navController.navigate(Screen.CardInfo.route)
                    cartViewModel.onShowCardSettingsChange(true)
                }
                DeliveryFrame {
                    //navController.navigate(Screen.Address.route)
                    cartViewModel.onShowAddressSettingsChange(true)
                }
                OrderSummary(total)
                Spacer(modifier = Modifier.height(10.dp))
                PlaceOrder(openDialog = openDialog, navController, cartViewModel)
                Spacer(modifier = Modifier.height(10.dp))
            }
        }
    }

    if (cartViewModel.uiState.showAddressSettings) {
        AddressSettingsDialog(cartViewModel = cartViewModel)
    }
    if (cartViewModel.uiState.showCardSettings) {
        CardSettingsDialog(cartViewModel = cartViewModel)
    }
}

@Composable
fun EmptyScreen() {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "-- No items in Cart --",
            style = MaterialTheme.typography.h2,
            color = Color.DarkGray
        )
    }
}

@Composable
fun OrderScreenHeader(navController: NavController) {
    Header(
        start = { BackButton { navController.popBackStack() } },
        middle = {
            Text(
                text = "Cart",
                style = MaterialTheme.typography.h2,
                color = Color(0xA3323232),
            )
        }
    )
}

@Composable
fun CartItemsList(cartItems: List<AnnotatedCartItem>, cartViewModel: CartViewModel) {
    val state = rememberLazyListState()
    LazyColumn(state = state) {
        items(cartItems) { cartItem ->
            ItemCard(cartItem, cartViewModel)
        }
    }
}

@Composable
fun ItemCard(
    annotatedCartItem: AnnotatedCartItem,
    cartViewModel: CartViewModel
) {
    val product = annotatedCartItem.product

    val paddingModifier = Modifier.padding(10.dp)
    var count by remember { mutableStateOf(annotatedCartItem.quantity) }
    val totalPrice = product.price * count

    Box(
        modifier = paddingModifier
            .fillMaxWidth()
            .background(
                color = LightGrey,
                shape = RoundedCornerShape(20.dp)
            )
    ) {
        Row(modifier = Modifier.padding(10.dp)) {
            Box(
                modifier = Modifier
                    .align(Alignment.CenterVertically)
                    .background(
                        color = Color.White,
                        shape = RoundedCornerShape(20.dp)
                    )
                    .height(120.dp)
                    .width(120.dp)
            ) {
                CustomAsyncImage(model = product.image)
            }
            Spacer(modifier = Modifier.width(15.dp))
            Column(modifier = Modifier.padding(top = 9.dp)) {
                Text(
                    text = product.title,
                    color = ButtonTextColor,
                    fontWeight = FontWeight.SemiBold,
                    style = MaterialTheme.typography.body1
                )
                Spacer(modifier = Modifier.height(19.dp))

                Text(
                    text = "$$totalPrice",
                    color = Color(0xFF6A6D72),
                    style = MaterialTheme.typography.body2
                )
                Spacer(modifier = Modifier.height(15.dp))
                Row {
                    RemoveOneLess {
                        annotatedCartItem.quantity = (max(0, annotatedCartItem.quantity - 1))
                        cartViewModel.updateCartItem(annotatedCartItem.cartItem)
                        count = annotatedCartItem.quantity
                    }
                    Spacer(modifier = Modifier.width(5.dp))
                    Crossfade(
                        targetState = count,
                        modifier = Modifier.align(Alignment.CenterVertically)
                    ) {
                        Text(
                            text = "$it",
                            style = MaterialTheme.typography.body2,
                            color = Color(0xFF6A6D72),
                            fontSize = 15.sp,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.widthIn(min = 24.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(5.dp))
                    AddOneMore {
                        annotatedCartItem.quantity =
                            (min(annotatedCartItem.quantity + 1, product.stock))
                        cartViewModel.updateCartItem(annotatedCartItem.cartItem)
                        count = annotatedCartItem.quantity
                    }
                    Spacer(modifier = Modifier.width(45.dp))

                    IconButton(
                        onClick = { cartViewModel.deleteCartItem(annotatedCartItem.id) },
                        modifier = Modifier
                            .statusBarsPadding()
                            .padding(5.dp)
                            .size(20.dp)
                    ) {
                        Image(
                            painter = painterResource(R.drawable.ic_delete),
                            contentDescription = "Delete Card",
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun OrderFrame(cartItems: List<AnnotatedCartItem>, cartViewModel: CartViewModel) {
    Card(
        elevation = 3.dp,
        shape = RoundedCornerShape(10),
        modifier = Modifier
            .heightIn(0.dp, 420.dp)
            .padding(10.dp)
    ) {
        CartItemsList(cartItems, cartViewModel)
    }
}

@Composable
fun AddOneMore(updateCount: () -> Unit) {
    IconButton(
        onClick = { updateCount() },
        modifier = Modifier
            .statusBarsPadding()
            .background(
                color = Color(0xFFCACACA),
                shape = CircleShape
            )
            .padding(5.dp)
            .size(20.dp)
    ) {
        Image(
            painter = painterResource(R.drawable.ic_plus),
            contentDescription = "Add 1",
        )
    }
}

@Composable
fun RemoveOneLess(updateCount: () -> Unit) {
    IconButton(
        onClick = { updateCount() },
        modifier = Modifier
            .statusBarsPadding()
            .background(
                color = Color(0xFFCACACA),
                shape = CircleShape
            )
            .padding(5.dp)
            .size(20.dp)
    ) {
        Image(
            painter = painterResource(R.drawable.ic_minus),
            contentDescription = "Remove 1"
        )
    }
}

@SuppressLint("PrivateResource")
@Composable
fun PaymentFrame(onClick: () -> Unit) {
    TextButton(
        onClick = { onClick() },
        modifier = Modifier
            .fillMaxWidth()
            .clip(shape = RoundedCornerShape(30))
            .padding(start = 10.dp, end = 10.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "Payment Method",
                    fontSize = 18.sp,
                    color = Color(0xFF8F959E),
                    style = MaterialTheme.typography.subtitle1,
                )
                Spacer(modifier = Modifier.width(5.dp))
                Image(
                    painter = painterResource(R.drawable.moneyicon),
                    contentDescription = "PaymentIcon",
                    modifier = Modifier.size(30.dp)
                )
            }
            Icon(
                painter = painterResource(material_ic_keyboard_arrow_right_black_24dp),
                contentDescription = "Icon",
                tint = Color(0xFF8F959E)
            )
        }
    }
}

@SuppressLint("PrivateResource")
@Composable
fun DeliveryFrame(onClick: () -> Unit) {
    TextButton(
        onClick = { onClick() },
        modifier = Modifier
            .clip(shape = RoundedCornerShape(30))
            .fillMaxWidth()
            .padding(10.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "Delivery Address",
                    fontSize = 18.sp,
                    color = Color(0xFF8F959E),
                    style = MaterialTheme.typography.subtitle1,
                )
                Spacer(modifier = Modifier.width(5.dp))
                Image(
                    painter = painterResource(R.drawable.addressicon),
                    contentDescription = "AddressIcon",
                    modifier = Modifier.size(30.dp)
                )
            }
            Icon(
                painter = painterResource(material_ic_keyboard_arrow_right_black_24dp),
                contentDescription = "Icon",
                tint = Color(0xFF8F959E)
            )
        }
    }
}

@Composable
fun OrderSummary(total: Double) {
    Row(
        modifier = Modifier
            .padding(start = 17.dp)
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = "Total",
            fontSize = 18.sp,
            fontWeight = FontWeight.SemiBold,
            color = Color(0xFF5A5A5A),
            style = MaterialTheme.typography.subtitle1,
        )
        Text(
            text = "$${(total * 100.0).roundToInt() / 100.0}",
            fontSize = 17.sp,
            color = Color(0xFFB8B8B8),
            style = MaterialTheme.typography.subtitle1,
        )
    }
}

@Composable
fun PlaceOrder(
    openDialog: MutableState<Boolean>,
    navController: NavController,
    cartViewModel: CartViewModel
) {
    Row(
        horizontalArrangement = Arrangement.Center,
        modifier = Modifier.fillMaxWidth()
    ) {
        val context = LocalContext.current
        TextButton(
            onClick = {
                if (!cartViewModel.addressIsSet()) {
                    Toast.makeText(
                        context,
                        "Missing address details",
                        Toast.LENGTH_SHORT
                    ).show()
                } else if (!cartViewModel.cardIsSet()) {
                    Toast.makeText(
                        context,
                        "Missing payment method details",
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    openDialog.value = true
                }
            },
            modifier = Modifier
                .width(180.dp)
                .clip(shape = RoundedCornerShape(10.dp))
                .height(48.dp)
                .angledGradientBackground(
                    degrees = 280f,
                    colors = listOf(
                        ButtonGradientStartColor,
                        ButtonGradientEndColor,
                    )
                ),
        ) {
            if (openDialog.value) {
                AlertDialog(
                    modifier = Modifier.width(300.dp),
                    title = {
                        Image(
                            painter = painterResource(R.drawable.ic_confirm5),
                            contentDescription = "ConfirmOrder",
                            modifier = Modifier
                                .height(300.dp)
                                .fillMaxWidth()
                        )
                    },
                    onDismissRequest = {},
                    confirmButton = { },
                    dismissButton = {
                        Row(
                            horizontalArrangement = Arrangement.Center,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 25.dp)
                        ) {
                            TextButton(
                                onClick = {
                                    navController.navigate(Screen.Home.route) {
                                        popUpTo(navController.graph.findStartDestination().id)
                                        launchSingleTop = true
                                    }
                                    cartViewModel.onPlaceOrderClicked()
                                },
                                modifier = Modifier
                                    .width(200.dp)
                                    .clip(shape = RoundedCornerShape(10.dp))
                                    .height(48.dp)
                                    .angledGradientBackground(
                                        degrees = 280f,
                                        colors = listOf(
                                            ButtonGradientStartColor,
                                            ButtonGradientEndColor,
                                        )
                                    ),
                            ) {
                                Text(
                                    text = "Continue Shopping!",
                                    style = MaterialTheme.typography.button,
                                    color = ButtonTextColor
                                )

                            }
                        }
                    },
                    shape = RoundedCornerShape(50.dp)
                )
            }
            Text(
                text = "Place Order",
                style = MaterialTheme.typography.button,
                color = ButtonTextColor
            )
            Spacer(modifier = Modifier.width(10.dp))
            Icon(
                painter = painterResource(R.drawable.ic_shopping_bag),
                tint = ButtonTextColor,
                contentDescription = "Place Order button "
            )
        }
    }
}

@Composable
fun AddressSettingsDialog(cartViewModel: CartViewModel) {
    Dialog(
        onDismissRequest = { cartViewModel.onShowAddressSettingsChange(false) }
    ) {
        Surface(
            modifier = Modifier.clip(RoundedCornerShape(10.dp))
        ) {
            Column(
                modifier = Modifier.padding(30.dp),
                verticalArrangement = Arrangement.spacedBy(30.dp)
            ) {
                ElevatedTextField(
                    value = cartViewModel.uiState.addressLine,
                    onValueChange = { cartViewModel.onAddressLineChange(it) },
                    placeholder = "Address Line"
                )
                ElevatedTextField(
                    value = cartViewModel.uiState.city,
                    onValueChange = { cartViewModel.onCityChange(it) },
                    placeholder = "City"
                )
                ElevatedTextField(
                    value = cartViewModel.uiState.country,
                    onValueChange = { cartViewModel.onCountryChange(it) },
                    placeholder = "Country"
                )
                ElevatedTextField(
                    value = cartViewModel.uiState.poBox,
                    onValueChange = { cartViewModel.onPOBoxChange(it) },
                    placeholder = "P. O. Box",
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )
                ElevatedTextField(
                    value = cartViewModel.uiState.phoneNumber,
                    onValueChange = { cartViewModel.onPhoneNumberChange(it) },
                    placeholder = "Phone Number",
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )
                GradientButton(text = "Save") {
                    cartViewModel.onShowAddressSettingsChange(false)
                }
            }
        }
    }
}

@Composable
fun CardSettingsDialog(cartViewModel: CartViewModel) {
    Dialog(
        onDismissRequest = { cartViewModel.onShowCardSettingsChange(false) }
    ) {
        Surface(
            modifier = Modifier.clip(RoundedCornerShape(10.dp))
        ) {
            Column(
                modifier = Modifier.padding(30.dp),
                verticalArrangement = Arrangement.spacedBy(30.dp)
            ) {
                ElevatedTextField(
                    value = cartViewModel.uiState.nameOnCard,
                    onValueChange = { cartViewModel.onNameOnCardChange(it) },
                    placeholder = "Name on Card"
                )
                ElevatedTextField(
                    value = cartViewModel.uiState.cardNumber,
                    onValueChange = { cartViewModel.onCardNumberChange(it) },
                    placeholder = "Card Number",
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    ElevatedTextField(
                        value = cartViewModel.uiState.expiryMonth,
                        onValueChange = { cartViewModel.onExpiryMonthChange(it) },
                        placeholder = "MM",
                        fillMaxWidth = false,
                        modifier = Modifier.width(70.dp),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                    )
                    ElevatedTextField(
                        value = cartViewModel.uiState.expiryYear,
                        onValueChange = { cartViewModel.onExpiryYearChange(it) },
                        placeholder = "YYYY",
                        fillMaxWidth = false,
                        modifier = Modifier.width(90.dp),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                    )
                    ElevatedTextField(
                        value = cartViewModel.uiState.cvv,
                        onValueChange = { cartViewModel.onCvvChange(it) },
                        placeholder = "CVV",
                        fillMaxWidth = false,
                        modifier = Modifier.width(70.dp),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                    )
                }
                GradientButton(text = "Save") {
                    cartViewModel.onShowCardSettingsChange(false)
                }
            }
        }
    }
}