package com.team3.qshopping.view.screens

import androidx.activity.ComponentActivity
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.team3.qshopping.data.local.models.Order
import com.team3.qshopping.data.local.models.OrderItem
import com.team3.qshopping.data.remote.models.RemoteProduct
import com.team3.qshopping.ui.theme.EggYolk
import com.team3.qshopping.ui.theme.GlowingButtonTextColor
import com.team3.qshopping.ui.theme.QShoppingTheme
import com.team3.qshopping.utcToLocal
import com.team3.qshopping.view.CustomAsyncImage
import com.team3.qshopping.view.composables.DeliveryStatus
import com.team3.qshopping.view.composables.buttons.BackButton
import com.team3.qshopping.view.composables.layout.Header
import com.team3.qshopping.viewmodel.OrderViewModel

@Composable
fun ProductListCustomer(
    orderItems: List<OrderItem>,
    orderViewModel: OrderViewModel = viewModel(LocalContext.current as ComponentActivity)
) {
    val state = rememberLazyListState()
    LazyRow(
        state = state
    ) {
        items(orderItems) { orderItem ->
            var product: RemoteProduct? by remember { mutableStateOf(null) }
            orderViewModel.getProductById(orderItem.productId) {
                product = it
            }
            ProductCardCustomer(product)
        }
    }
}

@Composable
fun ProductCardCustomer(product: RemoteProduct?) {
    Row(modifier = Modifier.padding(10.dp)) {
        Box(
            modifier = Modifier
                .height(118.dp)
                .width(70.dp)
                .background(
                    color = Color.White,
                    shape = RoundedCornerShape(20.dp)
                )
                .align(Alignment.CenterVertically)
        ) {
            if (product != null) {
                CustomAsyncImage(model = product.image)
            }
        }
    }
}

@Composable
fun OrderListCustomer(
    orders: List<Order>,
    orderViewModel: OrderViewModel = viewModel(LocalContext.current as ComponentActivity)
) {
    val state = rememberLazyListState()
    LazyColumn(
        state = state,
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        items(orders) { order ->
            var orderItems: List<OrderItem> by remember { mutableStateOf(emptyList()) }
            orderViewModel.getOrderItems(order.id) {
                orderItems = it
            }
            OrderCardCustomer(order, orderItems)
        }
    }
}

@Composable
fun OrderCardCustomer(order: Order, orderItems: List<OrderItem>) {
    val shimmerColors = listOf(
        EggYolk.copy(alpha = 0.6f),
        EggYolk.copy(alpha = 0.2f),
        EggYolk.copy(alpha = 0.6f),
    )
    val transition = rememberInfiniteTransition()
    val translateAnim = transition.animateFloat(
        initialValue = 0f,
        targetValue = 30000f,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = 6000,
                easing = FastOutSlowInEasing
            )
        )
    )
    val brush = Brush.linearGradient(
        colors = shimmerColors,
        start = Offset.Zero,
        end = Offset(x = translateAnim.value, y = translateAnim.value)
    )
    Modifier.padding(10.dp)


    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(brush, RoundedCornerShape(20.dp))
            .padding(15.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column {
            Text(
                modifier = Modifier.padding(start = 10.dp, bottom = 10.dp),
                text = "Order ID: ${order.id}",
                color = GlowingButtonTextColor,
                fontWeight = FontWeight.SemiBold,
                style = MaterialTheme.typography.body1
            )
            Row(
                modifier = Modifier
                    .width(270.dp)
                    .height(90.dp)
                    .background(
                        color = Color.White,
                        shape = RoundedCornerShape(20.dp)
                    )
                    .padding(2.dp)
            ) {
                ProductListCustomer(orderItems)
            }
            Column(
                modifier = Modifier.padding(top = 10.dp)
            ) {

                Text(
                    text = "Ordered on: ${utcToLocal(order.timestamp)}",
                    color = GlowingButtonTextColor,
                    style = MaterialTheme.typography.body2
                )
                if ((order.status == "Shipping") || (order.status == "Delivered")) {
                    Text(
                        text = "Status update: ${utcToLocal(order.statusDate!!)}",
                        color = GlowingButtonTextColor,
                        style = MaterialTheme.typography.body2
                    )
                }
            }
        }
        Column(
            modifier = Modifier.padding(start = 10.dp)
        ) {
            DeliveryStatus(status = order.status)
        }
    }

}

@Composable
fun OrderTrackPageCustomer(
    navController: NavController,
    orderViewModel: OrderViewModel = viewModel()
) {
    val orders = orderViewModel.orders

    QShoppingTheme {
        Surface {
            Box(modifier = Modifier.fillMaxSize()) {
                Column {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Header(
                            start = { BackButton(onBackPress = { navController.popBackStack() }) },
                            middle = {
                                Text(
                                    text = "Orders",
                                    style = MaterialTheme.typography.h2,
                                    color = Color(0xA3323232),
                                )
                            })
                    }
                    Spacer(modifier = Modifier.height(20.dp))
                    /*Row(
                        modifier = Modifier.padding(
                            start = 10.dp,
                            end = 10.dp,
                            top = 15.dp,
                            bottom = 15.dp
                        )
                    ) {
                        SearchBar(value = value, onValueChange = setValue)
                    }*/
                    OrderListCustomer(orders)
                }
            }
        }
    }
}