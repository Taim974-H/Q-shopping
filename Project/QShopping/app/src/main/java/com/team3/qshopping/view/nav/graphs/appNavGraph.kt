package com.team3.qshopping.view.nav.graphs

import androidx.compose.material.ScaffoldState
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.team3.qshopping.Globals
import com.team3.qshopping.view.nav.Screen
import com.team3.qshopping.view.screens.*
import kotlinx.coroutines.CoroutineScope

@Composable
fun AppNavGraph(
    appNavController: NavHostController,
    scaffoldState: ScaffoldState,
    scaffoldScope: CoroutineScope
) {
    NavHost(
        navController = appNavController,
        route = Graph.APP,
        startDestination = Screen.Home.route,
    ) {
        composable(route = Screen.Address.route) {
            Address(appNavController)
        }
        composable(route = Screen.Profile.route) {
            ProfileScreen(appNavController)
        }
        composable(route = Screen.Home.route) {
            val user = Globals.user
            if (user != null) {
                if (user.admin) {
                    HomeScreenAdmin(appNavController, scaffoldState, scaffoldScope)
                } else {
                    HomeScreen(appNavController, scaffoldState, scaffoldScope)
                }
            }
        }
        composable(route = Screen.Category.parameterizedRoute) {
            val categoryId = it.arguments?.getString(
                // this code will extract the parameter name from the parameterized route
                Screen.Category.parameterizedRoute.substringAfter('{').substringBefore('}')
            )?.toInt()
            CategoryScreen(appNavController, categoryId!!)
        }
        composable(
            route = Screen.Product.route,
            arguments = listOf(navArgument("productId") { type = NavType.IntType })
        ) {
            ProductDetailScreen(
                navController = appNavController,
                productId = it.arguments?.getInt("productId")!!
            )
        }
        composable(
            route = Screen.ProductAdmin.route,
            arguments = listOf(navArgument("productId") { type = NavType.IntType })
        ) {
            AdminProductDetailScreen(
                navController = appNavController,
                productId = it.arguments?.getInt("productId")!!
            )
        }
        composable(route = Screen.Orders.route) {
            val user = Globals.user
            if (user != null) {
                if (user.admin) {
                    OrderTrackPageAdmin(appNavController)
                } else {
                    OrderTrackPageCustomer(appNavController)
                }
            }
        }
        composable(route = Screen.Favourites.route) {
            WishlistScreen(appNavController)
        }
        composable(route = Screen.Cart.route) {
            CartScreen(appNavController)
        }
        composable(route = Screen.CardInfo.route) {
            CardInfoScreen(appNavController)
        }
        composable(route = Screen.AddProduct.route) {
            AddProductScreen(navController = appNavController)
        }
        composable(
            route = Screen.UpdateProduct.route,
            arguments = listOf(navArgument("productId") { type = NavType.IntType })
        ) {
            UpdateProduct(navController = appNavController, it.arguments?.getInt("productId")!!)
        }
        composable(route = Screen.AdminReport.route) {
            AdminReportScreen(appNavController, scaffoldState, scaffoldScope)
        }
    }
}