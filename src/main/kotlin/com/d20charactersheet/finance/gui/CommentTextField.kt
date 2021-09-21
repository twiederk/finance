package com.d20charactersheet.finance.gui

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

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
        modifier = modifier.width(250.dp).padding(start = 20.dp, end = 20.dp)
    )
}
