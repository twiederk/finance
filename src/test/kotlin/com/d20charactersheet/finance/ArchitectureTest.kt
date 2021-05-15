package com.d20charactersheet.finance

import com.tngtech.archunit.core.importer.ClassFileImporter
import com.tngtech.archunit.core.importer.ImportOption.DoNotIncludeTests
import com.tngtech.archunit.lang.ArchRule
import com.tngtech.archunit.library.Architectures.layeredArchitecture
import com.tngtech.archunit.library.dependencies.SlicesRuleDefinition.slices
import org.junit.jupiter.api.Test


class ArchitectureTest {

    private val classes =
        ClassFileImporter().withImportOption(DoNotIncludeTests()).importPackages("com.d20charactersheet.finance")

    @Test
    fun modulePackagesShouldNotBeAccessedByOthers() {

        // Act
        val rule: ArchRule = layeredArchitecture() //
            .layer("domain").definedBy("..domain..") //
            .layer("gui").definedBy("..gui..") //
            .layer("import").definedBy("..import..") //
            .layer("main").definedBy("..finance..")
            .whereLayer("gui").mayOnlyBeAccessedByLayers("main") //
            .whereLayer("import").mayOnlyBeAccessedByLayers("main") //

        // Assert
        rule.check(classes)
    }

    @Test
    fun noCyclicDependencies() {

        // Act
        val rule: ArchRule = slices().matching("com.d20charactersheet.finance.(**)").should().beFreeOfCycles()

        // Assert
        rule.check(classes)
    }

}