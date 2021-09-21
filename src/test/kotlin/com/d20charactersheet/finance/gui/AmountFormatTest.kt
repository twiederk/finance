package com.d20charactersheet.finance.gui

import com.d20charactersheet.finance.domain.Amount
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class AmountFormatTest {

    @Test
    fun format_amountWithThreeDecimals_displayAmountWithTwoDecimals() {

        // act
        val amount = AmountFormat().format(Amount(9.876F))

        // assert
        assertThat(amount).isEqualTo("9,88 €")
    }

    @Test
    fun format_amountWithTwoDecimals_displayAmountWithTwoDecimals() {

        // act
        val amount = AmountFormat().format(Amount(8.12F))

        // assert
        assertThat(amount).isEqualTo("8,12 €")
    }

    @Test
    fun format_amountWithOneDecimal_displayAmountWithTwoDecimals() {

        // act
        val amount = AmountFormat().format(Amount(6.5F))

        // assert
        assertThat(amount).isEqualTo("6,50 €")
    }

    @Test
    fun format_amountWithZeroDecimals_displayAmountWithTwoDecimals() {

        // act
        val amount = AmountFormat().format(Amount(1F))

        // assert
        assertThat(amount).isEqualTo("1,00 €")
    }

    @Test
    fun format_amountWithOnlyDecimals_displayAmountWithTwoDecimals() {

        // act
        val amount = AmountFormat().format(Amount(0.36F))

        // assert
        assertThat(amount).isEqualTo("0,36 €")
    }

    @Test
    fun format_amountOfExactThousand_displayAmountWithTwoDecimals() {

        // act
        val amount = AmountFormat().format(Amount(2000.0F))

        // assert
        assertThat(amount).isEqualTo("2.000,00 €")
    }

}