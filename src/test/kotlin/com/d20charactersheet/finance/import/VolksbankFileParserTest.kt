package com.d20charactersheet.finance.import

import com.d20charactersheet.finance.domain.*
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import java.time.LocalDate

class VolksbankFileParserTest {

    @Test
    fun importMoneyTransfers_fileWithTwoMoneyTransfers_listOfTwoMoneyTransfers() {

        // act
        val moneyTransfers =
            VolksbankFileParser().readMoneyTransfersFromFile(arrayOf("src/test/resources/volksbank_06_rows.csv"))

        // assert
        assertThat(moneyTransfers).hasSize(6)

        var rawMoneyTransfer = moneyTransfers[0]
        assertThat(rawMoneyTransfer.entryDate).isEqualTo(EntryDate(LocalDate.of(2024, 8, 30)))
        assertThat(rawMoneyTransfer.valutaDate).isEqualTo(ValutaDate(LocalDate.of(2024, 8, 31)))
        assertThat(rawMoneyTransfer.recipient).isEqualTo(Recipient(""))
        assertThat(rawMoneyTransfer.postingText).isEqualTo(PostingText("ABSCHLUSS"))
        assertThat(rawMoneyTransfer.reasonForTransfer).isEqualTo(ReasonForTransfer("Abschluss per 31.08.2024"))
        assertThat(rawMoneyTransfer.amount).isEqualTo(Amount(-13.90F))
        assertThat(rawMoneyTransfer.currency).isEqualTo(Currency("EUR"))

        rawMoneyTransfer = moneyTransfers[1]
        assertThat(rawMoneyTransfer.entryDate).isEqualTo(EntryDate(LocalDate.of(2024, 8, 30)))
        assertThat(rawMoneyTransfer.valutaDate).isEqualTo(ValutaDate(LocalDate.of(2024, 8, 30)))
        assertThat(rawMoneyTransfer.recipient).isEqualTo(Recipient("Lastschrift aus Kartenzahlung"))
        assertThat(rawMoneyTransfer.postingText).isEqualTo(PostingText("Basislastschrift"))
        assertThat(rawMoneyTransfer.reasonForTransfer).isEqualTo(ReasonForTransfer("ALLDRINK GMBH/Heidelberg/DE29.08.2024 um 12:35:28 Uhr 68052603/182831/ECTL/      67092300/0033108036/0/1224"))
        assertThat(rawMoneyTransfer.amount).isEqualTo(Amount(-20.94F))
        assertThat(rawMoneyTransfer.currency).isEqualTo(Currency("EUR"))

        rawMoneyTransfer = moneyTransfers[2]
        assertThat(rawMoneyTransfer.entryDate).isEqualTo(EntryDate(LocalDate.of(2024, 8, 30)))
        assertThat(rawMoneyTransfer.valutaDate).isEqualTo(ValutaDate(LocalDate.of(2024, 8, 30)))
        assertThat(rawMoneyTransfer.recipient).isEqualTo(Recipient("Bundeskasse - Dienstort Trier -"))
        assertThat(rawMoneyTransfer.postingText).isEqualTo(PostingText("Gehalt"))
        assertThat(rawMoneyTransfer.reasonForTransfer).isEqualTo(ReasonForTransfer("Zahltag Versorgung 09-202403141003-2024-45012908-041914 EREF: 03141003-2024-45012908-041914"))
        assertThat(rawMoneyTransfer.amount).isEqualTo(Amount(3300.58F))
        assertThat(rawMoneyTransfer.currency).isEqualTo(Currency("EUR"))
    }

}