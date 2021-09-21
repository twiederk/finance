package com.d20charactersheet.finance.gui

import com.d20charactersheet.finance.domain.Amount
import java.text.DecimalFormat

class AmountFormat {
    fun format(amount: Amount): String {
        return DecimalFormat("#,##0.00 €").format(amount.toString().toFloat())
    }

}
