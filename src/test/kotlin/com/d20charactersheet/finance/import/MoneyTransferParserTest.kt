package com.d20charactersheet.finance.import

import com.d20charactersheet.finance.domain.*
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import java.time.LocalDate

class MoneyTransferParserTest {

    @Test
    fun parseSalesFigure_returnParsedSalesFigures() {

        // act
        val moneyTransfer =
            MoneyTransferParser().parseMoneyTransfer("11.05.2021;12.05.2021;myRecipient;myPostingText;myIngCategory;myHashTag;myReasonForTransfer;-13,99;EUR")

        // assert
        Assertions.assertThat(moneyTransfer).isNotNull
        Assertions.assertThat(moneyTransfer.entryDate).isEqualTo(EntryDate(LocalDate.of(2021, 5, 11)))
        Assertions.assertThat(moneyTransfer.valutaDate).isEqualTo(ValutaDate(LocalDate.of(2021, 5, 12)))
        Assertions.assertThat(moneyTransfer.recipient).isEqualTo(Recipient("myRecipient"))
        Assertions.assertThat(moneyTransfer.postingText).isEqualTo(PostingText("myPostingText"))
        Assertions.assertThat(moneyTransfer.ingCategory).isEqualTo(IngCategory("myIngCategory"))
        Assertions.assertThat(moneyTransfer.hashTag).isEqualTo(HashTag("myHashTag"))
        Assertions.assertThat(moneyTransfer.reasonForTransfer).isEqualTo(ReasonForTransfer("myReasonForTransfer"))
        Assertions.assertThat(moneyTransfer.amount).isEqualTo(Amount(-13.99F))
        Assertions.assertThat(moneyTransfer.currency).isEqualTo(Currency("EUR"))

    }

}