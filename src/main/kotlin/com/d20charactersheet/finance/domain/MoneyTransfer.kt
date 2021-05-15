package com.d20charactersheet.finance.domain

data class MoneyTransfer(
    val id: Int = 0,
    val valutaDate: ValutaDate,
    val recipient: Recipient,
    val amount: Amount,
    val category: Category = EmptyCategory,
    val comment: Comment = Comment(""),
    val paymentInstrument: PaymentInstrument = EmptyPaymentInstrument
)

data class Category(val id: Int, val name: String)
data class Comment(val text: String)
data class PaymentInstrument(val id: Int, val name: String)

val EmptyCategory = Category(0, "empty")
val EmptyPaymentInstrument = PaymentInstrument(0, "empty")
