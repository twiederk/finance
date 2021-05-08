package com.d20charactersheet.finance

import com.d20charactersheet.finance.gui.MainWindow
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.builder.SpringApplicationBuilder


@SpringBootApplication
class FinanceApplication : CommandLineRunner {

    @Autowired
    private lateinit var mainWindow: MainWindow

    override fun run(vararg args: String?) {
        mainWindow.show()
    }
}


fun main(args: Array<String>) {
    SpringApplicationBuilder(FinanceApplication::class.java).headless(false).run(*args)
}

