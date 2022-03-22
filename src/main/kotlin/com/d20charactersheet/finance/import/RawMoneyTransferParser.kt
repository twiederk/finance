package com.d20charactersheet.finance.import

import com.d20charactersheet.finance.domain.*
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class RawMoneyTransferParser {

    private val formatter: DateTimeFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy")

    fun parseRawMoneyTransfer(rawDataString: String): RawMoneyTransfer {
        val rawDataList = rawDataString.split(";")

        val entryDate = LocalDate.parse(rawDataList[0], formatter)
        val valutaDate = LocalDate.parse(rawDataList[1], formatter)
        val recipient = rawDataList[2]
        val postingText = rawDataList[3]
        val note = rawDataList[4]
        val reasonForTransfer = rawDataList[5]
        val amount = rawDataList[6].replace(",", "x").replace(".", "").replace("x", ".").toFloat()
        val currency = rawDataList[7]

        return RawMoneyTransfer(
            EntryDate(entryDate),
            ValutaDate(valutaDate),
            Recipient(recipient),
            PostingText(postingText),
            Note(note),
            ReasonForTransfer(reasonForTransfer),
            Amount(amount),
            Currency(currency)
        )
    }


}
