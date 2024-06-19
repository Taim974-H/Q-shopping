package com.team3.qshopping.view.nav

import android.annotation.SuppressLint
import androidx.compose.material.Scaffold
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import com.team3.qshopping.view.nav.graphs.AuthNavGraph

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun AuthScreen(rootNavController: NavHostController) {
    val state = rememberScaffoldState()
    Scaffold(scaffoldState = state) {
        AuthNavGraph(rootNavController = rootNavController)
    }
}
