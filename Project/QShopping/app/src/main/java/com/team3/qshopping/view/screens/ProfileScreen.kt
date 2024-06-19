package com.team3.qshopping.view.screens

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Person
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.team3.qshopping.Globals
import com.team3.qshopping.R
import com.team3.qshopping.view.composables.buttons.BackButton
import com.team3.qshopping.view.composables.buttons.MatteButton
import com.team3.qshopping.view.composables.layout.Header
import com.team3.qshopping.view.nav.Screen
import kotlin.math.max

@Composable
fun ProfileScreen(
    navController: NavController,
) {
    val user = Globals.user
    val isAdmin: Boolean = user!!.admin == true

    Surface {
        Box(modifier = Modifier.fillMaxSize()) {
            Column {
                ProfileImageSection(
                    name = user.fullName.toString(),
                    isAdmin = isAdmin,
                    onBackPress = { navController.popBackStack() }
                )
                Spacer(modifier = Modifier.height(30.dp))
                Divider(
                    modifier = Modifier.fillMaxWidth(),
                    thickness = 1.dp,
                    color = Color.Black
                )
                Spacer(modifier = Modifier.height(30.dp))
                ProfileInfoSection(navController, user)
            }
        }
    }
}

@Composable
private fun ProfileInfoSection(
    navController: NavController,
    user: com.team3.qshopping.data.remote.models.User
) {
    Column(
        modifier = Modifier
            .fillMaxHeight()
            .padding(bottom = 55.dp),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Column {
            DataRow(
                icon = Icons.Default.Person,
                label = "Username",
                data = user.userName
            )
            Spacer(modifier = Modifier.height(15.dp))
            DataRow(
                icon = Icons.Default.Email,
                iconSize = 40.dp,
                label = "Email",
                data = user.email
            )
        }
        if (user.admin) AdminUtils(navController)
        else NonAdminUtils { navController.navigate(Screen.Address.route) }
    }
}

@Composable
fun NonAdminUtils(onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        UserInfoSection(
            "Addresses",
            "Address",
            2
        ) { onClick() }
        UserInfoSection(
            "Credit Cards",
            "Card",
            2
        ) { onClick() }
    }
}

@Composable
fun AdminUtils(navController: NavController) {
    Column {
        MatteButton(
            text = "View Orders",
            trailingIcon = Icons.Filled.ArrowForward,
        ) {
            navController.navigate(Screen.Orders.route)
        }
        Spacer(modifier = Modifier.height(15.dp))
        MatteButton(
            text = "Generate Report",
            trailingIcon = Icons.Filled.ArrowForward,
        ) { navController.navigate(Screen.AdminReport.route) }
    }
}

@Composable
fun UserInfoSection(
    title: String,
    placeholder: String,
    inputButtonCount: Int,
    onClick: () -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(15.dp)) {
        Text(
            text = title,
            style = MaterialTheme.typography.h4
        )
        (0 until inputButtonCount).forEach { i ->
            Row(modifier = Modifier.width(160.dp)) {
                MatteButton(
                    text = "$placeholder ${i + 1}",
                    trailingIcon = Icons.Filled.ArrowForward,
                ) { onClick() }
            }
        }
    }
}

@Composable
fun DataRow(icon: ImageVector, iconSize: Dp = 50.dp, label: String, data: String) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier.fillMaxWidth(0.95f)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Spacer(
                modifier = Modifier.width(
                    max((50 - iconSize.value.toInt()) / 2.0, 0.0).dp
                )
            )
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = Color.DarkGray,
                modifier = Modifier.size(iconSize)
            )
            Spacer(modifier = Modifier.width(10.dp))
            Text(
                text = label,
                style = MaterialTheme.typography.h4
            )
        }
        Text(
            text = data,
            style = MaterialTheme.typography.h4
        )
    }

}

@Composable
private fun ProfileImageSection(
    name: String = "Null",
    isAdmin: Boolean = false,
    onBackPress: () -> Unit
) {
    Box {
        Header(
            start = {
                BackButton { onBackPress() }
            },
            middle = { },
            end = { }
        )
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(30.dp))
            ProfileImage()
            Spacer(modifier = Modifier.height(20.dp))
            Text(
                text = name,
                style = MaterialTheme.typography.h3
            )
            if (isAdmin) {
                Text(
                    text = "(Admin)",
                    style = MaterialTheme.typography.subtitle1
                )
            }
        }
    }
}

@Composable
private fun ProfileImage() {
    val imageUrl = rememberSaveable { mutableStateOf("") }
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let { imageUrl.value = it.toString() }
    }

    Row(
        verticalAlignment = Alignment.Bottom,
        modifier = Modifier.padding(start = 24.dp)
    ) {
        Card(
            shape = CircleShape,
            modifier = Modifier
                .size(150.dp)
        ) {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(imageUrl.value.ifEmpty { R.drawable.ic_person })
                    .crossfade(true)
                    .build(),
                contentDescription = "Profile Image",
                placeholder = painterResource(R.drawable.ic_person),
                colorFilter = if (imageUrl.value.isEmpty()) ColorFilter.tint(Color.Gray)
                else null,
                modifier = Modifier
                    .wrapContentSize()
            )
        }
        Image(
            painter = painterResource(R.drawable.ic_pen),
            contentDescription = "Upload Image",
            colorFilter = ColorFilter.tint(Color.Gray),
            modifier = Modifier
                .padding(top = 40.dp)
                .clickable { launcher.launch("image/*") }
        )
    }
}

