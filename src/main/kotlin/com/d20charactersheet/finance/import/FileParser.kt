package com.d20charactersheet.finance.import

import com.d20charactersheet.finance.domain.RawMoneyTransfer
import java.io.File
import kotlin.streams.toList

class FileParser {

    fun readMoneyTransfersFromFile(fileName: String): List<RawMoneyTransfer> {
        val linesInFile: List<String> = File(fileName).useLines { it.toList() }
        val startIndex = findStartIndexOfMoneyTransfers(linesInFile)
        return importMoneyTransfers(linesInFile, startIndex)
    }

    private fun findStartIndexOfMoneyTransfers(linesInFile: List<String>): Long {
        for (i in linesInFile.indices) {
            if (linesInFile[i].startsWith("Buchung")) {
                return i + 1L
            }
        }
        throw IllegalArgumentException("Can't find money transfers in file")
    }

    private fun importMoneyTransfers(linesInFile: List<String>, startIndex: Long): List<RawMoneyTransfer> {
        return linesInFile.stream().skip(startIndex).map { RawMoneyTransferParser().parseRawMoneyTransfer(it) }.toList()
    }

}
