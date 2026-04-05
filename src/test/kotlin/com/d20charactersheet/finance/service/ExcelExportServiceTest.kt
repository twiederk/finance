package com.d20charactersheet.finance.service

import com.d20charactersheet.finance.domain.CategoryType
import com.d20charactersheet.finance.domain.Critically
import com.d20charactersheet.finance.domain.ExportMessage
import com.d20charactersheet.finance.domain.MonthlyStatement
import com.d20charactersheet.finance.domain.MonthlyStatements
import org.apache.poi.xssf.usermodel.XSSFWorkbook
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.Test
import java.nio.file.Files
import java.nio.file.Path


class ExcelExportServiceTest {

    private val testDirectory = Path.of(System.getProperty("user.dir"), "src/test/resources/").toString()

    private fun createExcelExportService() = ExcelExportService(testDirectory, "KostenrechnungTest.xlsx")

    @Test
    fun backup() {
        // arrange
        val directory = Path.of(testDirectory)
        val filename = "KostenrechnungTest.xlsx"

        // act
        val backupFilePath = createExcelExportService().backup(directory, filename)

        // assert
        assertThat(backupFilePath).exists()

        // tear down
        backupFilePath.toFile().delete()
    }

    @Test
    fun export() {
        // arrange
        val directory = Path.of(testDirectory)
        val originalFilename = "KostenrechnungTest.xlsx"
        val exportFilename = "KostenrechnungTest_export.xlsx"

        Files.copy(directory.resolve(originalFilename), directory.resolve(exportFilename))

        val statements = listOf(
            MonthlyStatement(1, "Bücher / Musik / Filme", -30.0, CategoryType.EXPENSE),
            MonthlyStatement(2, "Freizeit", -20.1, CategoryType.EXPENSE),
            MonthlyStatement(3, "Geschenk", -23.29, CategoryType.EXPENSE),
            MonthlyStatement(8, "Spielzeug", -73.87, CategoryType.EXPENSE),
            MonthlyStatement(30, "Klamotten", 90.93, CategoryType.EXPENSE),
            MonthlyStatement(98, "Streaming", -4.95, CategoryType.EXPENSE),
            MonthlyStatement(99, "Lebensmittel", -612.78, CategoryType.EXPENSE),
            MonthlyStatement(100, "Gehalt Torsten", 1000.00, CategoryType.INCOME),
        )
        val monthlyStatements = MonthlyStatements(statements, 2021, 5)

        // act
        val exportSummary = createExcelExportService().export(monthlyStatements, directory, exportFilename)

        // assert
        assertThat(exportSummary.messages).containsExactlyInAnyOrder(
            ExportMessage(
                Critically.WARN,
                "Zugewiesen Bücher / Musik / Filme (Id: 1) zu Bücher / Musik (Zeile: 139): 30.0 => bitte unterschiedlichen Kategorienamen prüfen!"
            ),
            ExportMessage(Critically.INFO, "Zugewiesen Freizeit (Id: 2) zu Freizeit (Zeile: 137): 20.1"),
            ExportMessage(
                Critically.WARN,
                "Zugewiesen Geschenk (Id: 3) zu Geschenke / Party (Zeile: 138): 23.29 => bitte unterschiedlichen Kategorienamen prüfen!"
            ),
            ExportMessage(
                Critically.WARN,
                "Zugewiesen Klamotten (Id: 30) zu Klamotten (Zeile: 113): -90.93 => bitte negativen Wert prüfen!"
            ),
            ExportMessage(Critically.INFO, "Zugewiesen Streaming (Id: 98) zu Streaming (Zeile: 140): 4.95"),
            ExportMessage(Critically.INFO, "Zugewiesen Lebensmittel (Id: 99) zu Lebensmittel (Zeile: 104): 612.78"),
            ExportMessage(Critically.INFO, "Zugewiesen Gehalt Torsten (Id: 100) zu Gehalt Torsten (Zeile: 3): 1000.0"),
            ExportMessage(Critically.ERROR, "*** Keine Zeile gefunden für Spielzeug (Id: 8) ***"),
        )


        // tear down
        Files.delete(directory.resolve(exportFilename))
        Files.delete(exportSummary.backupFilePath)

    }

    @Test
    fun export_fileDoesNotExist_throwsException() {
        // arrange
        val directory = Path.of(testDirectory)
        val nonExistentFilename = "NonExistentFile.xlsx"
        val monthlyStatements = MonthlyStatements(emptyList(), 2021, 5)

        // act & assert
        assertThatThrownBy {
            createExcelExportService().export(monthlyStatements, directory, nonExistentFilename)
        }
            .isInstanceOf(IllegalArgumentException::class.java)
            .hasMessageContaining("Datei existiert nicht")
            .hasMessageContaining(nonExistentFilename)
    }

