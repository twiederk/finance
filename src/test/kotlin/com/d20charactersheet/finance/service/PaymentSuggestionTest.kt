package com.d20charactersheet.finance.service

import com.d20charactersheet.finance.domain.*
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.eq
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever
import org.springframework.jdbc.core.JdbcTemplate
import java.time.LocalDate

class PaymentSuggestionTest {

    private val paymentInstruments: PaymentInstruments = mock()

    private lateinit var paymentSuggestionDb: PaymentSuggestionDb

    companion object {
        const val PAYMENT_INSTRUMENT_ID_PAYPAL = 1
        const val PAYMENT_INSTRUMENT_ID_VISA = 3
        const val PAYMENT_INSTRUMENT_ID_D20CS = 10
    }

    @BeforeEach
    fun beforeEach() {

        val jdbcTemplate = mock<JdbcTemplate>()
        whenever(
            jdbcTemplate.query(
                eq("SELECT PaymentInstrumentText, PaymentInstrumentId FROM PaymentInstrumentRule"),
                any<PaymentInstrumentRuleRowMapper>()
            )
        ).thenReturn(
            mutableListOf(
                PaymentInstrumentRule("PayPal", PAYMENT_INSTRUMENT_ID_PAYPAL),
                PaymentInstrumentRule("VISA", PAYMENT_INSTRUMENT_ID_VISA),
                PaymentInstrumentRule("d20cs", PAYMENT_INSTRUMENT_ID_D20CS),
            )
        )

        whenever(paymentInstruments.findPaymentInstrumentById(any()))
            .thenAnswer { i -> PaymentInstrument(i.arguments[0] as Int, "myPaymentInstrument") }

        paymentSuggestionDb = PaymentSuggestionDb(jdbcTemplate)

    }

    @Test
    fun suggestPaymentInstrument_basedOnRecipientPayPal_suggestPayPal() {
        // arrange
        val paypalMoneyTransfer = MoneyTransfer(
            id = 0,
            valutaDate = ValutaDate(LocalDate.of(2021, 5, 12)),
            recipient = Recipient("PayPal myRecipient"),
            amount = Amount(-1.25F),
            reasonForTransfer = ReasonForTransfer("myReasonForTransfer"),
            category = EmptyCategory,
            comment = Comment("myComment"),
            paymentInstrument = EmptyPaymentInstrument
        )

        // act
        paymentSuggestionDb.suggestPaymentInstrument(listOf(paypalMoneyTransfer), paymentInstruments)

        // assert
        assertThat(paypalMoneyTransfer.paymentInstrument.id).isEqualTo(PAYMENT_INSTRUMENT_ID_PAYPAL)
    }

    @Test
    fun suggestPaymentInstrument_basedOnRecipientGiroCard_suggestGirocard() {
        // arrange
        val giroMoneyTransfer = MoneyTransfer(
            id = 0,
            valutaDate = ValutaDate(LocalDate.of(2021, 5, 12)),
            recipient = Recipient("myRecipient"),
            amount = Amount(-1.25F),
            reasonForTransfer = ReasonForTransfer("myReasonForTransfer"),
            category = EmptyCategory,
            comment = Comment("myComment"),
            paymentInstrument = EmptyPaymentInstrument
        )

        // act
        paymentSuggestionDb.suggestPaymentInstrument(listOf(giroMoneyTransfer), paymentInstruments)

        // assert
        assertThat(giroMoneyTransfer.paymentInstrument.id).isEqualTo(PaymentSuggestionDb.DEFAULT_PAYMENT_INSTRUMENT_ID)
    }

    @Test
    fun suggestPaymentInstrument_basedOnRecipientVisaCard_suggestVisacard() {
        // arrange
        val visaMoneyTransfer = MoneyTransfer(
            id = 0,
            valutaDate = ValutaDate(LocalDate.of(2021, 5, 12)),
            recipient = Recipient("VISA myRecipient"),
            amount = Amount(-1.25F),
            reasonForTransfer = ReasonForTransfer("myReasonForTransfer"),
            category = EmptyCategory,
            comment = Comment("myComment"),
            paymentInstrument = EmptyPaymentInstrument
        )

        val paymentInstruments: PaymentInstruments = mock()
        whenever(paymentInstruments.findPaymentInstrumentById(any()))
            .thenAnswer { i -> PaymentInstrument(i.arguments[0] as Int, "myPaymentInstrument") }

        // act
        paymentSuggestionDb.suggestPaymentInstrument(listOf(visaMoneyTransfer), paymentInstruments)

        // assert
        assertThat(visaMoneyTransfer.paymentInstrument.id).isEqualTo(PAYMENT_INSTRUMENT_ID_VISA)
    }

    @Test
    fun suggestPaymentInstrument_basedOnCategoryD20cs_suggestD20cs() {
        // arrange
        val d20csMoneyTransfer = MoneyTransfer(
            id = 0,
            valutaDate = ValutaDate(LocalDate.of(2021, 5, 12)),
            recipient = Recipient("myRecipient"),
            amount = Amount(-1.25F),
            reasonForTransfer = ReasonForTransfer("myReasonForTransfer"),
            category = Category(44, "d20cs"),
            comment = Comment("myComment"),
            paymentInstrument = EmptyPaymentInstrument
        )

        val paymentInstruments: PaymentInstruments = mock()
        whenever(paymentInstruments.findPaymentInstrumentById(any()))
            .thenAnswer { i -> PaymentInstrument(i.arguments[0] as Int, "myPaymentInstrument") }

        // act
        paymentSuggestionDb.suggestPaymentInstrument(listOf(d20csMoneyTransfer), paymentInstruments)

        // assert
        assertThat(d20csMoneyTransfer.paymentInstrument.id).isEqualTo(PAYMENT_INSTRUMENT_ID_D20CS)
    }

}