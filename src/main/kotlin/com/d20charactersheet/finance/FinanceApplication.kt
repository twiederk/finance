package com.d20charactersheet.finance

import com.d20charactersheet.finance.domain.Categories
import com.d20charactersheet.finance.domain.PaymentInstruments
import com.d20charactersheet.finance.gui.MainWindow
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

    override fun run(vararg args: String?) {
        MainWindow(categories, paymentInstruments)
    }
}


fun main(args: Array<String>) {
    SpringApplicationBuilder(FinanceApplication::class.java).headless(false).run(*args)
}

