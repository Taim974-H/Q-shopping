package com.team3.qshopping.view.composables.layout

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ScaffoldState
import androidx.compose.material.Switch
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.google.firebase.auth.FirebaseAuth
import com.team3.qshopping.Globals
import com.team3.qshopping.R
import com.team3.qshopping.view.nav.Screen
import com.team3.qshopping.view.screens.MenuButton
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch


@Composable
fun NavDrawer(
    navController: NavHostController,
    scaffoldState: ScaffoldState,
    scaffoldScope: CoroutineScope,
    onLogout: () -> Unit
) {
    Column(
        modifier = Modifier.padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        MenuButton {
            scaffoldScope.launch { scaffoldState.drawerState.close() }
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    modifier = Modifier
                        .size(64.dp)
                        .background(Color(0xFFC4C4C4), shape = RoundedCornerShape(50))
                        .padding(4.dp),
                    painter = painterResource(id = R.drawable.ic_account),
                    contentDescription = "Profile Picture"
                )
                Globals.user?.let {
                    Text(
                        it.fullName,
                        style = MaterialTheme.typography.h3
                    )
                }
            }
            /*Text(
                text = "${sessionViewModel.orderCount} orders",
                modifier = Modifier
                    .background(
                        Color(0xFFF5F5F5),
                        shape = RoundedCornerShape(5.dp)
                    )
                    .padding(vertical = 12.dp, horizontal = 14.dp),
                color = Color(0xFF8F959E)
            )*/
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_sun),
                    contentDescription = "Dark mode",
                    modifier = Modifier.size(28.dp)
                )
                Text("Dark Mode", style = MaterialTheme.typography.h3)
            }
            Switch(checked = false, onCheckedChange = {})
        }
        Column(
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        navController.navigate(route = Screen.Profile.route)
                        scaffoldScope.launch { scaffoldState.drawerState.close() }
                    }
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_account),
                    colorFilter = ColorFilter.tint(Color(0xFFA6A8AA)),
                    contentDescription = "Account",
                    modifier = Modifier.size(28.dp)
                )
                Text("Profile", style = MaterialTheme.typography.h3)
            }

            Row(
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        onLogout()
                        FirebaseAuth
                            .getInstance()
                            .signOut()
                        Globals.user = null
                        scaffoldScope.launch { scaffoldState.drawerState.close() }
                    }
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_logout),
                    colorFilter = ColorFilter.tint(Color(0xFFA6A8AA)),
                    contentDescription = "Logout",
                    modifier = Modifier.size(28.dp)
                )
                Text("Logout", style = MaterialTheme.typography.h3, color = Color.Red)
            }
        }
    }
}
