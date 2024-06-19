package com.team3.qshopping.view.screens

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import com.team3.qshopping.R
import com.team3.qshopping.ui.theme.GlowingButtonTextColor
import com.team3.qshopping.ui.theme.H1Color
import com.team3.qshopping.ui.theme.SubtitleTextColor
import com.team3.qshopping.view.composables.buttons.GradientButton
import com.team3.qshopping.view.composables.inputFields.ElevatedTextField
import com.team3.qshopping.viewmodel.RegisterViewModel

@Composable
fun RegistrationScreen(
    onRegister: () -> Unit
) {
    val registerViewModel = RegisterViewModel()

    Register(
        onRegister = onRegister,
        registerViewModel = registerViewModel
    )
}

@Composable
fun RegistrationHeader() {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = "Sign Up!",
            color = H1Color,
            style = MaterialTheme.typography.h1
        )
        Icon(
            painter = painterResource(R.drawable.ic_pen),
            contentDescription = "A pen",
            tint = H1Color,
            modifier = Modifier.size(60.dp)
        )
    }
}

@Composable
fun Register(
    onRegister: () -> Unit,
    registerViewModel: RegisterViewModel
) {
    if (registerViewModel.registered) {
        onRegister()
    }

    if (registerViewModel.uiState.message != null) {
        Toast.makeText(
            LocalContext.current,
            registerViewModel.uiState.message,
            Toast.LENGTH_SHORT
        ).show()
        registerViewModel.clearMessage()
    }

    Column(
        verticalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier
            .fillMaxSize()
            .padding(50.dp)
            .padding(bottom = 20.dp)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(50.dp)
        ) {
            RegistrationHeader()
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                ElevatedTextField(
                    value = registerViewModel.uiState.fullName,
                    onValueChange = { registerViewModel.onFullNameChange(it) },
                    placeholder = "Full Name",
                    singleLine = true,
                )
                ElevatedTextField(
                    value = registerViewModel.uiState.username,
                    onValueChange = { registerViewModel.onUsernameChange(it) },
                    placeholder = "Username",
                    singleLine = true,
                )
                ElevatedTextField(
                    value = registerViewModel.uiState.email,
                    onValueChange = { registerViewModel.onEmailChange(it) },
                    placeholder = "Email",
                    singleLine = true,
                )
                ElevatedTextField(
                    value = registerViewModel.uiState.password,
                    onValueChange = { registerViewModel.onPasswordChange(it) },
                    placeholder = "Password",
                    singleLine = true,
                )
            }
        }

        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
            GradientButton(text = "Register") {
                registerViewModel.signUp()
            }
            Text(
                buildAnnotatedString {
                    withStyle(style = SpanStyle(color = SubtitleTextColor)) {
                        append("Already a member? ")
                    }
                    withStyle(style = SpanStyle(color = GlowingButtonTextColor)) {
                        append("Login")
                    }
                },
                modifier = Modifier.clickable { onRegister() }
            )
        }

    }
}