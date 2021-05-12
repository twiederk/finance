package com.d20charactersheet.finance.import

import com.d20charactersheet.finance.domain.MoneyTransfer
import com.d20charactersheet.finance.domain.MoneyTransfers
import java.io.File
import kotlin.streams.toList

class FileParser {

    fun readMoneyTransfersFromFile(fileName: String): MoneyTransfers {
        val linesInFile: List<String> = File(fileName).useLines { it.toList() }
        val startIndex = findStartIndexOfMoneyTransfers(linesInFile)
        val moneyTransfers: List<MoneyTransfer> = importMoneyTransfers(linesInFile, startIndex)
        return MoneyTransfers(moneyTransfers)
    }

    private fun findStartIndexOfMoneyTransfers(linesInFile: List<String>): Long {
        for (i in linesInFile.indices) {
            if (linesInFile[i].startsWith("Buchung")) {
                return i + 1L
            }
        }
        throw IllegalArgumentException("Can't find money transfers in file")
    }

    private fun importMoneyTransfers(linesInFile: List<String>, startIndex: Long): List<MoneyTransfer> {
        return linesInFile.stream().skip(startIndex).map { MoneyTransferParser().parseMoneyTransfer(it) }.toList()
    }

}
