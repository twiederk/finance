package com.d20charactersheet.finance.import

import com.d20charactersheet.finance.domain.*
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import java.time.LocalDate

class VolksbankRawMoneyTransferParserTest {

    @Test
    fun parseSalesFigure_returnParsedSalesFigures() {

        // act
        val rawMoneyTransfer =
            VolksbankRawMoneyTransferParser().parseRawMoneyTransfer("Bezeichnung Auftragskonto;IBAN Auftragskonto;BIC Auftragskonto;Bankname Auftragskonto;30.08.2024;31.08.2024;myRecipient;IBAN Zahlungsbeteiligter;BIC (SWIFT-Code) Zahlungsbeteiligter;myPostingText;myReasonForTransfer;2000,00;EUR;22456,16;;Sonstiges;;;")

        // assert
        assertThat(rawMoneyTransfer).isNotNull
        assertThat(rawMoneyTransfer.entryDate).isEqualTo(EntryDate(LocalDate.of(2024, 8, 30)))
        assertThat(rawMoneyTransfer.valutaDate).isEqualTo(ValutaDate(LocalDate.of(2024, 8, 31)))
        assertThat(rawMoneyTransfer.recipient).isEqualTo(Recipient("myRecipient"))
        assertThat(rawMoneyTransfer.postingText).isEqualTo(PostingText("myPostingText"))
        assertThat(rawMoneyTransfer.reasonForTransfer).isEqualTo(ReasonForTransfer("myReasonForTransfer"))
        assertThat(rawMoneyTransfer.amount).isEqualTo(Amount(2000F))
        assertThat(rawMoneyTransfer.currency).isEqualTo(Currency("EUR"))
    }

}