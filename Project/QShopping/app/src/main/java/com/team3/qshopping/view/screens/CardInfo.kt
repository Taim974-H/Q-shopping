package com.team3.qshopping.view.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.team3.qshopping.R
import com.team3.qshopping.ui.theme.GlowingButtonTextColor
import com.team3.qshopping.ui.theme.H1Color
import com.team3.qshopping.view.composables.HyperlinkText
import com.team3.qshopping.view.composables.buttons.CancelSaveButtonsRow
import com.team3.qshopping.view.composables.inputFields.MultipleElevatedTextFields

@Composable
fun CardInfoScreen(navController: NavController) {

    val cardName = remember { mutableStateOf("") }
    val cardOwner = remember { mutableStateOf("") }
    val cardNumber = remember { mutableStateOf("") }
    var cardExpiry by remember { mutableStateOf("") }
    var cardCVV by remember { mutableStateOf("") }
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.padding(top = 30.dp, start = 50.dp, bottom = 50.dp, end = 50.dp)
    ) {
        CardInfoHeader()
        MultipleElevatedTextFields(
            inputFields = mapOf(
                "Card name (Ex: Main Card)" to cardName,
                "Card Owner" to cardOwner,
                "Card Number" to cardNumber,
            )
        )
        Spacer(modifier = Modifier.height(15.dp))
        Row(
            horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()
        ) {
            TextField(
                modifier = Modifier
                    .width(110.dp)
                    .border(1.dp, MaterialTheme.colors.background, RoundedCornerShape(30))
                    .shadow(
                        elevation = 12.dp,
                        shape = RoundedCornerShape(30),
                        spotColor = Color(0xFFD28A00)
                    ),
                value = cardExpiry,
                placeholder = { Text("EXP") },
                singleLine = true,
                onValueChange = { cardExpiry = it },
                colors = TextFieldDefaults.textFieldColors(backgroundColor = MaterialTheme.colors.background)
            )
            TextField(
                modifier = Modifier
                    .width(110.dp)
                    .border(1.dp, MaterialTheme.colors.background, RoundedCornerShape(30))
                    .shadow(
                        elevation = 12.dp,
                        shape = RoundedCornerShape(30),
                        spotColor = Color(0xFFD28A00)
                    ),
                value = cardCVV,
                placeholder = { Text("CVV") },
                singleLine = true,
                onValueChange = { cardCVV = it },
                colors = TextFieldDefaults.textFieldColors(backgroundColor = MaterialTheme.colors.background)
            )
        }
    }
    Column(
        verticalArrangement = Arrangement.Bottom,
        modifier = Modifier
            .fillMaxSize()
            .padding(bottom = 10.dp, start = 50.dp, end = 50.dp)
    ) {
        CancelSaveButtonsRow(
            onCancel = { navController.popBackStack() },
            onSave = { navController.popBackStack() }
        )
        HyperlinkText(
            fullText = "By Proceeding you confirm that you agree with our Terms and Conditions",
            linkText = listOf("Terms and Conditions"),
            hyperlinks = listOf("https://terms-and-conditions-qshopping.netlify.app/"),
            linkTextColor = GlowingButtonTextColor,
            fontSize = 16.sp
        )
    }
}

@Composable
fun CardInfoHeader() {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = "Card Info.", color = H1Color, style = MaterialTheme.typography.h1
        )
        Image(
            painter = painterResource(R.drawable.ic_visalogo),
            contentDescription = "Visa logo",
            modifier = Modifier.size(90.dp)
        )
    }
}

