@file:Suppress("FunctionName")

package com.d20charactersheet.finance.gui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.OutlinedButton
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.d20charactersheet.finance.domain.Category
import com.d20charactersheet.finance.domain.MoneyTransfer
import com.d20charactersheet.finance.domain.PaymentInstrument
import com.d20charactersheet.finance.service.MoneyTransferService


@Composable
fun MoneyTransferList(
    moneyTransfers: List<MoneyTransfer>,
    moneyTransferService: MoneyTransferService,
    categories: List<Category>,
    paymentInstruments: List<PaymentInstrument>
) {
    Column(
        modifier = Modifier
            .verticalScroll(rememberScrollState())
            .padding(bottom = 80.dp)
    ) {
        for (moneyTransfer in moneyTransfers) {
            MoneyTransferRow(moneyTransfer, moneyTransferService, categories, paymentInstruments)
        }
    }
}

@Composable
fun MoneyTransferRow(
    moneyTransfer: MoneyTransfer,
    moneyTransferService: MoneyTransferService,
    categories: List<Category>,
    paymentInstruments: List<PaymentInstrument>
) {
    Row(Modifier.padding(start = 20.dp, end = 20.dp, bottom = 10.dp)) {
        val isSaved = remember { mutableStateOf(false) }
        val viewModel = MoneyTransferViewModel(moneyTransfer, moneyTransferService)

        Text("${moneyTransfer.valutaDate}", Modifier.width(100.dp).alignByBaseline())

        PaymentInstrumentDropDown(viewModel, paymentInstruments)

        Text(
            moneyTransfer.recipient.name,
            Modifier.width(250.dp).alignByBaseline().padding(start = 20.dp, end = 20.dp)
        )
        Text(
            text = "${moneyTransfer.amount} €",
            modifier = Modifier.width(100.dp).alignByBaseline(),
            textAlign = TextAlign.End
        )

        CommentTextField(viewModel, Modifier.width(250.dp).alignByBaseline().padding(start = 20.dp, end = 20.dp))
        CategoryDropDown(viewModel, categories)

        if (isSaved.value) {
            OutlinedButton(
                onClick = { },
                modifier = Modifier.width(150.dp).alignByBaseline().padding(start = 20.dp, end = 20.dp)
            ) {
                Text("DONE")
            }
        } else {
            Button(
                onClick = {
                    viewModel.onCommit()
                    isSaved.value = true
                },
                modifier = Modifier.width(150.dp).alignByBaseline().padding(start = 20.dp, end = 20.dp)
            ) {
                Text("Commit")
            }
        }
    }
}


