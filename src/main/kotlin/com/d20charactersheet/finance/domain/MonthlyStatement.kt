package com.d20charactersheet.finance.domain

enum class CategoryType { EXPENSE, INCOME }

data class MonthlyStatement(
    val categoryId: Int,
    val categoryName: String,
    val total: Double,
    val categoryType: CategoryType
)