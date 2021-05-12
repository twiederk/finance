package com.d20charactersheet.finance.domain

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class CategoryTest {

    @Test
    fun instantiateCategory() {
        // act
        val category = Category(1, "myCategory")

        // assert
        assertThat(category.id).isEqualTo(1)
        assertThat(category.name).isEqualTo("myCategory")
    }

}