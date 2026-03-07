package com.d20charactersheet.finance.domain

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import java.nio.file.Path

class ExportSummaryTest {

    @Test
    fun instantiateExportSummary() {
        // arrange
        val backupFilePath = Path.of("directory", "backupFileName")
        val messages = listOf(
            ExportMessage(Critically.INFO, "message1"),
            ExportMessage(Critically.WARN, "message2"),
            ExportMessage(Critically.ERROR, "message3"),
            ExportMessage(Critically.INFO, "message4")
        )

        // act
        val exportSummary = ExportSummary(backupFilePath, messages)

        // assert
        assertThat(exportSummary.backupFilePath).isEqualTo(backupFilePath)
        assertThat(exportSummary.messages).containsExactly(
            ExportMessage(Critically.INFO, "message1"),
            ExportMessage(Critically.WARN, "message2"),
            ExportMessage(Critically.ERROR, "message3"),
            ExportMessage(Critically.INFO, "message4")
        )
    }

}