package com.d20charactersheet.finance.domain

import java.time.LocalDate

data class MoneyTransfer(
    val entryDate: EntryDate,
    val valutaDate: ValutaDate,
    val recipient: Recipient,
    val postingText: PostingText,
    val ingCategory: IngCategory,
    val hashTag: HashTag,
    val reasonForTransfer: ReasonForTransfer,
    val amount: Amount,
    val currency: Currency
)

data class EntryDate(val entryDate: LocalDate)
data class ValutaDate(val ValutaDate: LocalDate)
data class Recipient(val recipient: String)
data class PostingText(val postingText: String)
data class IngCategory(val ingCategory: String)
data class HashTag(val hashTag: String)
data class ReasonForTransfer(val reasonForTransfer: String)
data class Amount(val amount: Float)
data class Currency(val currency: String)



