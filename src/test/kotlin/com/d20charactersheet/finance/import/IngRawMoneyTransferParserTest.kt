package com.d20charactersheet.finance.import

import com.d20charactersheet.finance.domain.*
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import java.time.LocalDate

class IngRawMoneyTransferParserTest {

    @Test
    fun parseSalesFigure_returnParsedSalesFigures() {

        // act
        val rawMoneyTransfer =
            IngRawMoneyTransferParser().parseRawMoneyTransfer("11.05.2021;12.05.2021;myRecipient;myPostingText;myReasonForTransfer;2.000,00;EUR")

        // assert
        assertThat(rawMoneyTransfer).isNotNull
        assertThat(rawMoneyTransfer.entryDate).isEqualTo(EntryDate(LocalDate.of(2021, 5, 11)))
        assertThat(rawMoneyTransfer.valutaDate).isEqualTo(ValutaDate(LocalDate.of(2021, 5, 12)))
        assertThat(rawMoneyTransfer.recipient).isEqualTo(Recipient("myRecipient"))
        assertThat(rawMoneyTransfer.postingText).isEqualTo(PostingText("myPostingText"))
        assertThat(rawMoneyTransfer.reasonForTransfer).isEqualTo(ReasonForTransfer("myReasonForTransfer"))
        assertThat(rawMoneyTransfer.amount).isEqualTo(Amount(2000F))
        assertThat(rawMoneyTransfer.currency).isEqualTo(Currency("EUR"))
    }

}