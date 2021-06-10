package com.d20charactersheet.finance.gui

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import com.d20charactersheet.finance.domain.Category
import com.d20charactersheet.finance.domain.Comment
import com.d20charactersheet.finance.domain.MoneyTransfer
import com.d20charactersheet.finance.domain.PaymentInstrument
import com.d20charactersheet.finance.service.MoneyTransferService

class MoneyTransferViewModel(
    private val moneyTransfer: MoneyTransfer,
    private val moneyTransferService: MoneyTransferService
) {

    var paymentInstrument: MutableState<PaymentInstrument> = mutableStateOf(moneyTransfer.paymentInstrument)
    var comment: MutableState<String> = mutableStateOf(moneyTransfer.comment.text)
    var category: MutableState<Category> = mutableStateOf(moneyTransfer.category)


    fun onCategoryChange(category: Category) {
        this.category.value = category
    }

    fun onPaymentInstrumentChange(paymentInstrument: PaymentInstrument) {
        this.paymentInstrument.value = paymentInstrument
    }

    fun onCommit() {
        moneyTransfer.category = category.value
        moneyTransfer.paymentInstrument = paymentInstrument.value
        moneyTransfer.comment = Comment(comment.value)
        moneyTransferService.save(moneyTransfer)
    }

}