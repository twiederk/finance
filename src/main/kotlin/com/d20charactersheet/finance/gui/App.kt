@file:Suppress("FunctionName")

package com.d20charactersheet.finance.gui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Tab
import androidx.compose.material.TabRow
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import com.d20charactersheet.finance.domain.Categories
import com.d20charactersheet.finance.domain.MoneyTransfer
import com.d20charactersheet.finance.domain.PaymentInstruments
import com.d20charactersheet.finance.gui.theme.FinanceTheme
import com.d20charactersheet.finance.service.ExcelExportService
import com.d20charactersheet.finance.service.MoneyTransferService
import com.d20charactersheet.finance.service.MonthlyStatementService


@Composable
fun App(
    moneyTransfers: List<MoneyTransfer>,
    moneyTransferService: MoneyTransferService,
    categories: Categories,
    paymentInstruments: PaymentInstruments,
    monthlyStatementService: MonthlyStatementService,
    excelExportService: ExcelExportService
) {
    var selectedTabIndex by remember { mutableStateOf(0) }
    val tabTitles = listOf("Buchungen", "Export nach Excel")

    FinanceTheme {
        Column(modifier = Modifier.fillMaxSize()) {
            TabRow(selectedTabIndex = selectedTabIndex) {
                tabTitles.forEachIndexed { index, title ->
                    Tab(
                        selected = selectedTabIndex == index,
                        onClick = { selectedTabIndex = index },
                        text = { Text(title) }
                    )
                }
            }

            when (selectedTabIndex) {
                0 -> MoneyTransferTab(
                    moneyTransfers = moneyTransfers,
                    moneyTransferService = moneyTransferService,
                    categories = categories,
                    paymentInstruments = paymentInstruments
                )
                1 -> ExportToExcelTab(
                    monthlyStatementService = monthlyStatementService,
                    excelExportService = excelExportService
                )
            }
        }
    }
}

