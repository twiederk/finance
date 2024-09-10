package com.d20charactersheet.finance.import

import com.d20charactersheet.finance.domain.RawMoneyTransfer
import java.io.File
import kotlin.streams.toList

class IngFileParser : FileParser {

    override fun readMoneyTransfersFromFile(fileNames: Array<out String?>): List<RawMoneyTransfer> {
        val fileName = retrieveFilename(fileNames)
        fileName?.let {
            val linesInFile: List<String> = File(fileName).useLines { it.toList() }
            val startIndex = findStartIndexOfMoneyTransfers(linesInFile)
            return importMoneyTransfers(linesInFile, startIndex)
        }
        return listOf()
    }

    private fun retrieveFilename(args: Array<out String?>): String? {
        var filename: String? = null
        if (args.size == 1) {
            filename = args[0]
        }
        return filename
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
        return linesInFile.stream().skip(startIndex).map { IngRawMoneyTransferParser().parseRawMoneyTransfer(it) }
            .toList()
    }

}
