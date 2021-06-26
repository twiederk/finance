package com.d20charactersheet.finance.gui

import androidx.compose.ui.graphics.Color
import com.d20charactersheet.finance.domain.Category
import com.d20charactersheet.finance.domain.EmptyCategory
import com.d20charactersheet.finance.domain.EmptyPaymentInstrument
import com.d20charactersheet.finance.domain.PaymentInstrument
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class DataColorSelectorTest {

    @Test
    fun getPaymentInstrumentColor_validPaymentCategory_returnValidColor() {

        // act
        val color = DataColorSelector().getPaymentInstrumentColor(PaymentInstrument(1, "myCategory"))

        // assert
        assertThat(color).isEqualTo(Color.Green)
    }

    @Test
    fun getPaymentInstrumentColor_emptyPaymentCategory_returnWarningColor() {

        // act
        val color = DataColorSelector().getPaymentInstrumentColor(EmptyPaymentInstrument)

        // assert
        assertThat(color).isEqualTo(Color.Yellow)
    }

    @Test
    fun getCategoryColor_validCategory_returnValidColor() {

        // act
        val color = DataColorSelector().getCategoryColor(Category(1, "myCategory"))

        // assert
        assertThat(color).isEqualTo(Color.Green)
    }

    @Test
    fun getCategoryColor_emptyCategory_returnWarningColor() {

        // act
        val color = DataColorSelector().getCategoryColor(EmptyCategory)

        // assert
        assertThat(color).isEqualTo(Color.Yellow)
    }

}