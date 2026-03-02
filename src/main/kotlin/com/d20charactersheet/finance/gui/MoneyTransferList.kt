@file:Suppress("FunctionName")

package com.d20charactersheet.finance.gui

import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Divider
import androidx.compose.material.OutlinedButton
import androidx.compose.material.Text
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
            .fillMaxWidth()
    ) {
        for (moneyTransfer in moneyTransfers) {
            MoneyTransferRow(moneyTransfer, moneyTransferService, categories, paymentInstruments)
        }
        EndRow()
    }
}


@Composable
fun MoneyTransferRow(
    moneyTransfer: MoneyTransfer,
    moneyTransferService: MoneyTransferService,
    categories: List<Category>,
    paymentInstruments: List<PaymentInstrument>
) {
    Row(
        Modifier
            .fillMaxWidth()
            .horizontalScroll(rememberScrollState())
            .padding(start = 20.dp, end = 20.dp, bottom = 10.dp)
    ) {
        val transactionButtonState = remember { mutableStateOf(TransactionButtonState.Commit) }
        val viewModel = MoneyTransferViewModel(moneyTransfer, moneyTransferService)

        ValutaDateText(moneyTransfer, Modifier.alignByBaseline().weight(1f))
        PaymentInstrumentDropDown(viewModel, paymentInstruments, Modifier.weight(1.2f))
        Column(Modifier.alignByBaseline().weight(1.8f)) {
            RecipientText(moneyTransfer)
            ReasonForTransferText(moneyTransfer)
        }
        AmountText(moneyTransfer, Modifier.alignByBaseline().weight(0.8f))
        CommentTextField(viewModel, Modifier.alignByBaseline().weight(1.5f))
        CategoryDropDown(viewModel, categories, Modifier.weight(1.2f))

        when (transactionButtonState.value) {
            TransactionButtonState.Commit -> CommitButton(
                viewModel,
                transactionButtonState,
                Modifier.alignByBaseline().weight(0.9f)
            )

            TransactionButtonState.Done -> DoneButton(Modifier.alignByBaseline().weight(0.9f))
            TransactionButtonState.Rejected -> RejectButton(Modifier.alignByBaseline().weight(0.9f))
        }
    }
    Divider(
        modifier = Modifier.padding(bottom = 10.dp)
    )
}

@Composable
private fun AmountText(moneyTransfer: MoneyTransfer, modifier: Modifier) {
    Text(
        text = AmountFormat().format(moneyTransfer.amount),
        modifier = modifier,
        textAlign = TextAlign.End
    )
}


@Composable
private fun RecipientText(moneyTransfer: MoneyTransfer, modifier: Modifier = Modifier) {
    Text(
        text = moneyTransfer.recipient.name,
        modifier = modifier.padding(start = 20.dp, end = 20.dp, bottom = 20.dp)
    )
}

@Composable
fun ReasonForTransferText(moneyTransfer: MoneyTransfer, modifier: Modifier = Modifier) {
    Text(
        text = moneyTransfer.reasonForTransfer.text,
        modifier = modifier.padding(start = 20.dp, end = 20.dp)
    )
}


@Composable
private fun ValutaDateText(moneyTransfer: MoneyTransfer, modifier: Modifier) {
    Text(
        text = "${moneyTransfer.valutaDate}",
        modifier = modifier
    )
}


@Composable
private fun CommitButton(
    viewModel: MoneyTransferViewModel,
    transactionButtonState: MutableState<TransactionButtonState>,
    modifier: Modifier
) {
    Button(
        onClick = {
            if (viewModel.onCommit()) {
                transactionButtonState.value = TransactionButtonState.Done
            } else {
                transactionButtonState.value = TransactionButtonState.Rejected
            }
        },
        modifier = modifier.padding(start = 20.dp, end = 20.dp)
    ) {
        Text("Commit")
    }
}


@Composable
private fun DoneButton(modifier: Modifier) {
    OutlinedButton(
        onClick = { },
        modifier = modifier.padding(start = 20.dp, end = 20.dp),
        enabled = false
    ) {
        Text("DONE")
    }
}

@Composable
private fun RejectButton(modifier: Modifier) {
    Button(
        onClick = { },
        modifier = modifier.padding(start = 20.dp, end = 20.dp),
        colors = ButtonDefaults.buttonColors(
            disabledBackgroundColor = Color.Red,
            disabledContentColor = Color.White
        ),
        enabled = false
    ) {
        Text("REJECTED")
    }
}


@Composable
fun EndRow() {
    Row {
        Text(
            modifier = Modifier
                .fillMaxSize()
                .padding(25.dp)
                .background(Color.LightGray),
            textAlign = TextAlign.Center,
            text = "End"
        )
    }
}
