package com.d20charactersheet.finance

import com.d20charactersheet.finance.domain.Categories
import com.d20charactersheet.finance.domain.MoneyTransfer
import com.d20charactersheet.finance.domain.PaymentInstruments
import com.d20charactersheet.finance.domain.RawMoneyTransfer
import com.d20charactersheet.finance.gui.MainWindow
import com.d20charactersheet.finance.import.FileParser
import com.d20charactersheet.finance.service.CategorySuggestion
import com.d20charactersheet.finance.service.MoneyTransferService
import com.d20charactersheet.finance.service.PaymentSuggestion
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.builder.SpringApplicationBuilder


@SpringBootApplication
class FinanceApplication : CommandLineRunner {

    @Autowired
    private lateinit var categories: Categories

    @Autowired
    private lateinit var paymentInstruments: PaymentInstruments

    @Autowired
    private lateinit var moneyTransferService: MoneyTransferService

    @Autowired
    private lateinit var paymentSuggestion: PaymentSuggestion

    @Autowired
    private lateinit var categorySuggestion: CategorySuggestion

    override fun run(vararg args: String?) {
        val rawMoneyTransfers: List<RawMoneyTransfer> = FileParser().readMoneyTransfersFromFile(args)
        val moneyTransfers: List<MoneyTransfer> = moneyTransferService.filterNewMoneyTransfers(rawMoneyTransfers)
        paymentSuggestion.suggestPaymentInstrument(moneyTransfers, paymentInstruments)
        categorySuggestion.suggestCategory(moneyTransfers)
        MainWindow(moneyTransfers, moneyTransferService, categories, paymentInstruments)
    }

}


fun main(args: Array<String>) {
    SpringApplicationBuilder(FinanceApplication::class.java).headless(false).run(*args)
}

