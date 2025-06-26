package com.d20charactersheet.finance.service

import com.d20charactersheet.finance.domain.MoneyTransfer
import com.d20charactersheet.finance.domain.RawMoneyTransfer
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.stereotype.Service
import java.time.format.DateTimeFormatter


@Service
class MoneyTransferService(
    @Autowired private val jdbcTemplate: JdbcTemplate
) {

    private val formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy")

    fun filterNewMoneyTransfers(rawMoneyTransfers: List<RawMoneyTransfer>): List<MoneyTransfer> {
        val moneyTransfers = rawMoneyTransfers.map { it.toMoneyTransfer() }
        return moneyTransfers.filter { !isDuplicateMoneyTransfer(it) }
    }

    @Suppress("BooleanMethodIsAlwaysInverted")
    fun save(moneyTransfer: MoneyTransfer): Boolean {
        println(moneyTransfer)
        if (isDuplicateMoneyTransfer(moneyTransfer)) return false
        val updatedRows = insertTransaction(moneyTransfer)
        return updatedRows == 1
    }

    private fun isDuplicateMoneyTransfer(moneyTransfer: MoneyTransfer): Boolean {
        val formattedDate: String = moneyTransfer.valutaDate.date.format(formatter)
        val sql = """SELECT count(*)
            |FROM umsaetze
            |WHERE datum = #${formattedDate}#
            |AND betrag = ${moneyTransfer.amount.value}
            |AND anzeigetext = '${moneyTransfer.recipient.name}'""".trimMargin()
        val result = jdbcTemplate.queryForObject(
            sql,
            Int::class.java,
        )
        return result != null && result > 0
    }

    private fun insertTransaction(moneyTransfer: MoneyTransfer) = jdbcTemplate.update(
        "INSERT INTO umsaetze (datum, anzeigetext, verwendungszweck, betrag, kategorieId, beschreibung, quelleId) VALUES (?, ?, ?, ?, ? ,?, ?)",
        moneyTransfer.valutaDate.date,
        moneyTransfer.recipient.name,
        moneyTransfer.reasonForTransfer.text,
        moneyTransfer.amount.value,
        moneyTransfer.category.id,
        moneyTransfer.comment.text,
        moneyTransfer.paymentInstrument.id
    )

}
