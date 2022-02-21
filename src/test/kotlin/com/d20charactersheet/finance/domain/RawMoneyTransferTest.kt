package com.d20charactersheet.finance.domain

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import java.time.LocalDate

class RawMoneyTransferTest {

    @Test
    fun toMoneyTransfer_returnMoneyTransfer() {
        // arrange
        val rawMoneyTransfer = RawMoneyTransfer(
            entryDate = EntryDate(LocalDate.of(2021, 5, 11)),
            valutaDate = ValutaDate(LocalDate.of(2021, 5, 12)),
            recipient = Recipient("myRecipient"),
            postingText = PostingText("myPostingText"),
            reasonForTransfer = ReasonForTransfer("myReasonForTransfer"),
            amount = Amount(-1.99F),
            currency = Currency("myCurrency")
        )
        // act
        val moneyTransfer = rawMoneyTransfer.toMoneyTransfer()

        // assert
        assertThat(moneyTransfer.id).isEqualTo(0)
        assertThat(moneyTransfer.valutaDate).isEqualTo(ValutaDate(LocalDate.of(2021, 5, 12)))
        assertThat(moneyTransfer.recipient).isEqualTo(Recipient("myRecipient"))
        assertThat(moneyTransfer.reasonForTransfer).isEqualTo(ReasonForTransfer("myReasonForTransfer"))
        assertThat(moneyTransfer.amount).isEqualTo(Amount(-1.99F))
        assertThat(moneyTransfer.category).isEqualTo(EmptyCategory)
        assertThat(moneyTransfer.comment).isEqualTo(Comment(""))
        assertThat(moneyTransfer.paymentInstrument).isEqualTo(EmptyPaymentInstrument)
    }

}