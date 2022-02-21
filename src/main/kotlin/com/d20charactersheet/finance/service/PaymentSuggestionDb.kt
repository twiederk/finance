package com.d20charactersheet.finance.service

import com.d20charactersheet.finance.domain.MoneyTransfer
import com.d20charactersheet.finance.domain.PaymentInstrument
import com.d20charactersheet.finance.domain.PaymentInstruments
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.stereotype.Component

@Component
class PaymentSuggestionDb(
    @Autowired private val jdbcTemplate: JdbcTemplate
) {

    companion object {
        const val DEFAULT_PAYMENT_INSTRUMENT_ID = 4
    }

    final var paymentInstrumentRules: Map<String, Int> = mutableMapOf()

    init {
        val paymentInstrumentRulesList: MutableList<PaymentInstrumentRule> =
            jdbcTemplate.query(
                "SELECT PaymentInstrumentText, PaymentInstrumentId FROM PaymentInstrumentRule",
                PaymentInstrumentRuleRowMapper()
            )
        paymentInstrumentRules = paymentInstrumentRulesList.associate { it.text to it.paymentInstrumentId }
    }

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
            if (moneyTransfer.recipient.name.startsWith(paymentInstrumentRule.key)
                || moneyTransfer.category.name.startsWith(paymentInstrumentRule.key)
            ) {
                return paymentInstruments.findPaymentInstrumentById(paymentInstrumentRule.value)
            }
        }
        return paymentInstruments.findPaymentInstrumentById(DEFAULT_PAYMENT_INSTRUMENT_ID)
    }

}
