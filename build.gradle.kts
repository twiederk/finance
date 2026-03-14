import org.jetbrains.compose.desktop.application.dsl.TargetFormat
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("org.springframework.boot") version "3.1.6"
    id("io.spring.dependency-management") version "1.1.0"
    kotlin("plugin.spring") version "1.9.10"
    kotlin("jvm") version "1.9.10"
    id("org.jetbrains.compose") version "1.6.0"
}

group = "com.d20charactersheet.finance"
version = "1.16.0"


repositories {
    google()
    mavenCentral()
    maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
}

dependencies {
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.assertj:assertj-core:3.27.7")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit5:1.9.10")
    testImplementation("org.mockito.kotlin:mockito-kotlin:4.1.0")

    implementation("org.jetbrains.kotlin:kotlin-reflect:1.9.10")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8:1.9.10")
    implementation("org.springframework.boot:spring-boot-starter-jdbc")
    implementation(compose.desktop.currentOs)
    implementation("io.github.spannm:ucanaccess:5.1.5")
    implementation("org.apache.poi:poi-ooxml:5.5.1")
}

tasks.test {
    useJUnitPlatform()
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(19))
    }
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "19"
}

compose.desktop {
    application {
        mainClass = "FinanceApplicationKt"
        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
            packageName = "finance"
            packageVersion = "1.0.0"
        }
    }
}