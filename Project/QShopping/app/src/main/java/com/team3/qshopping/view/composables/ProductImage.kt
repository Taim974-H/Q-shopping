package com.team3.qshopping.view.composables

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.team3.qshopping.R
import com.team3.qshopping.ui.theme.LightGrey

@Composable
fun ProductImage(
    productImageUrl: String,
    productName: String,
    contentDescription: String = "",
    placeholder: Int = R.drawable.ic_logo
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = productName,
            style = MaterialTheme.typography.h4,
            fontWeight = FontWeight.Normal,
            fontSize = 30.sp
        )
        Spacer(modifier = Modifier.height(5.dp))
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .height(300.dp)
                .fillMaxWidth()
                .padding(10.dp)
                .shadow(
                    elevation = 10.dp,
                    shape = RoundedCornerShape(5),
                    spotColor = LightGrey
                )
        ) {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(productImageUrl)
                    .crossfade(true)
                    .build(),
                contentDescription = contentDescription,
                placeholder = painterResource(placeholder),
                modifier = Modifier.fillMaxHeight(0.9f)
            )
        }
    }
}