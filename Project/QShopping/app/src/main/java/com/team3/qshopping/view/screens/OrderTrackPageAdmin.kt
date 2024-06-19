package com.team3.qshopping.view.screens

import androidx.activity.ComponentActivity
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.team3.qshopping.R
import com.team3.qshopping.data.local.models.Order
import com.team3.qshopping.data.local.models.OrderItem
import com.team3.qshopping.data.remote.models.RemoteProduct
import com.team3.qshopping.ui.theme.ButtonTextColor
import com.team3.qshopping.ui.theme.LightGrey
import com.team3.qshopping.view.CustomAsyncImage
import com.team3.qshopping.view.composables.DeliveryStatus
import com.team3.qshopping.view.composables.buttons.BackButton
import com.team3.qshopping.view.composables.buttons.FilterButton
import com.team3.qshopping.view.composables.buttons.HollowButton
import com.team3.qshopping.view.composables.layout.Header
import com.team3.qshopping.viewmodel.OrderViewModel
import java.time.LocalDateTime
import java.time.ZoneOffset

//====================================================//
//====================LAZY LISTS =====================//
//====================================================//

@Composable
private fun ProductListAdmin(
    orderItems: List<OrderItem>,
    orderViewModel: OrderViewModel = viewModel(LocalContext.current as ComponentActivity)
) {
    val state = rememberLazyListState()
    LazyColumn(
        state = state
    ) {
        items(orderItems) { orderItem ->
            var product: RemoteProduct? by remember { mutableStateOf(null) }
            orderViewModel.getProductById(orderItem.productId) {
                product = it
            }
            ProductCardAdmin(product)
        }
    }
}

@Composable
fun OrderListAdmin(
    orders: List<Order>,
    selectedStatus: String,
    orderViewModel: OrderViewModel = viewModel(LocalContext.current as ComponentActivity)
) {
    val state = rememberLazyListState()
    LazyColumn(
        state = state
    ) {
        items(orders) { order ->
            if ((selectedStatus == "All") || (order.status == selectedStatus)) {
                val statefulOrder: Order by remember { mutableStateOf(order) }
                var orderItems: List<OrderItem> by remember { mutableStateOf(emptyList()) }
                orderViewModel.getOrderItems(order.id) {
                    orderItems = it
                }
                OrderCardAdmin(statefulOrder, orderItems)
            }
        }
    }
}

//===========================================================//
//====================CARDS COMPOSABLE=======================//
//===========================================================//

@Composable
fun ProductCardAdmin(product: RemoteProduct?) {
    val paddingModifier = Modifier.padding(10.dp)
    Box(
        modifier = paddingModifier
            .height(140.48.dp)
            .width(392.dp)
            .background(
                color = LightGrey, shape = RoundedCornerShape(20.dp)
            )

    ) {
        Row(modifier = Modifier.padding(10.dp)) {
            Box(
                modifier = Modifier
                    .height(118.dp)
                    .width(118.dp)
                    .background(
                        color = Color.White, shape = RoundedCornerShape(20.dp)
                    )
                    .align(Alignment.CenterVertically)
            ) {
                if (product != null) {
                    CustomAsyncImage(model = product.image)
                }
            }
            Spacer(modifier = Modifier.width(15.dp))
            Column(modifier = Modifier.align(Alignment.CenterVertically)) {
                Text(
                    text = "${product?.title}",
                    color = ButtonTextColor,
                    fontWeight = FontWeight.SemiBold,
                    style = MaterialTheme.typography.body1
                )
                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    text = "$${product?.price}",
                    color = Color(0xFF6A6D72),
                    style = MaterialTheme.typography.body2
                )

            }
        }
    }
}

