package com.d20charactersheet.finance.gui

import com.d20charactersheet.finance.domain.Category
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class CategoryFilterTest {

    @Test
    fun filterCategories_emptySearchText_returnAllCategories() {
        // arrange
        val categories = listOf(
            Category(1, "first category"),
            Category(2, "second category"),
        )

        // act
        val filteredCategories = CategoryFilter().filter("", categories)

        // assert
        assertThat(filteredCategories).containsExactlyInAnyOrder(
            Category(1, "first category"),
            Category(2, "second category")
        )
    }

    @Test
    fun filterCategories_withExactSearchText_returnOneCategory() {
        // arrange
        val categories = listOf(
            Category(1, "first category"),
            Category(2, "second category"),
        )

        // act
        val filteredCategories = CategoryFilter().filter("first", categories)

        // assert
        assertThat(filteredCategories).containsExactly(Category(1, "first category"))
    }

    @Test
    fun filterCategories_withSearchTextForManyCategories_returnManyCategory() {
        // arrange
        val categories = listOf(
            Category(1, "first category"),
            Category(2, "second category"),
            Category(3, "third item"),
        )

        // act
        val filteredCategories = CategoryFilter().filter("category", categories)

        // assert
        assertThat(filteredCategories).containsExactlyInAnyOrder(
            Category(1, "first category"),
            Category(2, "second category")
        )
    }

}