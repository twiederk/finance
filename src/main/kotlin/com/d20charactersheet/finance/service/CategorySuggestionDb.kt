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
    @Autowired private val jdbcTemplate: JdbcTemplate,
    @Autowired private val categories: Categories
) : CategorySuggestion {

    private final var categoryRules: Map<String, Int> = mutableMapOf()

    init {
        val categoryRulesList: MutableList<CategoryRule> =
            jdbcTemplate.query("SELECT CategoryText, CategoryId FROM CategoryRule", CategoryRuleRowMapper())
        categoryRules = categoryRulesList.associate { it.text to it.categoryId }
    }

    override fun suggestCategory(moneyTransfers: List<MoneyTransfer>) {
        moneyTransfers.stream().forEach {
            it.category = suggestCategory(it)
        }
    }

    override fun suggestCategory(moneyTransfer: MoneyTransfer): Category {
        for (categoryRule in categoryRules) {
            if (moneyTransfer.recipient.name.startsWith(categoryRule.key)
                || moneyTransfer.reasonForTransfer.text.startsWith(categoryRule.key)
            ) {
                return categories.findCategoryId(categoryRule.value)
            }
        }
        return EmptyCategory
    }

}
