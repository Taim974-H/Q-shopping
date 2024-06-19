package com.team3.qshopping.view.composables

import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf


@Composable
fun ConfirmDialogue(
    title: String = "Confirm",
    text: String = "Are you sure you want to do this?",
    onYesClicked: () -> Unit,
    shown: MutableState<Boolean> = mutableStateOf(false),
    onNoClicked: () -> Unit = { shown.value = false },
) {
    if (shown.value) {
        AlertDialog(
            onDismissRequest = { shown.value = false },
            title = { Text(title) },
            text = { Text(text) },
            confirmButton = {
                Button(
                    onClick = onYesClicked
                ) {
                    Text("Yes")
                }
            },
            dismissButton = {
                Button(
                    onClick = onNoClicked
                ) {
                    Text("No")
                }
            }
        )
    }
}