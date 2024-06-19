package com.team3.qshopping.view.composables.inputFields

import android.app.DatePickerDialog
import android.widget.DatePicker
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.team3.qshopping.R
import com.team3.qshopping.view.conditional
import java.time.LocalDate
import java.time.format.DateTimeFormatter


@Composable
fun CustomDatePicker(
    modifier: Modifier = Modifier,
    date: MutableState<LocalDate?> = mutableStateOf(null),
    placeholder: String,
    enabled: Boolean = true
) {
    val currentDate: LocalDate = date.value ?: LocalDate.now()
    val datePickerDialog = DatePickerDialog(
        LocalContext.current,
        { _: DatePicker, year: Int, month: Int, dayOfMonth: Int ->
            date.value = LocalDate.of(
                year,
                month + 1,//month + 1 because DatePicker gives 0-11 and LocalDate expects 1-12
                dayOfMonth
            )
        },
        currentDate.year, currentDate.month.ordinal, currentDate.dayOfMonth
    )
    Box(modifier = Modifier
        .clip(shape = RoundedCornerShape(12.dp))
        .conditional(enabled) { clickable { datePickerDialog.show() } }
        .then(modifier))
    {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .conditional(enabled) {
                    background(Color(0xFFF5F6FA), shape = RoundedCornerShape(12.dp))
                }
                .conditional(!enabled) {
                    background(Color.LightGray, shape = RoundedCornerShape(12.dp))
                }
                .padding(vertical = 16.dp, horizontal = 12.dp),
            horizontalArrangement = Arrangement.spacedBy(4.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_calendar),
                contentDescription = "calendar",
                tint = Color(0xA3323232),
            )
            val text = if (date.value == null)
                placeholder
            else
                currentDate.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))
            Text(
                text,
                color = Color(0xFF848484),
                style = MaterialTheme.typography.body2
            )
        }
    }
}

