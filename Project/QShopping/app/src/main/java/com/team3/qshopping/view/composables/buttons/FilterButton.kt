package com.team3.qshopping.view.composables.buttons

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.team3.qshopping.R
import com.team3.qshopping.ui.theme.LightGrey

@Composable
fun FilterButton(onClick: () -> Unit){
    TextButton(
        onClick = onClick,
        modifier = Modifier
            .height(37.dp)
            .width(77.dp)
            .background(
                color = LightGrey,
                shape = RoundedCornerShape(10.dp)
            )

    ){
        Image(
            painter = painterResource(R.drawable.filtericon),
            contentDescription = "FilterIcon",
        )
        Text(
            text = "Filter",
            color = Color.Black,
            style = MaterialTheme.typography.body1,
        )
    }
}

@Preview
@Composable
fun FilterButtonPreview(){
    val context = LocalContext.current

    Surface{
        Box(
            modifier = Modifier.padding(10.dp)
        ){
            FilterButton(
                onClick = {
                    Toast.makeText(context, "Shiny button clicked", Toast.LENGTH_SHORT).show()
                }
            )
        }

    }
}