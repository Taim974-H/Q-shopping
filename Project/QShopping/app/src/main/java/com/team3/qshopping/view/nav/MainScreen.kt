package com.team3.qshopping.view.nav

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.team3.qshopping.view.composables.layout.BottomBar
import com.team3.qshopping.view.composables.layout.NavDrawer
import com.team3.qshopping.view.conditional
import com.team3.qshopping.view.nav.graphs.AppNavGraph
import com.team3.qshopping.view.nav.graphs.Graph

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun MainScreen(rootNavController: NavHostController) {
    val appNavController = rememberNavController()
    val state = rememberScaffoldState()
    val scope = rememberCoroutineScope()
    Scaffold(
        scaffoldState = state,
        drawerContent = {
            NavDrawer(
                navController = appNavController,
                scaffoldState = state,
                scaffoldScope = scope,
                onLogout = {
                    rootNavController.popBackStack()
                    rootNavController.navigate(route = Graph.AUTH)
                }
            )
        },
        bottomBar = { BottomBar(navController = appNavController) },
    ) { innerPadding ->
        val currentRoute = appNavController.currentBackStackEntryAsState().value?.destination?.route
        Box(
            modifier = Modifier
                .conditional(
                    !(Screen.fromRoute(currentRoute)?.noPadding ?: false)
                ) { padding(horizontal = 15.dp) }
                .padding(innerPadding)

        )
        {
            AppNavGraph(
                appNavController = appNavController,
                scaffoldState = state,
                scaffoldScope = scope
            )
        }
    }
}
