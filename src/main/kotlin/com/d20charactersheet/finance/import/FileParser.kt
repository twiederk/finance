package com.d20charactersheet.finance.import

import com.d20charactersheet.finance.domain.RawMoneyTransfer

interface FileParser {

    fun readMoneyTransfersFromFile(fileNames: Array<out String?>): List<RawMoneyTransfer>

}