package com.team3.qshopping

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.team3.qshopping.data.remote.initialiseCollections
import com.team3.qshopping.ui.theme.QShoppingTheme
import com.team3.qshopping.view.nav.graphs.RootNavGraph

class MainActivity : ComponentActivity() {
    @SuppressLint("CoroutineCreationDuringComposition")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            initialiseCollections()

            val navController = rememberNavController()
            QShoppingTheme {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(MaterialTheme.colors.background)
                ) {
                    RootNavGraph(rootNavController = navController)
                }
            }
        }
    }
}
