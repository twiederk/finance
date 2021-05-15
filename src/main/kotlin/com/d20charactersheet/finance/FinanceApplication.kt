package com.d20charactersheet.finance

import com.d20charactersheet.finance.domain.MoneyTransfer
import com.d20charactersheet.finance.domain.RawMoneyTransfer
import com.d20charactersheet.finance.gui.MainWindow
import com.d20charactersheet.finance.import.FileParser
import com.d20charactersheet.finance.service.MoneyTransferService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.builder.SpringApplicationBuilder


@SpringBootApplication
class FinanceApplication : CommandLineRunner {

    @Autowired
    private lateinit var moneyTransferService: MoneyTransferService

    override fun run(vararg args: String?) {
        val rawMoneyTransfers: List<RawMoneyTransfer> =
            FileParser().readMoneyTransfersFromFile("./src/main/resources/Umsatzanzeige_DE79500105175421442263_20210522.csv")
        val moneyTransfers: List<MoneyTransfer> = moneyTransferService.filterNewMoneyTransfers(rawMoneyTransfers)

        MainWindow(moneyTransfers)
    }
}


fun main(args: Array<String>) {
    SpringApplicationBuilder(FinanceApplication::class.java).headless(false).run(*args)
}

