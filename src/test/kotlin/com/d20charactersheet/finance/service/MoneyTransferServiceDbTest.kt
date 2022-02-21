package com.d20charactersheet.finance.service

import com.d20charactersheet.finance.domain.*
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDate

@SpringBootTest
@Transactional
class MoneyTransferServiceDbTest {

    @Autowired
    private lateinit var underTest: MoneyTransferService

    @Test
    fun save_existingTransaction_rejectTransaction() {
        // arrange
        val moneyTransfer = MoneyTransfer(
            valutaDate = ValutaDate(LocalDate.of(2021, 5, 30)),
            recipient = Recipient("testTransaction"),
            reasonForTransfer = ReasonForTransfer("testReasonForTransfer"),
            amount = Amount(100F),
            category = Category(1, "testCategory"),
            comment = Comment("testComment"),
            paymentInstrument = PaymentInstrument(1, "testPayment")
        )
        underTest.save(moneyTransfer)

        // act
        val savedTransfer = underTest.save(moneyTransfer)

        // assert
        assertThat(savedTransfer).isFalse
    }

}