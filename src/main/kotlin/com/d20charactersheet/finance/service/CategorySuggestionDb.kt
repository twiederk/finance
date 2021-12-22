package com.d20charactersheet.finance.service

import com.d20charactersheet.finance.domain.Categories
import com.d20charactersheet.finance.domain.Category
import com.d20charactersheet.finance.domain.EmptyCategory
import com.d20charactersheet.finance.domain.MoneyTransfer
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.stereotype.Component

@Component
class CategorySuggestionDb(
    @Autowired private val template: JdbcTemplate,
    @Autowired private val categories: Categories
) : CategorySuggestion {

    val categoryRules: Map<String, Int> = template.query("SELECT CategoryText, CategoryId FROM CategoryRule") { rs, _ ->
        CategoryRule(rs.getString("CategoryText"), rs.getInt("CategoryId"))
    }.associate { it.text to it.categoryId }

    override fun suggestCategory(moneyTransfers: List<MoneyTransfer>) {
        moneyTransfers.stream().forEach {
            it.category = suggestCategory(it)
        }
    }

    override fun suggestCategory(moneyTransfer: MoneyTransfer): Category {
        for (categoryRule in categoryRules) {
            if (moneyTransfer.recipient.name.startsWith(categoryRule.key)) {
                return categories.findCategoryId(categoryRule.value)
            }
        }
        return EmptyCategory
    }

}
