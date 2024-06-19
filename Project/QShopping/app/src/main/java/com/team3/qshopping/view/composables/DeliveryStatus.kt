package com.team3.qshopping.view.composables

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.RadioButton
import androidx.compose.material.RadioButtonDefaults
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.team3.qshopping.R
import com.team3.qshopping.ui.theme.QShoppingTheme

@Composable
fun DeliveryStatus(status: String, isAdmin: Boolean = false, onClick: (String) -> Unit = {}) {
    val tint = Color(0xFF585858)
    val processing = R.drawable.ic_processing
    val shipping = R.drawable.ic_shipping
    val delivered = R.drawable.ic_delivered

    Column(
        Modifier
            .width(60.dp)
            .height(150.dp)
    ) {
        StatusGroup(status, "Processing", processing, tint, isAdmin, onClick)
        VerticalDots(tint)
        StatusGroup(status, "Shipping", shipping, tint, isAdmin, onClick)
        VerticalDots(tint)
        StatusGroup(status, "Delivered", delivered, tint, isAdmin, onClick)
    }
}

@Composable
fun StatusGroup(
    status: String,
    value: String,
    icon: Int,
    tint: Color,
    isAdmin: Boolean,
    onClick: (String) -> Unit
) {

    val selected =
        (value == "Processing") or ((status == "Shipping") and (value == "Shipping")) or ((status == "Delivered") and ((value == "Shipping") or (value == "Delivered")))
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 5.dp), Arrangement.SpaceBetween
    ) {
        RadioButton(
            enabled = isAdmin,
            selected = selected,
            onClick = { onClick(value) },
            colors = RadioButtonDefaults.colors(
                disabledColor = tint,
                selectedColor = tint,
                unselectedColor = tint
            ),
            modifier = Modifier.size(24.dp)
        )
        Icon(
            painter = painterResource(icon),
            tint = tint,
            contentDescription = "Icon representing the state '$value'",
            modifier = Modifier.size(24.dp)
        )
    }
}

@Composable
fun VerticalDots(tint: Color) {
    Row(Modifier.fillMaxWidth(), Arrangement.End) {
        Icon(
            painter = painterResource(R.drawable.ic_dots),
            tint = tint,
            contentDescription = "Icon of three vertical dots"
        )
    }
}

@Preview(showBackground = true)
@Composable
fun DeliveryStatusPreview() {
    var status by remember { mutableStateOf("Shipping") }
    val setStatus: (String) -> Unit = { status = it }

    val context = LocalContext.current // to show that it works
    val onClick = { value: String ->
        setStatus(value)
        // to show that it works
        Toast.makeText(context, "$value tapped", Toast.LENGTH_SHORT).show()
    }

    QShoppingTheme {
        Row(
            modifier = Modifier.padding(10.dp),
            horizontalArrangement = Arrangement.spacedBy(20.dp)
        ) {

            // Client
            DeliveryStatus(status = status)

            // Admin
            DeliveryStatus(status = status, isAdmin = true, onClick = onClick)

        }
    }
}