package com.team3.qshopping.view.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.team3.qshopping.R
import com.team3.qshopping.data.remote.models.AnnotatedOrder
import com.team3.qshopping.data.repository.OrderStatus
import com.team3.qshopping.ui.theme.ButtonTextColor
import com.team3.qshopping.ui.theme.LightGrey
import com.team3.qshopping.utcToLocal
import com.team3.qshopping.view.composables.BarChart
import com.team3.qshopping.view.composables.Table
import com.team3.qshopping.view.composables.buttons.GradientButton
import com.team3.qshopping.view.composables.inputFields.CustomChoiceBox
import com.team3.qshopping.view.composables.inputFields.CustomDatePicker
import com.team3.qshopping.view.composables.layout.Header
import com.team3.qshopping.viewmodel.AdminReportViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import java.time.LocalDate

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun AdminReportScreen(
    navController: NavController,
    scaffoldState: ScaffoldState,
    scaffoldScope: CoroutineScope,
    adminReportViewModel: AdminReportViewModel = viewModel(),
) {
    Column()
    {
        Header(modifier = Modifier,
            start = {
                MenuButton {
                    scaffoldScope.launch { scaffoldState.drawerState.open() }
                }
            }, middle = {
                Text(
                    "Report",
                    style = MaterialTheme.typography.h2,
                    color = ButtonTextColor
                )
            })
        val bottomSheetState = rememberModalBottomSheetState(ModalBottomSheetValue.Hidden)
        val scope = rememberCoroutineScope()

        ModalBottomSheetLayout(
            modifier = Modifier.padding(horizontal = 0.dp),
            sheetState = bottomSheetState,
            sheetContent = {
                OrderReport(adminReportViewModel = adminReportViewModel)
            }
        ) {
            // Screen content
            Column(
                modifier = Modifier
                    .padding(start = 10.dp, end = 10.dp),
                verticalArrangement = Arrangement.spacedBy(15.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                OrdersFilter(
                    startDate = adminReportViewModel.startDate,
                    endDate = adminReportViewModel.endDate,
                    orderStatus = adminReportViewModel.orderStatus
                )
                GradientButton(text = "Generate") {
                    scope.launch {
                        adminReportViewModel.generateReport()
                        bottomSheetState.animateTo(ModalBottomSheetValue.Expanded)
                    }
                }
            }
        }
    }

}


@Composable
fun OrdersFilter(
    startDate: MutableState<LocalDate?>,
    endDate: MutableState<LocalDate?>,
    orderStatus: MutableState<OrderStatus>
) {
    Row(
        modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        CustomDatePicker(
            modifier = Modifier.weight(1f),
            date = startDate,
            placeholder = "Start Date",
        )
        CustomDatePicker(
            modifier = Modifier.weight(1f),
            date = endDate,
            placeholder = "End Date",
        )
    }
    CustomChoiceBox(
        currentChoice = orderStatus,
        choices = OrderStatus.values().associateWith { it.status },
        placeholder = "Select status"
    )
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
private fun OrderReport(adminReportViewModel: AdminReportViewModel) {
    val orders by adminReportViewModel.orders
    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        item {
            if (adminReportViewModel.orderStatus.value == OrderStatus.ALL) {
                BarChart(
                    data = adminReportViewModel.totalPriceByStatus.value.minus(OrderStatus.ALL)
                        .mapKeys { it.key.status },
                    title = "Products by status",
                    collapsable = true,
                    height = 200.dp,
                )
                Spacer(modifier = Modifier.height(8.dp))
                BarChart(
                    data = adminReportViewModel.countByStatus.value.minus(OrderStatus.ALL)
                        .mapValues { it.value.toDouble() }
                        .mapKeys { it.key.status },
                    title = "Total Price by status",
                    collapsable = true,
                    height = 200.dp,
                )
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
        item {
            Text("Orders", style = MaterialTheme.typography.h3)
            if (orders.isEmpty())
                Text("No orders found")
        }
        items(orders) { orderWithItems ->
            OrderReportCard(orderWithItems)
        }
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("Product Count")
                Text("${adminReportViewModel.productCount.value}")
            }
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("Total Price")
                Text("$%.2f".format(adminReportViewModel.totalPrice.value))
            }
        }
    }
}

@Composable
private fun OrderReportCard(
    order: AnnotatedOrder,
) {
    var productsExpanded by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .background(color = LightGrey, shape = RoundedCornerShape(20.dp))
            .fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.heightIn(100.dp)
        ) {
            Row(modifier = Modifier.padding(10.dp)) {
                Box(
                    modifier = Modifier
                        .background(
                            color = Color.White, shape = RoundedCornerShape(20.dp)
                        )
                        .align(Alignment.CenterVertically)
                ) {
                    Image(
                        painter = painterResource(R.drawable.boxicon),
                        contentDescription = "BoxIcon",
                        modifier = Modifier
                            .height(35.dp)
                            .width(35.dp)
                            .align(Alignment.Center)
                    )
                }
                Spacer(modifier = Modifier.width(15.dp))
                Column(
                    modifier = Modifier.align(Alignment.CenterVertically),
                    verticalArrangement = Arrangement.spacedBy(5.dp)
                ) {
                    Text(
                        text = "ID: ${order.id}",
                        color = ButtonTextColor,
                        fontWeight = FontWeight.SemiBold,
                        style = MaterialTheme.typography.body1
                    )
                    Text(
                        text = order.status,
                        color = Color(0xFF6A6D72),
                        style = MaterialTheme.typography.body2
                    )
                    Text(
                        text = "${utcToLocal(order.timestamp)}",
                        color = Color(0xFF6A6D72),
                        style = MaterialTheme.typography.body2
                    )
                }
            }
            Row(
                modifier = Modifier
                    .padding(start = 16.dp)
                    .clickable { productsExpanded = !productsExpanded },
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Icon(
                    modifier = Modifier.rotate(if (productsExpanded) 90f else 0f),
                    painter = painterResource(id = R.drawable.ic_arrowforward),
                    contentDescription = "Expand"
                )
                Text("Products")
            }
            AnimatedVisibility(productsExpanded) {
                OrderProductsTable(order)
            }
        }
    }
}


@Composable
private fun OrderProductsTable(order: AnnotatedOrder) {
    val orderItems = order.items
    val products = order.products
    Table(
        modifier = Modifier,
        rows = orderItems.size,
        columns = 5,
        headerRow = { index ->
            when (index) {
                0 -> "ID"
                1 -> "Title"
                2 -> "Unit price"
                3 -> "Quantity"
                4 -> "Price"
                else -> ""
            }
        },
        dataRows =
        { rowIndex, columnIndex ->
            val orderItem = orderItems[rowIndex]
            when (columnIndex) {
                0 -> Text(text = "${orderItem.productId}")
                1 -> Text(
                    modifier = Modifier.widthIn(max = 200.dp),
                    text = products[rowIndex].title,
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 1
                )
                2 -> Text(text = "$%.2f".format(orderItem.price))
                3 -> Text(text = "${orderItem.quantity}")
                4 -> Text(text = "$%.2f".format(orderItem.price * orderItem.quantity))
            }
        },
        footerRow = { columnIndex ->
            when (columnIndex) {
                0 -> Text(text = "Total")
                3 -> Text(text = "${order.itemCount}")
                4 -> Text(text = "$%.2f".format(order.totalPrice))
                else -> {}
            }
        }
    )
}
