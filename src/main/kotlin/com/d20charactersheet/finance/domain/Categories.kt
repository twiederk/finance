package com.d20charactersheet.finance.domain

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.stereotype.Component

@Component
class Categories(
    @Autowired private val template: JdbcTemplate
) {

    val categories: List<Category> = template.query("SELECT Id, Kategorie FROM Kategorie") { rs, _ ->
        Category(rs.getInt("id"), rs.getString("Kategorie"))
    }.sortedBy { it.name.toUpperCase() }

    fun numberOfCategories(): Int = categories.size

}