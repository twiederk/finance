@file:Suppress("FunctionName")

package com.d20charactersheet.finance.gui

import androidx.compose.desktop.Window
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import com.d20charactersheet.finance.domain.Categories
import com.d20charactersheet.finance.domain.MoneyTransfer
import com.d20charactersheet.finance.domain.PaymentInstruments
import com.d20charactersheet.finance.gui.theme.FinanceTheme
import com.d20charactersheet.finance.service.MoneyTransferService


fun MainWindow(
    moneyTransfers: List<MoneyTransfer>,
    moneyTransferService: MoneyTransferService,
    categories: Categories,
    paymentInstruments: PaymentInstruments
) {
    Window(title = "Finance Application (1.7.0)", size = IntSize(1600, 800)) {
        FinanceTheme {
            Column {
                Text(
                    text = "Imported Data",
                    style = MaterialTheme.typography.h4,
                    modifier = Modifier.padding(20.dp)
                )
                MoneyTransferList(
                    moneyTransfers,
                    moneyTransferService,
                    categories.categories,
                    paymentInstruments.paymentInstruments
                )
            }
        }
    }
}


