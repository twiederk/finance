package com.d20charactersheet.finance.domain

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class ExportMessageComparatorTest {

    @Test
    fun sortList_ordersCorrectly() {
        // arrange
        val messages = listOf(
            ExportMessage(Critically.INFO, "info message 1"),
            ExportMessage(Critically.ERROR, "error message 1"),
            ExportMessage(Critically.WARN, "warn message 1"),
            ExportMessage(Critically.INFO, "info message 2"),
            ExportMessage(Critically.WARN, "warn message 2"),
            ExportMessage(Critically.ERROR, "error message 2")
        )

        // act
        val sortedMessages = messages.sortedWith(ExportMessageComparator())

        // assert
        assertThat(sortedMessages).containsExactly(
            ExportMessage(Critically.ERROR, "error message 1"),
            ExportMessage(Critically.ERROR, "error message 2"),
            ExportMessage(Critically.WARN, "warn message 1"),
            ExportMessage(Critically.WARN, "warn message 2"),
            ExportMessage(Critically.INFO, "info message 1"),
            ExportMessage(Critically.INFO, "info message 2")
        )
    }
}

