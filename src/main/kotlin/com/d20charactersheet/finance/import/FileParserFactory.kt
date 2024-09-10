package com.d20charactersheet.finance.import

import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component

@Component
class FileParserFactory {

    @Value("\${transferfile.parser}")
    private lateinit var parserType: String

    fun getFileParser(): FileParser {
        return when (parserType) {
            "ING" -> IngFileParser()
            "Volksbank" -> VolksbankFileParser()
            else -> throw IllegalArgumentException("Unsupported parser type: $parserType")
        }
    }
}
