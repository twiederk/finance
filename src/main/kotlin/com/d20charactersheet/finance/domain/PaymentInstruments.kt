package com.d20charactersheet.finance.domain

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.stereotype.Component

@Component
class PaymentInstruments(
    @Autowired private val template: JdbcTemplate
) {

    val paymentInstruments: List<PaymentInstrument> = template.query("SELECT Id, Name FROM Quelle") { rs, _ ->
        PaymentInstrument(rs.getInt("id"), rs.getString("Name"))
    }.sortedBy { it.name.uppercase() }

    fun numberOfPaymentInstruments(): Int = paymentInstruments.size

}
