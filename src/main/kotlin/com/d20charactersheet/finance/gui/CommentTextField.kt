package com.d20charactersheet.finance.gui

import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier

@Composable
fun CommentTextField(
    viewModel: MoneyTransferViewModel,
    modifier: Modifier
) {
    val textState = remember { viewModel.comment }
    TextField(
        value = textState.value,
        onValueChange = { textState.value = it },
        label = { Text("Comment") },
        modifier = modifier
    )
}
