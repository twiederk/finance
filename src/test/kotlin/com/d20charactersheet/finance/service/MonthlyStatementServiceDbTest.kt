package com.d20charactersheet.finance.service

import com.d20charactersheet.finance.domain.CategoryType
import com.d20charactersheet.finance.domain.MonthlyStatement
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.transaction.annotation.Transactional

@SpringBootTest
@Transactional
class MonthlyStatementServiceDbTest {

    @Autowired
    private lateinit var underTest: MonthlyStatementService

    @Test
    fun monthlyStatement_readData_convertedData() {

        // act
        val monthlyStatements = underTest.monthlyStatements(2021, 5)

        // assert
        assertThat(monthlyStatements.monthlyStatements).containsExactlyInAnyOrder(
            MonthlyStatement(1, "Bücher / Musik / Filme", -30.0, CategoryType.EXPENSE),
            MonthlyStatement(2, "Freizeit", -20.1, CategoryType.EXPENSE),
            MonthlyStatement(3, "Geschenk", -23.29, CategoryType.EXPENSE),
            MonthlyStatement(8, "Spielzeug", -73.87, CategoryType.EXPENSE),
            MonthlyStatement(30,"Klamotten",  -90.93, CategoryType.EXPENSE),
            MonthlyStatement(98,"Streaming",  4.95, CategoryType.EXPENSE),
            MonthlyStatement(100,"Gehalt Torsten",  1000.0, CategoryType.INCOME),
        )







        assertThat(monthlyStatements.year).isEqualTo(2021)
        assertThat(monthlyStatements.month).isEqualTo(5)

    }
}