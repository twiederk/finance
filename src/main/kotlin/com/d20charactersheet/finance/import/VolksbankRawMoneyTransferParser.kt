package com.d20charactersheet.finance.import

import com.d20charactersheet.finance.domain.*
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class VolksbankRawMoneyTransferParser : RawMoneyTransferParser {

    private val formatter: DateTimeFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy")

    override fun parseRawMoneyTransfer(rawDataString: String): RawMoneyTransfer {
        val rawDataList = rawDataString.split(";")

        val entryDate = LocalDate.parse(rawDataList[4], formatter)
        val valutaDate = LocalDate.parse(rawDataList[5], formatter)
        val recipient = rawDataList[6]
        val postingText = rawDataList[9]
        val reasonForTransfer = rawDataList[10]
        val amount = rawDataList[11].replace(",", "x").replace(".", "").replace("x", ".").toFloat()
        val currency = rawDataList[12]

        return RawMoneyTransfer(
            EntryDate(entryDate),
            ValutaDate(valutaDate),
            Recipient(recipient),
            PostingText(postingText),
            ReasonForTransfer(reasonForTransfer),
            Amount(amount),
            Currency(currency)
        )
    }


}
