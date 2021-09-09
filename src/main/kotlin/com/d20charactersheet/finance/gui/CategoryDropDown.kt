package com.d20charactersheet.finance.gui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import com.d20charactersheet.finance.domain.Category

@Composable
fun CategoryDropDown(
    viewModel: MoneyTransferViewModel,
    categories: List<Category>
) {
    var isOpen by remember { mutableStateOf(false) }

    Box {
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
