package com.d20charactersheet.finance.domain

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class MonthlyStatementTest {

    @Test
    fun instantiateMonthlyStatement() {
        // act
        val monthlyStatement = MonthlyStatement(1, "Name", 1.2, CategoryType.EXPENSE)

        // assert
        assertThat(monthlyStatement.categoryId).isEqualTo(1)
        assertThat(monthlyStatement.categoryName).isEqualTo("Name")
        assertThat(monthlyStatement.total).isEqualTo(1.2)
        assertThat(monthlyStatement.categoryType).isEqualTo(CategoryType.EXPENSE)
    }
}