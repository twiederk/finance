@file:Suppress("FunctionName", "FunctionName", "FunctionName")

package com.d20charactersheet.finance.gui

import androidx.compose.desktop.Window
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import com.d20charactersheet.finance.domain.MoneyTransfer
import com.d20charactersheet.finance.gui.theme.FinanceTheme


fun MainWindow(moneyTransfers: List<MoneyTransfer>) {

    Window(title = "Finance App", size = IntSize(1024, 800)) {
        FinanceTheme {
            Column {
                Text(
                    text = "Imported Data",
                    style = MaterialTheme.typography.h4,
                    modifier = Modifier.padding(start = 20.dp, top = 20.dp)
                )
                MoneyTransferList(moneyTransfers)
            }
        }
    }
}


