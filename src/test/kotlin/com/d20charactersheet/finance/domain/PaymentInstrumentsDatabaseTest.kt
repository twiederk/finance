package com.d20charactersheet.finance.domain

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class PaymentInstrumentsDatabaseTest {

    @Autowired
    private lateinit var underTest: PaymentInstruments

    @Test
    fun numberOfCategories_returnTotalNumberOfCategories() {
        // act
        val numberOfPaymentInstruments = underTest.numberOfPaymentInstruments()

        // assert
        assertThat(numberOfPaymentInstruments).isEqualTo(3)
    }

    @Test
    fun getPaymentInstruments_alphabeticallyOrderedListOfAllCategories() {

        // act
        val paymentInstruments = underTest.paymentInstruments

        // assert
        assertThat(paymentInstruments.map { it.name }).containsExactly(
            "d20cs Konto",
            "Geschäftskonto",
            "Nebenkostenkonto",
        )
    }

}