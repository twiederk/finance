package com.d20charactersheet.finance.service

import com.d20charactersheet.finance.domain.CategoryType
import com.d20charactersheet.finance.domain.Critically
import com.d20charactersheet.finance.domain.ExportMessage
import com.d20charactersheet.finance.domain.ExportSummary
import com.d20charactersheet.finance.domain.MonthlyStatement
import com.d20charactersheet.finance.domain.MonthlyStatements
import org.apache.poi.ss.usermodel.CellType
import org.apache.poi.xssf.usermodel.XSSFRow
import org.apache.poi.xssf.usermodel.XSSFSheet
import org.apache.poi.xssf.usermodel.XSSFWorkbook
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import java.nio.file.Files
import java.nio.file.Path
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

typealias ExcelCategory = Pair<Int, String>

@Service
class ExcelExportService(
    @Value("\${export.directory}") val exportDirectory: String,
    @Value("\${export.filename}") val exportFilename: String
) {

    private val columnOffset = 2
    private val idColumnIndex = 1
    private val nameColumnIndex = 2

    /**
     * Creates a backup copy of the specified file with a timestamp appended to the filename.
     *
     * @param directory The directory where the original file is located
     * @param filename The name of the file to back up
     * @return The path to the created backup file
     */
    fun backup(directory: Path, filename: String): Path {
        val backupFileName = backupFileName(filename)

        val backupFile = Path.of(directory.toString(), backupFileName)
        val originalFile = Path.of(directory.toString(), filename)
        Files.copy(originalFile, backupFile)

        return backupFile
    }

    private fun backupFileName(filename: String): String {
        val timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"))

        val fileNameWithoutExtension = filename.substringBeforeLast('.')
        val extension = filename.substringAfterLast('.')

        val backupFileName = "${fileNameWithoutExtension}_${timestamp}.${extension}"
        return backupFileName
    }

    fun export(monthlyStatements: MonthlyStatements): ExportSummary {
        return export(monthlyStatements, Path.of(exportDirectory), exportFilename)
    }

    fun export(monthlyStatements: MonthlyStatements, directory: Path, filename: String): ExportSummary {
        val originalFilePath = Path.of(directory.toString(), filename)
        if (!Files.exists(originalFilePath)) {
            throw IllegalArgumentException("Datei existiert nicht: $originalFilePath")
        }
        val backupFilePath = backup(directory, filename)
        val (workbook, messages) = exportToSheet(directory, filename, monthlyStatements)
        val outputFilePath = save(directory, workbook)
        Files.delete(outputFilePath)
        return ExportSummary(backupFilePath, messages)
    }

    private fun exportToSheet(
        directory: Path,
        filename: String,
        monthlyStatements: MonthlyStatements
    ): Pair<XSSFWorkbook, List<ExportMessage>> {
        val originalFilePath = Path.of(directory.toString(), filename)
        val workbook = XSSFWorkbook(originalFilePath.toFile())
        val sheetName = monthlyStatements.year.toString()
        val sheet = workbook.getSheet(sheetName)
            ?: throw IllegalArgumentException("Sheet '$sheetName' existiert nicht in Datei: $originalFilePath")

        val categoryIdToRowMapping = buildCategoryIdToRowMapping(sheet)

        val messages = mutableListOf<ExportMessage>()
        monthlyStatements.monthlyStatements.forEach { statement ->
            exportToCell(categoryIdToRowMapping, statement, sheet, monthlyStatements.month, messages)
        }
        return Pair(workbook, messages)
    }

    private fun exportToCell(
        categoryIdToRowMapping: Map<Int, ExcelCategory>,
        statement: MonthlyStatement,
        sheet: XSSFSheet,
        month: Int,
        messages: MutableList<ExportMessage>
    ) {
        val excelCategory = categoryIdToRowMapping[statement.categoryId]
        if (excelCategory == null) {
            messages.add(
                ExportMessage(
                    Critically.ERROR,
                    "*** Keine Zeile gefunden für ${statement.categoryName} (Id: ${statement.categoryId}) ***"
                )
            )
            return
        }
        val rowIndex = excelCategory.first
        val row = sheet.getRow(rowIndex)
        val cell = row.getCell(month + columnOffset)
        val total = convertForExcel(statement.total, statement.categoryType)
        cell.setCellValue(total)
        messages.add(createSuccessMessage(statement, excelCategory, rowIndex, total))
    }

    fun createSuccessMessage(
        statement: MonthlyStatement,
        excelCategory: ExcelCategory,
        rowIndex: Int,
        total: Double
    ): ExportMessage {
        return if (total < 0.0) {
            ExportMessage(
                Critically.WARN,
                "Zugewiesen ${statement.categoryName} (Id: ${statement.categoryId}) zu ${excelCategory.second} (Zeile: ${rowIndex + 1}): $total => bitte negativen Wert prüfen!"
            )
        } else {
            if (statement.categoryName == excelCategory.second) {
                ExportMessage(
                    Critically.INFO,
                    "Zugewiesen ${statement.categoryName} (Id: ${statement.categoryId}) zu ${excelCategory.second} (Zeile: ${rowIndex + 1}): $total"
                )
            } else {
                ExportMessage(
                    Critically.WARN,
                    "Zugewiesen ${statement.categoryName} (Id: ${statement.categoryId}) zu ${excelCategory.second} (Zeile: ${rowIndex + 1}): $total => bitte unterschiedlichen Kategorienamen prüfen!"
                )
            }
        }
    }

    fun buildCategoryIdToRowMapping(sheet: XSSFSheet): Map<Int, ExcelCategory> {
        val categoryIdToRowMapping = mutableMapOf<Int, ExcelCategory>()
        for (rowIndex in 0..sheet.lastRowNum) {
            val row = sheet.getRow(rowIndex) ?: continue
            val idCell = row.getCell(idColumnIndex) ?: continue
            if (idCell.cellType == CellType.NUMERIC) {
                val categoryId = idCell.numericCellValue.toInt()
                val categoryName = readExcelCategoryName(row)
                categoryIdToRowMapping[categoryId] = ExcelCategory(rowIndex, categoryName)
            }
        }
        return categoryIdToRowMapping
    }

    private fun readExcelCategoryName(row: XSSFRow): String {
        val nameCell = row.getCell(nameColumnIndex) ?: return ""
        return if (nameCell.cellType == CellType.STRING) {
            nameCell.stringCellValue
        } else {
            "Fehlender Excel-Kategoriename"
        }

    }

    private fun save(directory: Path, workbook: XSSFWorkbook): Path {
        val outputFilePath = Path.of(directory.toString(), "KostenrechnungTest_modified.xlsx")
        outputFilePath.toFile().outputStream().use { output ->
            workbook.write(output)
        }
        workbook.close()
        return outputFilePath
    }

    fun convertForExcel(total: Double, categoryType: CategoryType): Double {
        return when (categoryType) {
            CategoryType.EXPENSE -> total * -1
            CategoryType.INCOME -> total
        }
    }
}


