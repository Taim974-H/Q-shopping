package com.team3.qshopping.view.nav.graphs

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.team3.qshopping.view.screens.LoginScreen
import com.team3.qshopping.view.screens.RegistrationScreen

@Composable
fun AuthNavGraph(rootNavController: NavHostController) {
    val authNavController = rememberNavController()
    NavHost(
        navController = authNavController,
        route = Graph.AUTH,
        startDestination = AuthScreens.Login.route
    ) {
        composable(route = AuthScreens.Login.route) {
            LoginScreen(
                onRegistrationClick = {
//                    authNavController.popBackStack()
                    authNavController.navigate(AuthScreens.Register.route)
                },
                onSignIn = {
                    rootNavController.popBackStack()
                    rootNavController.navigate(Graph.APP)
                }
            )
        }
        composable(route = AuthScreens.Register.route) {
            RegistrationScreen(
                onRegister = {
                    rootNavController.popBackStack()
                    authNavController.navigate(route = AuthScreens.Login.route)
                }
            )
        }
    }
}

sealed class AuthScreens(val route: String) {
    object Login : AuthScreens(route = "LOGIN")
    object Register : AuthScreens(route = "REGISTER")
}