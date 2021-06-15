package com.d20charactersheet.finance.domain

import java.time.LocalDate

data class RawMoneyTransfer(
    val entryDate: EntryDate,
    val valutaDate: ValutaDate,
    val recipient: Recipient,
    val postingText: PostingText,
    val ingCategory: IngCategory,
    val hashTag: HashTag,
    val reasonForTransfer: ReasonForTransfer,
    val amount: Amount,
    val currency: Currency
) {
    fun toMoneyTransfer(): MoneyTransfer {
        return MoneyTransfer(
            valutaDate = valutaDate,
            recipient = recipient,
            amount = amount
        )
    }
}

data class EntryDate(val date: LocalDate)
data class ValutaDate(val date: LocalDate) {
    override fun toString(): String {
        return date.toString()
    }
}

data class Recipient(val name: String)
data class PostingText(val text: String)
data class IngCategory(val name: String)
data class HashTag(val text: String)
data class ReasonForTransfer(val text: String) {
    override fun toString(): String {
        return text
    }

}

data class Amount(val value: Float) {
    override fun toString(): String {
        return value.toString()
    }
}

data class Currency(val name: String) {
    override fun toString(): String {
        return name
    }
}

