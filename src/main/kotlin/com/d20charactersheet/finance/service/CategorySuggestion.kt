package com.d20charactersheet.finance.service

import com.d20charactersheet.finance.domain.Category
import com.d20charactersheet.finance.domain.MoneyTransfer

interface CategorySuggestion {

    fun suggestCategory(moneyTransfers: List<MoneyTransfer>)

    fun suggestCategory(moneyTransfer: MoneyTransfer): Category

}