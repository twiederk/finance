package com.d20charactersheet.finance

import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowState
import androidx.compose.ui.window.application
import com.d20charactersheet.finance.domain.Categories
import com.d20charactersheet.finance.domain.MoneyTransfer
import com.d20charactersheet.finance.domain.PaymentInstruments
import com.d20charactersheet.finance.domain.RawMoneyTransfer
import com.d20charactersheet.finance.gui.App
import com.d20charactersheet.finance.import.FileParser
import com.d20charactersheet.finance.service.CategorySuggestion
import com.d20charactersheet.finance.service.MoneyTransferService
import com.d20charactersheet.finance.service.PaymentSuggestionDb
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.builder.SpringApplicationBuilder


@SpringBootApplication
class FinanceApplication : CommandLineRunner {

    override fun run(vararg args: String?) {}

}

fun main(args: Array<String>) {
    val applicationContext = SpringApplicationBuilder(FinanceApplication::class.java).headless(false).run(*args)

    val moneyTransferService = applicationContext.getBean(MoneyTransferService::class.java)
    val paymentInstruments = applicationContext.getBean(PaymentInstruments::class.java)
    val paymentSuggestion = applicationContext.getBean(PaymentSuggestionDb::class.java)
    val categorySuggestion = applicationContext.getBean(CategorySuggestion::class.java)
    val categories = applicationContext.getBean(Categories::class.java)

    val rawMoneyTransfers: List<RawMoneyTransfer> = FileParser().readMoneyTransfersFromFile(args)
    val moneyTransfers: List<MoneyTransfer> = moneyTransferService.filterNewMoneyTransfers(rawMoneyTransfers)
    paymentSuggestion.suggestPaymentInstrument(moneyTransfers, paymentInstruments)
    categorySuggestion.suggestCategory(moneyTransfers)

    application {
        Window(
            onCloseRequest = ::exitApplication,
            title = "Finance Application (1.11.0-SNAPSHOT)",
            state = WindowState(width = 1600.dp, height = 800.dp)
        ) {
            App(moneyTransfers, moneyTransferService, categories, paymentInstruments)
        }
    }
}

