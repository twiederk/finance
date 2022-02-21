package com.d20charactersheet.finance.service

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever
import java.sql.ResultSet

internal class CategoryRuleRowMapperTest {

    @Test
    internal fun mapRow_validResultSet_mapCategoryRule() {
        // arrange
        val resultSet: ResultSet = mock()
        whenever(resultSet.getString("CategoryText")).thenReturn("myCategory")
        whenever(resultSet.getInt("CategoryId")).thenReturn(1)

        // act
        val categoryRule = CategoryRuleRowMapper().mapRow(resultSet, 1)

        // assert
        assertThat(categoryRule.text).isEqualTo("myCategory")
        assertThat(categoryRule.categoryId).isEqualTo(1)
    }

}