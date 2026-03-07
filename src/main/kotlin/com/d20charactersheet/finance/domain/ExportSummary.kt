package com.d20charactersheet.finance.domain

import java.nio.file.Path

enum class Critically {
    ERROR,
    WARN,
    INFO
}

data class ExportMessage(
    val critically: Critically,
    val message: String
)

class ExportMessageComparator : Comparator<ExportMessage> {
    override fun compare(o1: ExportMessage, o2: ExportMessage): Int {
        return o1.critically.ordinal - o2.critically.ordinal
    }
}

data class ExportSummary(
    val backupFilePath: Path,
    val messages: List<ExportMessage>
)
