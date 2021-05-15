package com.d20charactersheet.finance.service

import com.d20charactersheet.finance.domain.*
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import java.time.LocalDate

class MoneyTransferServiceTest {

    @Test
    fun filterNewMoneyTransfers() {

        // arrange
        val rawMoneyTransfers = listOf(
            RawMoneyTransfer(
                entryDate = EntryDate(LocalDate.of(2021, 5, 11)),
                valutaDate = ValutaDate(LocalDate.of(2021, 5, 12)),
                recipient = Recipient("myRecipient"),
                postingText = PostingText("myPostingText"),
                ingCategory = IngCategory("myIngCategory"),
                reasonForTransfer = ReasonForTransfer("myReasonForTransfer"),
                hashTag = HashTag("myHashTag"),
                amount = Amount(-1.99F),
                currency = Currency("myCurrency")
            ),
            RawMoneyTransfer(
                entryDate = EntryDate(LocalDate.of(2021, 5, 11)),
                valutaDate = ValutaDate(LocalDate.of(2021, 5, 12)),
                recipient = Recipient("myRecipient"),
                postingText = PostingText("myPostingText"),
                ingCategory = IngCategory("myIngCategory"),
                reasonForTransfer = ReasonForTransfer("myReasonForTransfer"),
                hashTag = HashTag(""),
                amount = Amount(-1.99F),
                currency = Currency("myCurrency")
            )
        )

        // act
        val moneyTransfers = MoneyTransferService().filterNewMoneyTransfers(rawMoneyTransfers)

        // assert
        assertThat(moneyTransfers).hasSize(1)
    }

}