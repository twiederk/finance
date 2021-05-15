@file:Suppress("FunctionName")

package com.d20charactersheet.finance.gui

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.d20charactersheet.finance.domain.MoneyTransfer


@Composable
fun MoneyTransferList(moneyTransfers: List<MoneyTransfer>) {
    MoneyTransferHeadline()
    for (moneyTransfer in moneyTransfers) {
        MoneyTransferRow(moneyTransfer)
    }
}

@Composable
private fun MoneyTransferHeadline() {
    Row(Modifier.padding(20.dp)) {
        Text("Valuta", Modifier.width(100.dp))
        Text("Payment Instrument", Modifier.width(100.dp))
        Text("Recipient", Modifier.width(250.dp))
        Text("Amount", Modifier.width(100.dp))
        Text("Comment", Modifier.width(250.dp))
        Text("Category", Modifier.width(150.dp))
    }
}

@Composable
fun MoneyTransferRow(moneyTransfer: MoneyTransfer) {
    Row(Modifier.padding(20.dp)) {
        Text("${moneyTransfer.valutaDate}", Modifier.width(100.dp))
        Text(moneyTransfer.paymentInstrument.name, Modifier.width(100.dp))
        Text(moneyTransfer.recipient.recipient, Modifier.width(250.dp))
        Text(text = "${moneyTransfer.amount} €", modifier = Modifier.width(100.dp), textAlign = TextAlign.End)
        Text(moneyTransfer.comment.text, Modifier.width(250.dp))
        Text(moneyTransfer.category.name, Modifier.width(150.dp))
    }
}

