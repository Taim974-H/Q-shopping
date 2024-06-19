package com.team3.qshopping.view.screens

import androidx.activity.ComponentActivity
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.team3.qshopping.Globals
import com.team3.qshopping.R
import com.team3.qshopping.data.local.models.Address
import com.team3.qshopping.ui.theme.GlowingButtonTextColor
import com.team3.qshopping.ui.theme.H1Color
import com.team3.qshopping.view.composables.HyperlinkText
import com.team3.qshopping.view.composables.buttons.CancelSaveButtonsRow
import com.team3.qshopping.view.composables.inputFields.MultipleElevatedTextFields
import com.team3.qshopping.viewmodel.AddressViewModel


@Composable
fun Address(
    navController: NavController,
    addressViewModel: AddressViewModel = viewModel(LocalContext.current as ComponentActivity)
) {
    val addressLine = remember { mutableStateOf("") }
    val city = remember { mutableStateOf("") }
    val state = remember { mutableStateOf("") }
    val country = remember { mutableStateOf("") }
    val zipCode = remember { mutableStateOf("") }
    val phoneNumber = remember { mutableStateOf("") }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.padding(top = 30.dp, start = 50.dp, bottom = 50.dp, end = 50.dp)
    ) {
        AddressInfoHeader()
        Spacer(modifier = Modifier.height(15.dp))
        MultipleElevatedTextFields(
            inputFields = mapOf(
                "Address Name (Ex: Home)" to addressLine,
                "City" to city,
                "State" to state,
                "Country" to country,
                "Zip code" to zipCode,
                "Phone number" to phoneNumber
            )
        )
    }
    Column(
        verticalArrangement = Arrangement.Bottom,
        modifier = Modifier
            .fillMaxSize()
            .padding(bottom = 10.dp, start = 50.dp, end = 50.dp)
    ) {
        CancelSaveButtonsRow(
            onCancel = { navController.popBackStack() },
            onSave = {
                val isAllInitialized: Boolean = isAllInitialized(
                    listOf(
                        addressLine, city, state,
                        country, zipCode, phoneNumber
                    )
                )

                if (isAllInitialized) {
                    addressViewModel.addAddress(
                        Address(
                            userId = Globals.user!!.id,
                            state = state.value,
                            addressLine = addressLine.value,
                            city = city.value,
                            country = country.value,
                            phoneNumber = phoneNumber.value,
                            zipCode = zipCode.value
                        )
                    )
                    navController.popBackStack()
                }
            }
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
fun AddressInfoHeader() {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = "Address", color = H1Color, style = MaterialTheme.typography.h1
        )
        Image(
            painter = painterResource(R.drawable.addressicon),
            contentDescription = "Visa logo",
            modifier = Modifier.size(60.dp)
        )
    }
}

fun <String> isAllInitialized(variables: List<MutableState<String>>): Boolean {
    return !variables.any { it.value == "" }
}