package com.d20charactersheet.finance.service

import com.d20charactersheet.finance.domain.*
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import org.springframework.jdbc.core.JdbcTemplate
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
        val moneyTransfers = MoneyTransferService(mock()).filterNewMoneyTransfers(rawMoneyTransfers)

        // assert
        assertThat(moneyTransfers).hasSize(1)
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

        // act
        MoneyTransferService(jdbcTemplate).save(moneyTransfer)

        // assert
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
    fun suggestPaymentInstrument() {
        // arrange
        val giroMoneyTransfer = MoneyTransfer(
            id = 0,
            valutaDate = ValutaDate(LocalDate.of(2021, 5, 12)),
            recipient = Recipient("myRecipient"),
            amount = Amount(-1.25F),
            EmptyCategory,
            Comment("myComment"),
            EmptyPaymentInstrument
        )
        val visaMoneyTransfer = MoneyTransfer(
            id = 0,
            valutaDate = ValutaDate(LocalDate.of(2021, 5, 12)),
            recipient = Recipient("VISA myRecipient"),
            amount = Amount(-1.25F),
            EmptyCategory,
            Comment("myComment"),
            EmptyPaymentInstrument
        )
        val paypalMoneyTransfer = MoneyTransfer(
            id = 0,
            valutaDate = ValutaDate(LocalDate.of(2021, 5, 12)),
            recipient = Recipient("PayPal myRecipient"),
            amount = Amount(-1.25F),
            EmptyCategory,
            Comment("myComment"),
            EmptyPaymentInstrument
        )
        val moneyTransfers = listOf(giroMoneyTransfer, visaMoneyTransfer, paypalMoneyTransfer)

        val paymentInstruments: PaymentInstruments = mock()
        whenever(paymentInstruments.findPaymentInstrumentById(any()))
            .thenAnswer { i -> PaymentInstrument(i.arguments[0] as Int, "myPaymentInstrument") }

        // act
        MoneyTransferService(mock()).suggestPaymentInstrument(moneyTransfers, paymentInstruments)

        // assert
        assertThat(paypalMoneyTransfer.paymentInstrument.id).isEqualTo(1)
        assertThat(visaMoneyTransfer.paymentInstrument.id).isEqualTo(3)
        assertThat(giroMoneyTransfer.paymentInstrument.id).isEqualTo(4)
    }

    @Test
    fun suggestCategory() {

        // arrange
        val amazon = createMoneyTransfer("VISA AMAZON.DE AMAZON.DE")
        val bauerMarkt = createMoneyTransfer("VISA BAUER-MARKT GMBH")
        val bauerMarktDanke = createMoneyTransfer("IHR BAUER-MARKT SAGT DANKE")
        val backhausBickert = createMoneyTransfer("VISA BACKHAUS BICKERT GMBH")
        val brotmacher = createMoneyTransfer("DER BROTMACHER GMBH")
        val stromLichtblick = createMoneyTransfer("LichtBlick SE")
        val stromEon = createMoneyTransfer("E.ON Energie Deutsch")
        val rewe = createMoneyTransfer("VISA REWE OEVUENC BEKAR OHG")
        val getraenkemarktOstheimer = createMoneyTransfer("Getraenkemarkt Ostheimer GmbH")
        val telekom = createMoneyTransfer("Telekom Deutschland GmbH")
        val o2 = createMoneyTransfer("Telefonica Germany GmbH + Co. OHG")
        val kindergeld = createMoneyTransfer("Bundesagentur fur Arbeit - Familienkasse")
        val aws = createMoneyTransfer("VISA AWS EMEA")
        val wuerttembergischVersicherung = createMoneyTransfer("Wuerttembergische Versicherung AG")
        val bayernLebensversicherung = createMoneyTransfer("BAYERN-VERSICHERUNG LEBENSVERSICHERUNG AG")

        val moneyTransfers = listOf(
            amazon,
            bauerMarkt,
            bauerMarktDanke,
            backhausBickert,
            brotmacher,
            stromLichtblick,
            stromEon,
            rewe,
            getraenkemarktOstheimer,
            telekom,
            o2,
            kindergeld,
            aws,
            wuerttembergischVersicherung,
            bayernLebensversicherung
        )

        val categories: Categories = mock()
        whenever(categories.findCategoryId(any()))
            .thenAnswer { i -> Category(i.arguments[0] as Int, "myCategory") }

        // act
        MoneyTransferService(mock()).suggestCategory(moneyTransfers, categories)

        // assert
        assertThat(amazon.category).isEqualTo(EmptyCategory)
        assertThat(bauerMarkt.category.id).isEqualTo(19)
        assertThat(bauerMarktDanke.category.id).isEqualTo(19)
        assertThat(backhausBickert.category.id).isEqualTo(9)
        assertThat(brotmacher.category.id).isEqualTo(9)
        assertThat(stromLichtblick.category.id).isEqualTo(50)
        assertThat(stromEon.category.id).isEqualTo(46)
        assertThat(rewe.category.id).isEqualTo(19)
        assertThat(getraenkemarktOstheimer.category.id).isEqualTo(19)
        assertThat(telekom.category.id).isEqualTo(52)
        assertThat(o2.category.id).isEqualTo(40)
        assertThat(kindergeld.category.id).isEqualTo(67)
        assertThat(aws.category.id).isEqualTo(22)
        assertThat(wuerttembergischVersicherung.category.id).isEqualTo(37)
        assertThat(bayernLebensversicherung.category.id).isEqualTo(65)
    }

    private fun createMoneyTransfer(recipient: String) = MoneyTransfer(
        id = 0,
        valutaDate = ValutaDate(LocalDate.of(2021, 5, 12)),
        recipient = Recipient(recipient),
        amount = Amount(-1.25F),
        EmptyCategory,
        Comment("myComment"),
        EmptyPaymentInstrument
    )

}