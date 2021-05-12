package com.d20charactersheet.finance.import

import com.d20charactersheet.finance.domain.*
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class MoneyTransferParser {

    private val formatter: DateTimeFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy")

    fun parseMoneyTransfer(rawDataString: String): MoneyTransfer {
        val rawDataList = rawDataString.split(";")

        val entryDate = LocalDate.parse(rawDataList[0], formatter)
        val valutaDate = LocalDate.parse(rawDataList[1], formatter)
        val recipient = rawDataList[2]
        val postingText = rawDataList[3]
        val ingCategory = rawDataList[4]
        val hashTag = rawDataList[5]
        val reasonForTransfer = rawDataList[6]
        val amount = rawDataList[7].replace(",", ".").toFloat()
        val currency = rawDataList[8]

        return MoneyTransfer(
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
