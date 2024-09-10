package com.d20charactersheet.finance.import

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.TestPropertySource

@SpringBootTest
@TestPropertySource(properties = ["transferfile.parser=ING"])
class FileParserFactoryTest {

    @Autowired
    private lateinit var fileParserFactory: FileParserFactory

    @Test
    fun should_return_IngFileParser_when_ING_is_configured() {
        // act
        val fileParser = fileParserFactory.getFileParser()

        // assert
        assertThat(fileParser).isInstanceOf(IngFileParser::class.java)
    }

}