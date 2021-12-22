package com.d20charactersheet.finance.service

import com.d20charactersheet.finance.domain.*
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import java.time.LocalDate

@SpringBootTest
class CategorySuggestionDbDatabaseTest {

    @Autowired
    private lateinit var categorySuggestionDb: CategorySuggestionDb

    @Test
    internal fun suggestCategory_oneCategory_returnSuggestedCategory() {
        // arrange
        val bauerMarkt = createMoneyTransfer("VISA BAUER-MARKT GMBH")
        val categories: Categories = mock()
        whenever(categories.findCategoryId(any()))
            .thenAnswer { i -> Category(i.arguments[0] as Int, "myCategory") }

        // act
        val suggestedCategory = categorySuggestionDb.suggestCategory(bauerMarkt)

        // assert
        assertThat(suggestedCategory.id).isEqualTo(99)
    }

    @Test
    fun suggestCategory_listOfMoneyTransfers_fillWithSuggestedCategory() {

        // arrange
        val amazon = createMoneyTransfer("VISA AMAZON.DE")
        val streaming = createMoneyTransfer("VISA PRIME VIDEO")
        val bauerMarkt = createMoneyTransfer("VISA BAUER-MARKT GMBH")

        val moneyTransfers = listOf(amazon, streaming, bauerMarkt)

        val categories: Categories = mock()
        whenever(categories.findCategoryId(any()))
            .thenAnswer { i -> Category(i.arguments[0] as Int, "myCategory") }

        // act
        categorySuggestionDb.suggestCategory(moneyTransfers)

        // assert
        assertThat(amazon.category).isEqualTo(EmptyCategory)
        assertThat(streaming.category.id).isEqualTo(98)
        assertThat(bauerMarkt.category.id).isEqualTo(99)
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