package com.d20charactersheet.finance.service

import org.springframework.jdbc.core.RowMapper
import java.sql.ResultSet


class PaymentInstrumentRuleRowMapper : RowMapper<PaymentInstrumentRule> {

    override fun mapRow(rs: ResultSet, rowNum: Int) = PaymentInstrumentRule(
        rs.getString("PaymentInstrumentText"),
        rs.getInt("PaymentInstrumentId")
    )

}
