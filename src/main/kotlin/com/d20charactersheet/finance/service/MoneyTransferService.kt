package com.d20charactersheet.finance.service

import com.d20charactersheet.finance.domain.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.stereotype.Service

@Service
class MoneyTransferService(
    @Autowired private val jdbcTemplate: JdbcTemplate
) {

    companion object {
        val paymentInstrumentRules = mapOf(
            "PayPal" to 1,
            "VISA" to 3,
        )

        val categoryRules = mapOf(
            "VISA BAUER-MARKT GMBH" to 19,
            "IHR BAUER-MARKT SAGT DANKE" to 19,
            "VISA REWE OEVUENC BEKAR OHG" to 19,
            "Getraenkemarkt Ostheimer GmbH" to 19,
            "VISA BACKHAUS BICKERT GMBH" to 9,
            "DER BROTMACHER GMBH" to 9,
            "LichtBlick SE" to 50,
            "E.ON Energie Deutsch" to 46,
            "Telekom Deutschland GmbH" to 52,
            "Telefonica Germany GmbH + Co. OHG" to 40,
            "Bundesagentur fur Arbeit - Familienkasse" to 67,
            "VISA AWS EMEA" to 22,
            "Wuerttembergische Versicherung AG" to 37,
            "BAYERN-VERSICHERUNG LEBENSVERSICHERUNG AG" to 65,
        )
    }

    fun filterNewMoneyTransfers(rawMoneyTransfers: List<RawMoneyTransfer>): List<MoneyTransfer> =
        rawMoneyTransfers.filter { it.hashTag.text == "" }.map { it.toMoneyTransfer() }

    fun save(moneyTransfer: MoneyTransfer) {
        println(moneyTransfer)
        jdbcTemplate.update(
            "INSERT INTO umsaetze (datum, anzeigetext, betrag, kategorieId, beschreibung, quelleId) VALUES (?, ?, ?, ?, ? ,?)",
            moneyTransfer.valutaDate.date,
            moneyTransfer.recipient.name,
            moneyTransfer.amount.value,
            moneyTransfer.category.id,
            moneyTransfer.comment.text,
            moneyTransfer.paymentInstrument.id
        )
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
            if (moneyTransfer.recipient.name.startsWith(paymentInstrumentRule.key)) {
                return paymentInstruments.findPaymentInstrumentById(paymentInstrumentRule.value)
            }
        }
        return paymentInstruments.findPaymentInstrumentById(4)
    }

    fun suggestCategory(moneyTransfers: List<MoneyTransfer>, categories: Categories) {
        moneyTransfers.stream().forEach {
            it.category = suggestCategory(it, categories)
        }
    }

    fun suggestCategory(moneyTransfer: MoneyTransfer, categories: Categories): Category {
        for (categoryRule in categoryRules) {
            if (moneyTransfer.recipient.name.startsWith(categoryRule.key)) {
                return categories.findCategoryId(categoryRule.value)
            }
        }
        return EmptyCategory
    }
}
