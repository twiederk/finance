package com.d20charactersheet.finance.import

import com.d20charactersheet.finance.domain.Amount
import com.d20charactersheet.finance.domain.EntryDate
import com.d20charactersheet.finance.domain.ReasonForTransfer
import com.d20charactersheet.finance.domain.Recipient
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.catchThrowable
import org.junit.jupiter.api.Test
import java.time.LocalDate

class FileParserTest {

    @Test
    fun importMoneyTransfers_fileWithTwoMoneyTransfers_listOfTwoMoneyTransfers() {

        // act
        val moneyTransfers =
            FileParser().readMoneyTransfersFromFile(arrayOf("src/test/resources/Umsatzanzeige_02_rows.csv"))

        // assert
        assertThat(moneyTransfers).hasSize(2)

        var rawMoneyTransfer = moneyTransfers[0]
        assertThat(rawMoneyTransfer.entryDate).isEqualTo(EntryDate(LocalDate.of(2021, 5, 11)))
        assertThat(rawMoneyTransfer.recipient).isEqualTo(Recipient("VISA ROSSMANN 3216"))
        assertThat(rawMoneyTransfer.reasonForTransfer).isEqualTo(ReasonForTransfer("NR XXXX 0013 GROSSWALLST KAUFUMSATZ 08.05 124138 ARN74830721128319018498371 Google Pay"))
        assertThat(rawMoneyTransfer.amount).isEqualTo(Amount(-13.99F))

        rawMoneyTransfer = moneyTransfers[1]
        assertThat(rawMoneyTransfer.entryDate).isEqualTo(EntryDate(LocalDate.of(2021, 5, 12)))
        assertThat(rawMoneyTransfer.recipient).isEqualTo(Recipient("VISA GOOGLE *GOOGLE PLAY AP"))
        assertThat(rawMoneyTransfer.reasonForTransfer).isEqualTo(ReasonForTransfer("NR XXXX 0013 G.CO/HELPPA IE KAUFUMSATZ 08.05 103439 ARN74313301128100074005962"))
        assertThat(rawMoneyTransfer.amount).isEqualTo(Amount(-2.99F))

    }

    @Test
    fun importMoneyTransfers_wrongFile_throwIllegalArgumentException() {

        // act
        val throwable =
            catchThrowable { FileParser().readMoneyTransfersFromFile(arrayOf("src/test/resources/wrongFile.csv")) }

        // assert
        assertThat(throwable).isInstanceOf(IllegalArgumentException::class.java)
            .hasMessage("Can't find money transfers in file")
    }

    @Test
    fun readMoneyTransfersFromFile_missingFilename_throwIllegalArgumentException() {
        // arrange

        // act
        val moneyTransfers = FileParser().readMoneyTransfersFromFile(arrayOf())

        // assert
        assertThat(moneyTransfers).isEmpty()
    }
}