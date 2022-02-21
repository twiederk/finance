package com.d20charactersheet.finance.service

import com.d20charactersheet.finance.domain.*
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.eq
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever
import org.springframework.jdbc.core.JdbcTemplate
import java.time.LocalDate

class CategorySuggestionTest {

    private lateinit var categorySuggestionDb: CategorySuggestionDb

    @BeforeEach
    fun before() {
        val jdbcTemplate = mock<JdbcTemplate>()
        whenever(
            jdbcTemplate.query(
                eq("SELECT CategoryText, CategoryId FROM CategoryRule"),
                any<CategoryRuleRowMapper>()
            )
        ).thenReturn(
            mutableListOf(CategoryRule("Supermarket", 99))
        )

        val categories: Categories = mock()
        whenever(categories.findCategoryId(any()))
            .thenAnswer { i -> Category(i.arguments[0] as Int, "myCategory") }

        categorySuggestionDb = CategorySuggestionDb(jdbcTemplate, categories)
    }

    @Test
    fun suggestCategory_basedOnRecipient_returnSuggestedCategory() {
        // arrange
        val moneyTransfer = createMoneyTransfer("Supermarket", "")


        // act
        val suggestedCategory = categorySuggestionDb.suggestCategory(moneyTransfer)

        // assert
        assertThat(suggestedCategory.id).isEqualTo(99)
        assertThat(suggestedCategory.name).isEqualTo("myCategory")
    }

    @Test
    fun suggestCategory_basedOnReasonForTransfer_returnSuggestedCategory() {
        // arrange
        val moneyTransfer = createMoneyTransfer("", "Supermarket")

        // act
        val suggestedCategory = categorySuggestionDb.suggestCategory(moneyTransfer)

        // assert
        assertThat(suggestedCategory.id).isEqualTo(99)
        assertThat(suggestedCategory.name).isEqualTo("myCategory")
    }

    private fun createMoneyTransfer(
        recipient: String,
        reasonForTransfer: String
    ) = MoneyTransfer(
        id = 0,
        valutaDate = ValutaDate(LocalDate.of(2021, 5, 12)),
        recipient = Recipient(recipient),
        reasonForTransfer = ReasonForTransfer(reasonForTransfer),
        amount = Amount(-1.25F),
        EmptyCategory,
        Comment("myComment"),
        EmptyPaymentInstrument
    )

}