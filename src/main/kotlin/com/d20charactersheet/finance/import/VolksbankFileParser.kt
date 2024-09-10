package com.d20charactersheet.finance.import

import com.d20charactersheet.finance.domain.RawMoneyTransfer
import java.io.File
import kotlin.streams.toList

class VolksbankFileParser : FileParser {

    override fun readMoneyTransfersFromFile(fileNames: Array<out String?>): List<RawMoneyTransfer> {
        val fileName = retrieveFilename(fileNames)
        fileName?.let {
            val linesInFile: List<String> = File(fileName).useLines { it.toList() }
            return importMoneyTransfers(linesInFile)
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

    private fun importMoneyTransfers(linesInFile: List<String>): List<RawMoneyTransfer> {
        return linesInFile.stream().skip(1).map { VolksbankRawMoneyTransferParser().parseRawMoneyTransfer(it) }.toList()
    }

}
