package com.d20charactersheet.finance.domain

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import java.time.LocalDate

class RawMoneyTransferTest {

    @Test
    fun toMoneyTransfer_returnMoneyTransfer() {
        // arrange

        // act
        val moneyTransfer = RawMoneyTransfer(
            entryDate = EntryDate(LocalDate.of(2021, 5, 11)),
            valutaDate = ValutaDate(LocalDate.of(2021, 5, 12)),
            recipient = Recipient("myRecipient"),
            postingText = PostingText("myPostingText"),
            ingCategory = IngCategory("myIngCategory"),
            reasonForTransfer = ReasonForTransfer("myReasonForTransfer"),
            hashTag = HashTag("myHashTag"),
            amount = Amount(-1.99F),
            currency = Currency("myCurrency")
        ).toMoneyTransfer()

        // assert
        assertThat(moneyTransfer.id).isEqualTo(0)
        assertThat(moneyTransfer.valutaDate).isEqualTo(ValutaDate(LocalDate.of(2021, 5, 12)))
        assertThat(moneyTransfer.recipient).isEqualTo(Recipient("myRecipient"))
        assertThat(moneyTransfer.amount).isEqualTo(Amount(-1.99F))
        assertThat(moneyTransfer.category).isEqualTo(EmptyCategory)
        assertThat(moneyTransfer.comment).isEqualTo(Comment(""))
        assertThat(moneyTransfer.paymentInstrument).isEqualTo(EmptyPaymentInstrument)
    }

}