package com.team3.qshopping.view.composables

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp


//adapted from https://stackoverflow.com/a/68143597/14200676
@Composable
fun Table(
    modifier: Modifier,
    columns: Int,
    rows: Int,
    headerRow: @Composable (Int) -> String,
    dataRows: @Composable (Int, Int) -> Unit,
    footerRow: @Composable (Int) -> Unit = {},
) {
    // The LazyColumn will be our table. Notice the use of the weights below
    LazyRow(
        modifier = Modifier
            .padding(16.dp)
            .height(100.dp)
            .verticalScroll(rememberScrollState())
            .then(modifier)
    ) {
        items(columns) { colIndex ->
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp),
                verticalArrangement = Arrangement.spacedBy(2.dp)
            ) {
                Text(
                    text = headerRow(colIndex),
                    style = MaterialTheme.typography.h4,
                    textAlign = TextAlign.Center,
                    color = Color.Gray
                )
                Spacer(modifier = Modifier.height(8.dp))
                for (rowIndex in 0 until rows)
                    Box { dataRows(rowIndex, colIndex) }
                Spacer(modifier = Modifier.height(8.dp))
                footerRow(colIndex)
            }
        }
    }
}

@Composable
@Preview
fun TablePreview() {
    Surface {
        Table(
            modifier = Modifier,
            rows = 10,
            columns = 5,
            headerRow = { index ->
                when (index) {
                    0 -> "Product ID"
                    1 -> "Product title"
                    2 -> "Discounted Unit price"
                    3 -> "Quantity"
                    4 -> "Price"
                    else -> ""
                }
            },
            dataRows =
            { _, columnIndex ->
//            orderItems[rowIndex].orderId.toString()
                when (columnIndex) {
                    0 -> Text(text = "<Product ID>")
                    1 -> Text(text = "<title>")
                    2 -> Text(text = "<Discounted Unit price>")
                    3 -> Text(text = "<Quantity>")
                    4 -> Text(text = "<Price>")
                }
            }
        )
    }
}