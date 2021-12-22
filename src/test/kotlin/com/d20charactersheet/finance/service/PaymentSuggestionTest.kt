package com.d20charactersheet.finance.service

import com.d20charactersheet.finance.domain.*
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever
import java.time.LocalDate

class PaymentSuggestionTest {

    private val paymentInstruments: PaymentInstruments = mock()

    @BeforeEach
    fun beforeEach() {
        whenever(paymentInstruments.findPaymentInstrumentById(any()))
            .thenAnswer { i -> PaymentInstrument(i.arguments[0] as Int, "myPaymentInstrument") }
    }

    @Test
    fun suggestPaymentInstrument_payPal_suggestPayPal() {
        // arrange
        val paypalMoneyTransfer = MoneyTransfer(
            id = 0,
            valutaDate = ValutaDate(LocalDate.of(2021, 5, 12)),
            recipient = Recipient("PayPal myRecipient"),
            amount = Amount(-1.25F),
            EmptyCategory,
            Comment("myComment"),
            EmptyPaymentInstrument
        )

        // act
        PaymentSuggestion().suggestPaymentInstrument(listOf(paypalMoneyTransfer), paymentInstruments)

        // assert
        assertThat(paypalMoneyTransfer.paymentInstrument.id).isEqualTo(1)
    }

    @Test
    fun suggestPaymentInstrument_giroCard_suggestGirocard() {
        // arrange
        val giroMoneyTransfer = MoneyTransfer(
            id = 0,
            valutaDate = ValutaDate(LocalDate.of(2021, 5, 12)),
            recipient = Recipient("myRecipient"),
            amount = Amount(-1.25F),
            EmptyCategory,
            Comment("myComment"),
            EmptyPaymentInstrument
        )

        // act
        PaymentSuggestion().suggestPaymentInstrument(listOf(giroMoneyTransfer), paymentInstruments)

        // assert
        assertThat(giroMoneyTransfer.paymentInstrument.id).isEqualTo(4)
    }

    @Test
    fun suggestPaymentInstrument_visaCard_suggestVisacard() {
        // arrange
        val visaMoneyTransfer = MoneyTransfer(
            id = 0,
            valutaDate = ValutaDate(LocalDate.of(2021, 5, 12)),
            recipient = Recipient("VISA myRecipient"),
            amount = Amount(-1.25F),
            EmptyCategory,
            Comment("myComment"),
            EmptyPaymentInstrument
        )

        val paymentInstruments: PaymentInstruments = mock()
        whenever(paymentInstruments.findPaymentInstrumentById(any()))
            .thenAnswer { i -> PaymentInstrument(i.arguments[0] as Int, "myPaymentInstrument") }

        // act
        PaymentSuggestion().suggestPaymentInstrument(listOf(visaMoneyTransfer), paymentInstruments)

        // assert
        assertThat(visaMoneyTransfer.paymentInstrument.id).isEqualTo(3)
    }

}