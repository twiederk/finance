package com.d20charactersheet.finance

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class CategoriesTest {

    @Autowired
    private lateinit var underTest: Categories

    @Test
    fun numberOfCategories_returnTotalNumberOfCategories() {
        // act
        val numberOfCategories = underTest.numberOfCategories()

        // assert
        assertThat(numberOfCategories).isEqualTo(111)
    }

    @Test
    internal fun getCategories_alphabeticallyOrderedListOfAllCategories() {
        // arrange

        // act
        val categories = underTest.categories

        // assert
        assertThat(categories).hasSize(111)
        assertThat(categories[0].name).isEqualTo("Abfallgebühren (Müll)")
        assertThat(categories[1].name).isEqualTo("Abwassergebühren")
        assertThat(categories[2].name).isEqualTo("Amazon Prime")
    }

}