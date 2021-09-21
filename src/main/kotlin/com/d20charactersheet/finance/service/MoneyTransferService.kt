package com.d20charactersheet.finance.service

import com.d20charactersheet.finance.domain.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.stereotype.Service
import java.time.format.DateTimeFormatter


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
            "REWE Oevuenc Bekar oHG" to 19,
            "Getraenkemarkt Ostheimer GmbH" to 19,
            "VISA ALLDRINK GROSSWALLSTAD" to 19,
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
            "BAYERN VERSICHERUNG LEBENSVERSICHERUNG AG" to 65,
            "vbba - Gewerkschaft Arbeit und Soziales" to 63,
            "Google Payment Ireland Limited" to 42,
            "s.Oliver Bernd Freier GmbH + Co.KG" to 6,
            "IONOS SE" to 114,
            "VISA ROSSMANN 3216" to 24,
            "VISA MUELLER MH HANDELS GMB" to 24,
            "VISA MUELLER GMBH & CO.KG" to 24,
            "VISA DM-DROGERIE MARKT" to 24,
            "AOK Bayern" to 130,
            "VISA TAKKO 1609" to 6,
            "myToys.de GmbH" to 6,
            "Kath. Kirchenstiftung Grosswallstad t" to 80,
            "VISA AMZNPRIME DE*9H56P3UY5" to 23,
        )
    }

    private val formatter = DateTimeFormatter.ofPattern("MM/dd/YYYY")

    fun filterNewMoneyTransfers(rawMoneyTransfers: List<RawMoneyTransfer>): List<MoneyTransfer> =
        rawMoneyTransfers.filter { it.hashTag.text == "" }.map { it.toMoneyTransfer() }

    fun save(moneyTransfer: MoneyTransfer): Boolean {
        println(moneyTransfer)
        if (isDuplicateMoneyTransfer(moneyTransfer)) return false
        val updatedRows = insertTransaction(moneyTransfer)
        return updatedRows == 1
    }

    private fun isDuplicateMoneyTransfer(moneyTransfer: MoneyTransfer): Boolean {
        val formattedDate: String = moneyTransfer.valutaDate.date.format(formatter)
        val result = jdbcTemplate.queryForObject(
            """SELECT count(*)
                |FROM umsaetze
                |WHERE datum = #${formattedDate}#
                |AND betrag = ${moneyTransfer.amount.value}
                |AND anzeigetext = '${moneyTransfer.recipient.name}'""".trimMargin(),
            Int::class.java,
        )
        return result != null && result > 0
    }

    private fun insertTransaction(moneyTransfer: MoneyTransfer) = jdbcTemplate.update(
        "INSERT INTO umsaetze (datum, anzeigetext, betrag, kategorieId, beschreibung, quelleId) VALUES (?, ?, ?, ?, ? ,?)",
        moneyTransfer.valutaDate.date,
        moneyTransfer.recipient.name,
        moneyTransfer.amount.value,
        moneyTransfer.category.id,
        moneyTransfer.comment.text,
        moneyTransfer.paymentInstrument.id
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
