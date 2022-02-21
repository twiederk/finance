package com.d20charactersheet.finance.service

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever
import java.sql.ResultSet

internal class PaymentInstrumentRuleRowMapperTest {

    @Test
    internal fun mapRow_validResultSet_mapPaymentInstrumentRule() {
        // arrange
        val resultSet: ResultSet = mock()
        whenever(resultSet.getString("PaymentInstrumentText")).thenReturn("myPaymentInstrument")
        whenever(resultSet.getInt("PaymentInstrumentId")).thenReturn(1)

        // act
        val paymentInstrumentRule = PaymentInstrumentRuleRowMapper().mapRow(resultSet, 1)

        // assert
        assertThat(paymentInstrumentRule.text).isEqualTo("myPaymentInstrument")
        assertThat(paymentInstrumentRule.paymentInstrumentId).isEqualTo(1)
    }

}