@Composable
private fun OrderCardAdmin(
    order: Order,
    orderItems: List<OrderItem>,
    orderViewModel: OrderViewModel = viewModel(LocalContext.current as ComponentActivity)
) {
    var visible by remember { mutableStateOf(true) }
    var status by remember { mutableStateOf(order.status) }
    val paddingModifier = Modifier.padding(10.dp)

    Box(
        modifier = paddingModifier
            .height(180.48.dp)
            .width(392.dp)
            .background(
                color = LightGrey, shape = RoundedCornerShape(20.dp)
            )

    ) {
        Row(modifier = Modifier.padding(10.dp)) {
            Box(
                modifier = Modifier
                    .height(118.dp)
                    .width(118.dp)
                    .background(
                        color = Color.White, shape = RoundedCornerShape(20.dp)
                    )
                    .align(Alignment.CenterVertically)
            ) {
                Image(
                    painter = painterResource(R.drawable.boxicon),
                    contentDescription = "BoxIcon",
                    modifier = Modifier
                        .height(90.dp)
                        .width(90.dp)
                        .align(Alignment.Center)
                )
            }
            Spacer(modifier = Modifier.width(15.dp))
            Column(modifier = Modifier.align(Alignment.CenterVertically)) {
                Text(
                    text = "Order ID: ${order.id}",
                    color = ButtonTextColor,
                    fontWeight = FontWeight.SemiBold,
                    style = MaterialTheme.typography.body1
                )
                Spacer(modifier = Modifier.height(5.dp))
                Text(
                    text = "Status: ${order.status}",
                    color = Color(0xFF6A6D72),
                    style = MaterialTheme.typography.body2
                )
                Spacer(modifier = Modifier.height(5.dp))
                Text(
                    text = "Product Count: ${orderItems.size}",
                    color = Color(0xFF6A6D72),
                    style = MaterialTheme.typography.body2
                )
                Spacer(modifier = Modifier.height(15.dp))
                HollowButton(
                    text = "View Product",
                    onClick = { visible = !visible },
                )
            }
            Spacer(modifier = Modifier.width(15.dp))
            Box {
                DeliveryStatus(
                    status = status,
                    isAdmin = true,
                    onClick = { value: String ->
                        status = value
                        order.status = value
                        order.statusDate = LocalDateTime.now(ZoneOffset.UTC)
                        orderViewModel.updateOrder(order)
                    }
                )
            }
        }
    }
    AnimatedVisibility(!visible) {
        Card(
            modifier = Modifier
                .padding(10.dp)
                .heightIn(max = 200.dp),
            elevation = 5.dp,
            shape = RoundedCornerShape(10),
        ) {
            ProductListAdmin(orderItems)
        }
    }

}


@Composable
fun OrderTrackPageAdmin(
    navController: NavController,
    orderViewModel: OrderViewModel = viewModel()
) {
    //var value by remember { mutableStateOf("") }
    //val setValue: (String) -> Unit = { value = it }
    var filtersExpanded by remember { mutableStateOf(false) }
    val categories = listOf("All", "Processing", "Shipping", "Delivered")
    var selectedStatus by remember { mutableStateOf("All") }
    val orders = orderViewModel.orders

    Surface {
        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            Column {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(end = 10.dp),
                ) {
                    Header(start = {
                        BackButton(onBackPress = { navController.popBackStack() })
                    }, middle = {
                        Text(
                            text = "Orders",
                            style = MaterialTheme.typography.h2,
                            color = Color(0xA3323232),
                        )
                    }, end = {
                        FilterButton(onClick = { filtersExpanded = !filtersExpanded })
                    })
                    Row {
                        DropdownMenu(
                            expanded = filtersExpanded,
                            onDismissRequest = { filtersExpanded = false },
                            modifier = Modifier.padding(10.dp)
                        ) {
                            categories.forEach {
                                Spacer(modifier = Modifier.height(10.dp))
                                TextButton(
                                    onClick = {
                                        selectedStatus = it
                                        filtersExpanded = !filtersExpanded
                                    },
                                    modifier = Modifier
                                        .background(
                                            color = Color.LightGray,
                                            shape = RoundedCornerShape(4.dp)
                                        )
                                        .fillMaxWidth()
                                        .height(40.dp),
                                ) {
                                    Text(text = it, color = Color.White)
                                }

                            }
                        }
                    }

                }
                Spacer(modifier = Modifier.height(20.dp))
                /*Row(
                    modifier = Modifier.padding(
                        start = 10.dp, end = 10.dp, top = 15.dp, bottom = 15.dp
                    )
                ) {
                    SearchBar(value = value, onValueChange = setValue)
                }*/
                OrderListAdmin(orders, selectedStatus)
            }
        }
    }
}