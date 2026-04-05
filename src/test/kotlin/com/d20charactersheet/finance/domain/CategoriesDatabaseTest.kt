package com.d20charactersheet.finance.domain

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class CategoriesDatabaseTest {

    @Autowired
    private lateinit var underTest: Categories

    @Test
    fun numberOfCategories_returnTotalNumberOfCategories() {
        // act
        val numberOfCategories = underTest.numberOfCategories()

        // assert
        assertThat(numberOfCategories).isEqualTo(9)
    }

    @Test
    fun getCategories_alphabeticallyOrderedListOfAllCategories() {

        // act
        val categories = underTest.categories

        // assert
        assertThat(categories.map { it.name }).containsExactlyInAnyOrder(
            "Abwassergebühren",
            "Bücher / Musik / Filme",
            "Freizeit",
            "Geschenk",
            "Spielzeug",
            "Klamotten",
            "Streaming",
            "Lebensmittel",
            "Gehalt Torsten",
        )
    }

}