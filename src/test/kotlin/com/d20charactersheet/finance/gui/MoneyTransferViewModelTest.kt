package com.d20charactersheet.finance.gui

import com.d20charactersheet.finance.domain.*
import com.d20charactersheet.finance.service.MoneyTransferService
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.mockito.kotlin.argumentCaptor
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import java.time.LocalDate


class MoneyTransferViewModelTest {

    @Test
    fun onCommit_dataIsValid_saveInDatabase() {
        // arrange
        val moneyTransfer = MoneyTransfer(
            id = 0,
            valutaDate = ValutaDate(LocalDate.of(2021, 5, 12)),
            recipient = Recipient("myRecipient"),
            amount = Amount(-1.25F),
            reasonForTransfer = ReasonForTransfer("myReasonForTransfer"),
            category = EmptyCategory,
            comment = Comment(""),
            paymentInstrument = EmptyPaymentInstrument
        )
        val moneyTransferService: MoneyTransferService = mock()

        val underTest = MoneyTransferViewModel(moneyTransfer, moneyTransferService)
        underTest.onCategoryChange(Category(1, "myCategory"))
        underTest.onPaymentInstrumentChange(PaymentInstrument(1, "myPaymentInstrument"))
        underTest.comment.value = "myComment"

        // act
        underTest.onCommit()

        // assert
        val captor = argumentCaptor<MoneyTransfer>()
        verify(moneyTransferService).save(captor.capture())
        assertThat(captor.firstValue.paymentInstrument).isEqualTo(PaymentInstrument(1, "myPaymentInstrument"))
        assertThat(captor.firstValue.comment).isEqualTo(Comment("myComment"))
        assertThat(captor.firstValue.category).isEqualTo(Category(1, "myCategory"))
    }

}