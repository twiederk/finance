package com.d20charactersheet.finance.service

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
internal class PaymentSuggestionDbDatabaseTest {

    @Autowired
    private lateinit var paymentSuggestionDb: PaymentSuggestionDb

    @Test
    internal fun init_initialization_loadPaymentSuggestionRulesFromDatabase() {
        // assert
        assertThat(paymentSuggestionDb.paymentInstrumentRules).hasSize(3)
    }

}