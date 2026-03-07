@file:Suppress("FunctionName")

package com.d20charactersheet.finance.gui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedButton
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.d20charactersheet.finance.domain.Critically
import com.d20charactersheet.finance.domain.ExportMessage
import com.d20charactersheet.finance.domain.ExportMessageComparator
import com.d20charactersheet.finance.service.ExcelExportService
import com.d20charactersheet.finance.service.MonthlyStatementService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.LocalDate
import java.time.Month
import java.time.format.TextStyle
import java.util.*

@Composable
fun ExportToExcelTab(
    monthlyStatementService: MonthlyStatementService,
    excelExportService: ExcelExportService
) {
    val currentYear = LocalDate.now().year
    val currentMonth = LocalDate.now().monthValue

    var selectedYear by remember { mutableStateOf(currentYear) }
    var selectedMonth by remember { mutableStateOf(currentMonth) }
    var isExporting by remember { mutableStateOf(false) }
    var messages by remember { mutableStateOf<List<ExportMessage>?>(null) }

    val coroutineScope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp)
    ) {
        Text(
            text = "Export to Excel",
            style = MaterialTheme.typography.h4,
            modifier = Modifier.padding(bottom = 30.dp)
        )

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(bottom = 16.dp)
        ) {
            Text(
                text = "Jahr:",
                modifier = Modifier.width(100.dp)
            )
            YearDropdown(
                selectedYear = selectedYear,
                currentYear = currentYear,
                enabled = !isExporting,
                onYearSelected = { selectedYear = it }
            )
        }

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(bottom = 24.dp)
        ) {
            Text(
                text = "Monat:",
                modifier = Modifier.width(100.dp)
            )
            MonthDropdown(
                selectedMonth = selectedMonth,
                enabled = !isExporting,
                onMonthSelected = { selectedMonth = it }
            )
        }

        // Export-Button
        Button(
            onClick = {
                isExporting = true
                messages = null

                coroutineScope.launch {
                    val result = withContext(Dispatchers.IO) {
                        val monthlyStatements = monthlyStatementService.monthlyStatements(selectedYear, selectedMonth)
                        excelExportService.export(monthlyStatements)
                    }
                    messages = result.messages
                    isExporting = false
                }
            },
            enabled = !isExporting,
            modifier = Modifier.padding(bottom = 24.dp)
        ) {
            Text(if (isExporting) "Exportiere..." else "Export starten")
        }

        if (messages?.isEmpty() == true) {
            Text(
                text = "Keine Änderungen vorgenommen",
                style = MaterialTheme.typography.subtitle1,
                modifier = Modifier.padding(bottom = 12.dp)
            )
        }

        if (messages?.isNotEmpty() == true) {
            Text(
                text = "--- Export-Meldungen ---",
                style = MaterialTheme.typography.subtitle1,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 12.dp)
            )

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .verticalScroll(rememberScrollState())
            ) {
                messages?.sortedWith(ExportMessageComparator())?.forEach { message ->
                    MessageRow(message)
                }
            }
        }
    }
}

@Composable
private fun YearDropdown(
    selectedYear: Int,
    currentYear: Int,
    enabled: Boolean,
    onYearSelected: (Int) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    val years = listOf(currentYear, currentYear - 1)

    Box {
        OutlinedButton(
            onClick = { expanded = true },
            enabled = enabled
        ) {
            Text(selectedYear.toString())
        }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            years.forEach { year ->
                DropdownMenuItem(onClick = {
                    onYearSelected(year)
                    expanded = false
                }) {
                    Text(year.toString())
                }
            }
        }
    }
}

@Composable
private fun MonthDropdown(
    selectedMonth: Int,
    enabled: Boolean,
    onMonthSelected: (Int) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Box {
        OutlinedButton(
            onClick = { expanded = true },
            enabled = enabled
        ) {
            Text(getMonthName(selectedMonth))
        }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            (1..12).forEach { month ->
                DropdownMenuItem(onClick = {
                    onMonthSelected(month)
                    expanded = false
                }) {
                    Text(getMonthName(month))
                }
            }
        }
    }
}

private fun getMonthName(month: Int): String {
    return Month.of(month).getDisplayName(TextStyle.FULL, Locale.GERMAN)
}

@Composable
private fun MessageRow(message: ExportMessage) {
    val textColor = when (message.critically) {
        Critically.ERROR -> Color.Red
        Critically.WARN -> Color(0xFFFF8C00) // Orange
        Critically.INFO -> Color.Unspecified
    }

    val fontWeight = if (message.critically == Critically.ERROR) FontWeight.Bold else FontWeight.Normal

    Text(
        text = message.message,
        color = textColor,
        fontWeight = fontWeight,
        modifier = Modifier.padding(bottom = 4.dp)
    )
}


