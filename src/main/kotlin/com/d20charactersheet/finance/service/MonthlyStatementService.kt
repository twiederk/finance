package com.d20charactersheet.finance.service

import com.d20charactersheet.finance.domain.CategoryType
import com.d20charactersheet.finance.domain.MonthlyStatement
import com.d20charactersheet.finance.domain.MonthlyStatements
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.support.rowset.SqlRowSet
import org.springframework.stereotype.Service
import java.time.LocalDate
import java.time.YearMonth

@Service
class MonthlyStatementService(
    @Autowired private val jdbcTemplate: JdbcTemplate
) {

    fun monthlyStatements(year: Int, month: Int): MonthlyStatements {
        val startDate = LocalDate.of(year, month, 1)
        val endDate = YearMonth.of(year, month).atEndOfMonth()

        val sql = """SELECT
            |Kategorie.Id AS CategoryId,
            |Kategorie.Kategorie AS CategoryName,
            |Kategorie.Typ AS CategoryType,
            |Sum(Umsaetze.Betrag) AS Total
            |FROM
            |Kategorie
            |INNER JOIN Umsaetze ON Kategorie.Id = Umsaetze.KategorieId
            |WHERE Umsaetze.datum >= #${startDate}# AND Umsaetze.datum <= #${endDate}#
            |GROUP BY Kategorie.Id, Kategorie.Kategorie, Kategorie.Typ""".trimMargin()
        val result = jdbcTemplate.queryForRowSet(sql)

        val monthlyStatements = mapMonthlyStatements(result)
        return MonthlyStatements(monthlyStatements, year, month)
    }

    private fun mapMonthlyStatements(result: SqlRowSet): MutableList<MonthlyStatement> {
        val monthlyStatements = mutableListOf<MonthlyStatement>()
        while (result.next()) {
            val categoryId = result.getInt("CategoryId")
            val categoryName = result.getString("CategoryName") ?: "Missing category name"
            val total = result.getDouble("Total")
            val categoryType = convertCategoryType(result.getString("CategoryType"))
            monthlyStatements.add(MonthlyStatement(categoryId, categoryName, total, categoryType))
        }
        return monthlyStatements
    }

    private fun convertCategoryType(rawCategoryType: String?): CategoryType = when (rawCategoryType) {
        "Ausgaben" -> CategoryType.EXPENSE
        else -> CategoryType.INCOME
    }

}
