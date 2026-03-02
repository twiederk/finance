package com.d20charactersheet.finance.gui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import com.d20charactersheet.finance.domain.Category

@Composable
fun CategoryDropDown(
    viewModel: MoneyTransferViewModel,
    categories: List<Category>,
    modifier: Modifier = Modifier
) {
    var isOpen by remember { mutableStateOf(false) }

    Box(modifier = modifier) {
        Column {
            val color = DataColorSelector().getCategoryColor(viewModel.category.value)
            Surface(color = color) {
                TextField(
                    value = viewModel.category.value.name,
                    onValueChange = { },
                    label = { Text("Category") }
                )
            }

            DropdownMenu(
                expanded = isOpen,
                onDismissRequest = { isOpen = false },
                modifier = Modifier.width(250.dp).height(500.dp)
            ) {
                val textState = remember { mutableStateOf(TextFieldValue("")) }
                Column {
                    SearchView(textState)
                    CategoryList(
                        state = textState,
                        viewModel = viewModel,
                        categories = categories,
                        onClick = { isOpen = false }
                    )
                }

            }
        }
        Spacer(
            modifier = Modifier
                .matchParentSize()
                .background(Color.Transparent)
                .padding(10.dp)
                .clickable(
                    onClick = { isOpen = true }
                )
        )
    }
}

@Composable
fun CategoryList(
    state: MutableState<TextFieldValue>,
    viewModel: MoneyTransferViewModel,
    categories: List<Category>,
    onClick: () -> Unit
) {
    val searchedText = state.value.text
    val filteredCategories = CategoryFilter().filter(searchedText, categories)
    filteredCategories.forEach { category ->
        DropdownMenuItem(
            onClick = {
                onClick()
                viewModel.onCategoryChange(category)
            }
        ) {
            Text(category.name)
        }
    }

}
