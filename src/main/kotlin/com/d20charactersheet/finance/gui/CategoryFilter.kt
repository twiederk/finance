package com.d20charactersheet.finance.gui

import com.d20charactersheet.finance.domain.Category

class CategoryFilter {

    fun filter(searchedText: String, categories: List<Category>): List<Category> {
        if (searchedText.isEmpty()) {
            return categories.toList()
        }
        return categories.filter { category -> category.name.lowercase().contains(searchedText.lowercase()) }.toList()
    }

}
