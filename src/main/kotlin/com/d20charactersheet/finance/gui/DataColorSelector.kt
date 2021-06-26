package com.d20charactersheet.finance.gui

import androidx.compose.ui.graphics.Color
import com.d20charactersheet.finance.domain.Category
import com.d20charactersheet.finance.domain.EmptyCategory
import com.d20charactersheet.finance.domain.EmptyPaymentInstrument
import com.d20charactersheet.finance.domain.PaymentInstrument

class DataColorSelector {

    fun getCategoryColor(category: Category): Color {
        if (category == EmptyCategory) return Color.Yellow
        return Color.Green
    }

    fun getPaymentInstrumentColor(paymentInstrument: PaymentInstrument): Color {
        if (paymentInstrument == EmptyPaymentInstrument) return Color.Yellow
        return Color.Green
    }


}