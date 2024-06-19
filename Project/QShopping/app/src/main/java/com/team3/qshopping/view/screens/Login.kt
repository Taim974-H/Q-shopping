package com.team3.qshopping.view.screens

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import com.team3.qshopping.R
import com.team3.qshopping.ui.theme.*
import com.team3.qshopping.view.composables.DividerTextDividerRow
import com.team3.qshopping.view.composables.buttons.GradientButton
import com.team3.qshopping.view.composables.inputFields.ElevatedTextField
import com.team3.qshopping.viewmodel.LoginViewModel

@Composable
fun LoginScreen(
    onRegistrationClick: () -> Unit,
    onSignIn: () -> Unit
) {
    val loginViewModel = LoginViewModel()

    Login(
        onAuthenticated = onSignIn,
        onRegister = onRegistrationClick,
        loginViewModel = loginViewModel
    )
}

@Composable
fun Login(
    onAuthenticated: () -> Unit,
    onRegister: () -> Unit,
    loginViewModel: LoginViewModel
) {
    if (loginViewModel.loggedIn) {
        onAuthenticated()
    }

    if (loginViewModel.uiState.message != null) {
        Toast.makeText(
            LocalContext.current,
            loginViewModel.uiState.message,
            Toast.LENGTH_SHORT
        ).show()
        loginViewModel.clearMessage()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(50.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Image(painterResource(R.drawable.ic_logo), "Q Shopping Logo")
        Text(
            text = "Login",
            modifier = Modifier.fillMaxWidth(),
            style = MaterialTheme.typography.h1,
            color = H1Color,
            textAlign = TextAlign.Center
        )
        ElevatedTextField(
            value = loginViewModel.uiState.email,
            onValueChange = { loginViewModel.onEmailChange(it) },
            placeholder = "Email",
            singleLine = true,
        )
        ElevatedTextField(
            value = loginViewModel.uiState.password,
            onValueChange = { loginViewModel.onPasswordChange(it) },
            placeholder = "Password",
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            singleLine = true,
        )
        Text(
            "Forgot Password",
            modifier = Modifier.fillMaxWidth(),
            style = MaterialTheme.typography.subtitle2,
            color = SubtitleTextColor,
            textAlign = TextAlign.Center
        )
        GradientButton(
            text = "Sign in",
            onClick = { loginViewModel.signIn() }
        )
        DividerTextDividerRow(text = "Or Continue with")
        TextButton(
            onClick = { /*navController.navigate(Screens.Home.route)*/ },
            modifier = Modifier
                .fillMaxWidth()
                .shadow(
                    elevation = 14.dp,
                    shape = RoundedCornerShape(50),
                    spotColor = Color(0xFFD28A00),
                )
                .clip(shape = RoundedCornerShape(50))
                .background(MaterialTheme.colors.background)
                .height(52.dp)
                .border(
                    1.dp, brush = Brush.linearGradient(
                        colors = listOf(
                            ButtonGradientStartColor,
                            ButtonGradientEndColor,
                        ),
                    ), RoundedCornerShape(50)
                )
        ) {
            Image(
                modifier = Modifier.size(25.dp, 25.dp),
                painter = painterResource(id = R.drawable.ic_gmail),
                contentDescription = "Gmail logo"
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(
                text = "Sign in with Gmail",
                style = MaterialTheme.typography.button,
                color = GlowingButtonTextColor
            )
        }
        Text(
            buildAnnotatedString {
                withStyle(style = SpanStyle(color = SubtitleTextColor)) {
                    append("Not a member? ")
                }
                withStyle(style = SpanStyle(color = GlowingButtonTextColor)) {
                    append("Register now")
                }
            },
            modifier = Modifier.clickable { onRegister() }
        )
    }
}