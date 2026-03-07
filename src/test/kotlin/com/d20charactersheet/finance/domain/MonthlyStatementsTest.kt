package com.d20charactersheet.finance.domain

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class MonthlyStatementsTest {


    @Test
    fun instantiateMonthlyStatements() {

        // arrange
        val statements = listOf(
            MonthlyStatement(1, "First category", 10.0, categoryType = CategoryType.EXPENSE),
            MonthlyStatement(2, "Second category", 20.0, categoryType = CategoryType.INCOME),
        )

        // act
        val monthlyStatements = MonthlyStatements(
            statements,
            2021,
            5
        )

        // assert
        assertThat(monthlyStatements.monthlyStatements).hasSize(2)
        assertThat(monthlyStatements.year).isEqualTo(2021)
        assertThat(monthlyStatements.month).isEqualTo(5)
    }
}