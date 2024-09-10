package com.d20charactersheet.finance.import

import com.d20charactersheet.finance.domain.RawMoneyTransfer

interface RawMoneyTransferParser {

    fun parseRawMoneyTransfer(rawDataString: String): RawMoneyTransfer

}