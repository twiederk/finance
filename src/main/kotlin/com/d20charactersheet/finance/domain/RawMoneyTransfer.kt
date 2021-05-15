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

data class EntryDate(val entryDate: LocalDate)
data class ValutaDate(val valutaDate: LocalDate) {
    override fun toString(): String {
        return valutaDate.toString()
    }
}

data class Recipient(val recipient: String)
data class PostingText(val postingText: String)
data class IngCategory(val ingCategory: String)
data class HashTag(val hashTag: String)
data class ReasonForTransfer(val reasonForTransfer: String) {
    override fun toString(): String {
        return reasonForTransfer
    }

}

data class Amount(val amount: Float) {
    override fun toString(): String {
        return amount.toString()
    }
}

data class Currency(val currency: String) {
    override fun toString(): String {
        return currency
    }
}


