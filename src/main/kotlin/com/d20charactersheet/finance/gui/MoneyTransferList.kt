@file:Suppress("FunctionName")

package com.d20charactersheet.finance.gui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.d20charactersheet.finance.domain.Category
import com.d20charactersheet.finance.domain.MoneyTransfer
import com.d20charactersheet.finance.domain.PaymentInstrument


@Composable
fun MoneyTransferList(
    moneyTransfers: List<MoneyTransfer>,
    categories: List<Category>,
    paymentInstruments: List<PaymentInstrument>
) {
    for (moneyTransfer in moneyTransfers) {
        MoneyTransferRow(moneyTransfer, categories, paymentInstruments)
    }
}

@Composable
fun MoneyTransferRow(
    moneyTransfer: MoneyTransfer,
    categories: List<Category>,
    paymentInstruments: List<PaymentInstrument>
) {
    Row(Modifier.padding(start = 20.dp, end = 20.dp, bottom = 10.dp)) {
        val viewModel = ViewModel(
            mutableStateOf(moneyTransfer.paymentInstrument),
            mutableStateOf(moneyTransfer.comment.text),
            mutableStateOf(moneyTransfer.category)
        )

        Text("${moneyTransfer.valutaDate}", Modifier.width(100.dp).alignByBaseline())

        PaymentInstrumentDropDown(viewModel, paymentInstruments)

        Text(
            moneyTransfer.recipient.recipient,
            Modifier.width(250.dp).alignByBaseline().padding(start = 20.dp, end = 20.dp)
        )
        Text(
            text = "${moneyTransfer.amount} €",
            modifier = Modifier.width(100.dp).alignByBaseline(),
            textAlign = TextAlign.End
        )

        CommentTextField(viewModel, Modifier.width(250.dp).alignByBaseline().padding(start = 20.dp, end = 20.dp))
        CategoryDropDown(viewModel, categories)

        Button(
            onClick = { printCommitData(viewModel) },
            modifier = Modifier.width(150.dp).alignByBaseline().padding(start = 20.dp, end = 20.dp)
        ) {
            Text("Commit")
        }
    }
}

fun printCommitData(viewModel: ViewModel) {
    println("Payment Instrument: ${viewModel.paymentInstrument.value}")
    println("Comment: ${viewModel.comment.value}")
    println("Category: ${viewModel.category.value}")
}

data class ViewModel(
    var paymentInstrument: MutableState<PaymentInstrument>,
    var comment: MutableState<String>,
    var category: MutableState<Category>
) {

    fun onCategoryChange(category: Category) {
        this.category.value = category
    }

    fun onPaymentInstrumentChange(paymentInstrument: PaymentInstrument) {
        this.paymentInstrument.value = paymentInstrument
    }
}


@Composable
fun PaymentInstrumentDropDown(
    viewModel: ViewModel,
    categories: List<PaymentInstrument>,
) {
    val isOpen = remember { mutableStateOf(false) }

    Box {
        Column {
            TextField(
                value = viewModel.paymentInstrument.value.name,
                onValueChange = { },
                label = { Text("Payment Instrument") }
            )

            DropdownMenu(
                expanded = isOpen.value,
                onDismissRequest = { isOpen.value = false }
            ) {
                categories.forEach {
                    DropdownMenuItem(
                        onClick = {
                            isOpen.value = false
                            viewModel.onPaymentInstrumentChange(it)
                        }
                    ) {
                        Text(it.name)
                    }
                }
            }
        }
        Spacer(
            modifier = Modifier
                .matchParentSize()
                .background(Color.Transparent)
                .padding(10.dp)
                .clickable(
                    onClick = { isOpen.value = true }
                )
        )
    }
}

@Composable
fun CommentTextField(
    viewModel: ViewModel,
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

@Composable
fun CategoryDropDown(
    viewModel: ViewModel,
    categories: List<Category>
) {
    val isOpen = remember { mutableStateOf(false) }

    Box {
        Column {
            TextField(
                value = viewModel.category.value.name,
                onValueChange = { },
                label = { Text("Category") }
            )

            DropdownMenu(
                expanded = isOpen.value,
                onDismissRequest = { isOpen.value = false },
                modifier = Modifier.width(250.dp).height(500.dp)
            ) {
                categories.forEach {
                    DropdownMenuItem(
                        onClick = {
                            isOpen.value = false
                            viewModel.onCategoryChange(it)
                        }
                    ) {
                        Text(it.name)
                    }
                }
            }
        }
        Spacer(
            modifier = Modifier
                .matchParentSize()
                .background(Color.Transparent)
                .padding(10.dp)
                .clickable(
                    onClick = { isOpen.value = true }
                )
        )
    }
}
