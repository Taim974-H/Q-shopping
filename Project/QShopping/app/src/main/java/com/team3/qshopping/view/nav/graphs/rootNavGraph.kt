package com.team3.qshopping.view.nav.graphs

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.team3.qshopping.view.nav.AnimatedSplashScreen
import com.team3.qshopping.view.nav.AuthScreen
import com.team3.qshopping.view.nav.MainScreen

@Composable
fun RootNavGraph(
    rootNavController: NavHostController
) {
    NavHost(
        navController = rootNavController,
        route = Graph.ROOT,
        startDestination = Graph.SPLASH,
    ) {
        composable(route = Graph.SPLASH) {
            AnimatedSplashScreen(
                onLoggedIn = {
                    rootNavController.popBackStack()
                    rootNavController.navigate(route = Graph.APP)
                },
                onNotLoggedIn = {
                    rootNavController.popBackStack()
                    rootNavController.navigate(route = Graph.AUTH)
                }
            )
        }
        composable(route = Graph.AUTH) {
            AuthScreen(rootNavController = rootNavController)
        }
        composable(route = Graph.APP) {
            MainScreen(rootNavController = rootNavController)
        }
    }
}

object Graph {
    const val ROOT = "root_graph"
    const val SPLASH = "splash"
    const val AUTH = "auth_graph"
    const val APP = "app_graph"
}