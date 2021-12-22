package com.d20charactersheet.finance.service

import com.d20charactersheet.finance.domain.MoneyTransfer
import com.d20charactersheet.finance.domain.PaymentInstrument
import com.d20charactersheet.finance.domain.PaymentInstruments
import org.springframework.stereotype.Component

@Component
class PaymentSuggestion {

    private val paymentInstrumentRules = mapOf(
        "PayPal" to 1,
        "VISA" to 3,
    )

    fun suggestPaymentInstrument(moneyTransfers: List<MoneyTransfer>, paymentInstruments: PaymentInstruments) {
        moneyTransfers.stream().forEach {
            it.paymentInstrument = suggestPaymentInstrument(it, paymentInstruments)
        }
    }

    private fun suggestPaymentInstrument(
        moneyTransfer: MoneyTransfer,
        paymentInstruments: PaymentInstruments
    ): PaymentInstrument {
        for (paymentInstrumentRule in paymentInstrumentRules) {
            if (moneyTransfer.recipient.name.startsWith(paymentInstrumentRule.key)) {
                return paymentInstruments.findPaymentInstrumentById(paymentInstrumentRule.value)
            }
        }
        return paymentInstruments.findPaymentInstrumentById(4)
    }

}
