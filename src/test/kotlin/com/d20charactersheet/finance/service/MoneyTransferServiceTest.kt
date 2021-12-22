package com.d20charactersheet.finance.service

import com.d20charactersheet.finance.domain.*
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.mockito.ArgumentMatchers.anyString
import org.mockito.kotlin.*
import org.springframework.jdbc.core.JdbcTemplate
import java.time.LocalDate

class MoneyTransferServiceTest {

    @Test
    fun filterNewMoneyTransfers_alreadyStoredInDatabase_dropMoneyTransfer() {

        // arrange
        val rawMoneyTransfers = listOf(
            RawMoneyTransfer(
                entryDate = EntryDate(LocalDate.of(2021, 5, 11)),
                valutaDate = ValutaDate(LocalDate.of(2021, 5, 12)),
                recipient = Recipient("myRecipient"),
                postingText = PostingText("myPostingText"),
                reasonForTransfer = ReasonForTransfer("myReasonForTransfer"),
                amount = Amount(-1.99F),
                currency = Currency("myCurrency")
            ),
        )
        val jdbcTemplate: JdbcTemplate = mock()
        whenever(jdbcTemplate.queryForObject(anyString(), eq(Int::class.java))).thenReturn(1)

        // act
        val moneyTransfers = MoneyTransferService(jdbcTemplate).filterNewMoneyTransfers(rawMoneyTransfers)

        // assert
        assertThat(moneyTransfers).hasSize(0)
    }

    @Test
    fun save_validData_saveToDatabase() {
        // arrange
        val moneyTransfer = MoneyTransfer(
            id = 0,
            valutaDate = ValutaDate(LocalDate.of(2021, 5, 12)),
            recipient = Recipient("myRecipient"),
            amount = Amount(-1.25F),
            Category(1, "myCategory"),
            Comment("myComment"),
            PaymentInstrument(1, "myPaymentInstrument")
        )
        val jdbcTemplate: JdbcTemplate = mock()
        whenever(jdbcTemplate.queryForObject(anyString(), eq(Int::class.java), anyArray<String>())).thenReturn(0)
        whenever(jdbcTemplate.update(any(), any(), any(), any(), any(), any(), any())).thenReturn(1)

        // act
        val result = MoneyTransferService(jdbcTemplate).save(moneyTransfer)

        // assert
        assertThat(result).isTrue
        verify(jdbcTemplate).update(
            "INSERT INTO umsaetze (datum, anzeigetext, betrag, kategorieId, beschreibung, quelleId) VALUES (?, ?, ?, ?, ? ,?)",
            moneyTransfer.valutaDate.date,
            moneyTransfer.recipient.name,
            moneyTransfer.amount.value,
            moneyTransfer.category.id,
            moneyTransfer.comment.text,
            moneyTransfer.paymentInstrument.id
        )
    }

    @Test
    fun save_duplicateTransaction_rejectSaving() {
        // arrange
        val moneyTransfer = MoneyTransfer(
            id = 0,
            valutaDate = ValutaDate(LocalDate.of(2021, 5, 12)),
            recipient = Recipient("myRecipient"),
            amount = Amount(-1.25F),
            Category(1, "myCategory"),
            Comment("myComment"),
            PaymentInstrument(1, "myPaymentInstrument")
        )
        val jdbcTemplate: JdbcTemplate = mock()
        whenever(jdbcTemplate.queryForObject(anyString(), eq(Int::class.java))).thenReturn(1)

        // act
        val result = MoneyTransferService(jdbcTemplate).save(moneyTransfer)

        // assert
        assertThat(result).isFalse
        verify(jdbcTemplate, never()).update(
            "INSERT INTO umsaetze (datum, anzeigetext, betrag, kategorieId, beschreibung, quelleId) VALUES (?, ?, ?, ?, ? ,?)",
            moneyTransfer.valutaDate.date,
            moneyTransfer.recipient.name,
            moneyTransfer.amount.value,
            moneyTransfer.category.id,
            moneyTransfer.comment.text,
            moneyTransfer.paymentInstrument.id
        )
    }

}