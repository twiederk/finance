package com.d20charactersheet.finance.domain

import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.eq
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.core.RowMapper


class CategoriesTest {

    private val jdbcTemplate: JdbcTemplate = mock()
    private lateinit var underTest: Categories

    @BeforeEach
    fun beforeEach() {
        whenever(jdbcTemplate.query(eq("SELECT Id, Kategorie FROM Kategorie"), any<RowMapper<Category>>()))
            .thenReturn(
                mutableListOf(
                    Category(1, "Abc"),
                    Category(2, "abc"),
                    Category(3, "a b")
                )
            )
        underTest = Categories(jdbcTemplate)
    }

    @Test
    fun numberOfCategories_returnTotalNumberOfCategories() {

        // act
        val numberOfCategories = underTest.numberOfCategories()

        // assert
        assertThat(numberOfCategories).isEqualTo(3)
    }

    @Test
    fun getCategories_alphabeticallyOrderedListOfAllCategories() {

        // act
        val categories = underTest.categories

        // assert
        assertThat(categories).hasSize(3)
        assertThat(categories[0].name).isEqualTo("a b")
        assertThat(categories[1].name).isEqualTo("Abc")
        assertThat(categories[2].name).isEqualTo("abc")
    }

}