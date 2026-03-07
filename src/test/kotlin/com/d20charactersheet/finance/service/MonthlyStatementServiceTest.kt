package com.d20charactersheet.finance.service

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.mockito.ArgumentMatchers.anyString
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.support.rowset.SqlRowSet

class MonthlyStatementServiceTest {

    @Test
    fun monthlyStatements() {
        // arrange
        val jdbcTemplate: JdbcTemplate = mock()
        val sqlRowSet: SqlRowSet = mock()
        whenever(sqlRowSet.next()).thenReturn(false)
        whenever(jdbcTemplate.queryForRowSet(anyString())).thenReturn(sqlRowSet)

        // act
        val result = MonthlyStatementService(jdbcTemplate).monthlyStatements(2021, 5)

        // assert
        assertThat(result).isNotNull
    }
}