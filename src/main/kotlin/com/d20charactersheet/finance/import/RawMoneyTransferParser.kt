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
        val ingCategory = rawDataList[4]
        val hashTag = rawDataList[5]
        val reasonForTransfer = rawDataList[6]
        val amount = rawDataList[7].replace(",", "x").replace(".", "").replace("x", ".").toFloat()
        val currency = rawDataList[8]

        return RawMoneyTransfer(
            EntryDate(entryDate),
            ValutaDate(valutaDate),
            Recipient(recipient),
            PostingText(postingText),
            IngCategory(ingCategory),
            HashTag(hashTag),
            ReasonForTransfer(reasonForTransfer),
            Amount(amount),
            Currency(currency)
        )
    }


}