    @Test
    fun export_sheetDoesNotExist_throwsException() {
        // arrange
        val directory = Path.of(testDirectory)
        val filename = "KostenrechnungTest.xlsx"

        val nonExistentYear = 1999
        val monthlyStatements = MonthlyStatements(emptyList(), nonExistentYear, 5)

        // act & assert
        assertThatThrownBy {
            createExcelExportService().export(monthlyStatements, directory, filename)
        }
            .isInstanceOf(IllegalArgumentException::class.java)
            .hasMessageContaining("Sheet '$nonExistentYear' existiert nicht")
            .hasMessageContaining(filename)

        // tear down - clean up backup file if it was created
        val backupFiles = Files.list(directory)
            .filter { it.fileName.toString().startsWith("KostenrechnungTest_") }
            .toList()
        backupFiles.forEach { Files.deleteIfExists(it) }
    }

    @Test
    fun buildCategoryIdToRowMapping() {
        // arrange
        val directory = Path.of(testDirectory)
        val originalFilename = "KostenrechnungTest.xlsx"
        val backFilename = "KostenrechnungTest_backup.xlsx"
        Files.copy(directory.resolve(originalFilename), directory.resolve(backFilename))
        val workbook = XSSFWorkbook(directory.resolve(backFilename).toFile())
        val sheet = workbook.getSheet("2021")

        // act
        val categoryIdToRowMapping = createExcelExportService().buildCategoryIdToRowMapping(sheet)

        // assert
        assertThat(categoryIdToRowMapping).isEqualTo(
            mapOf(
                1 to ExcelCategory(138, "Bücher / Musik"),
                2 to ExcelCategory(136, "Freizeit"),
                3 to ExcelCategory(137, "Geschenke / Party"),
                30 to ExcelCategory(112, "Klamotten"),
                98 to ExcelCategory(139, "Streaming"),
                99 to ExcelCategory(103, "Lebensmittel"),
                100 to ExcelCategory(2, "Gehalt Torsten"),
            )
        )

        // tear down
        workbook.close()
        Files.delete(directory.resolve(backFilename))
    }

    @Test
    fun addSuccessMessage_categoryNameMatch_noWarning() {
        // arrange
        val statement = MonthlyStatement(1, "Spielzeug", -30.0, CategoryType.EXPENSE)
        val excelCategory = ExcelCategory(10, "Spielzeug")
        val rowIndex = 100

        // act
        val successMessage = createExcelExportService().createSuccessMessage(statement, excelCategory, rowIndex, 30.0)

        // assert
        assertThat(successMessage).isEqualTo(
            ExportMessage(
                Critically.INFO,
                "Zugewiesen Spielzeug (Id: 1) zu Spielzeug (Zeile: 101): 30.0"
            )
        )
    }

    @Test
    fun addSuccessMessage_categoryNameMatch_warning() {
        // arrange
        val statement = MonthlyStatement(1, "Spielzeug", -30.0, CategoryType.EXPENSE)
        val excelCategory = ExcelCategory(10, "Lebensmittel")
        val rowIndex = 100

        // act
        val successMessage = createExcelExportService().createSuccessMessage(statement, excelCategory, rowIndex, 30.0)

        // assert
        assertThat(successMessage).isEqualTo(
            ExportMessage(
                Critically.WARN,
                "Zugewiesen Spielzeug (Id: 1) zu Lebensmittel (Zeile: 101): 30.0 => bitte unterschiedlichen Kategorienamen prüfen!"
            )
        )
    }

    @Test
    fun addSuccessMessage_totalIsNegative_warning() {
        // arrange
        val statement = MonthlyStatement(1, "Spielzeug", -30.0, CategoryType.EXPENSE)
        val excelCategory = ExcelCategory(10, "Spielzeug")
        val rowIndex = 100

        // act
        val successMessage = createExcelExportService().createSuccessMessage(statement, excelCategory, rowIndex, -30.0)

        // assert
        assertThat(successMessage).isEqualTo(
            ExportMessage(
                Critically.WARN,
                "Zugewiesen Spielzeug (Id: 1) zu Spielzeug (Zeile: 101): -30.0 => bitte negativen Wert prüfen!"
            )
        )
    }

    @Test
    fun convertForExcel_income_alwaysPositive() {
        // arrange

        // act
        val result = createExcelExportService().convertForExcel(1000.0, CategoryType.INCOME)

        // assert
        assertThat(result).isEqualTo(1000.0)
    }

    @Test
    fun convertForExcel_expensePositive_changeSignToNegative() {
        // arrange

        // act
        val result = createExcelExportService().convertForExcel(1000.0, CategoryType.EXPENSE)

        // assert
        assertThat(result).isEqualTo(-1000.0)
    }

    @Test
    fun convertForExcel_expenseNegative_changeSignToPositive() {
        // arrange

        // act
        val result = createExcelExportService().convertForExcel(-1000.0, CategoryType.EXPENSE)

        // assert
        assertThat(result).isEqualTo(1000.0)
    }

}