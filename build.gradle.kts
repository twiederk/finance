import org.jetbrains.compose.desktop.application.dsl.TargetFormat
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("org.springframework.boot") version "2.5.4"
    id("io.spring.dependency-management") version "1.0.11.RELEASE"
    kotlin("plugin.spring") version "1.8.22"
    kotlin("jvm") version "1.8.22"
    id("org.jetbrains.compose") version "1.5.0"
}

group = "com.d20charactersheet.finance"
version = "1.14.0"


repositories {
    google()
    mavenCentral()
    maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
}

dependencies {
    testImplementation("org.springframework.boot:spring-boot-starter-test:2.6.5")
    testImplementation("org.assertj:assertj-core:3.22.0")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit5:1.8.22")
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.8.2")
    testImplementation("org.mockito.kotlin:mockito-kotlin:4.0.0")
    testImplementation("com.tngtech.archunit:archunit-junit5:0.23.1")

    implementation("org.jetbrains.kotlin:kotlin-reflect:1.8.22")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8:1.8.22")
    implementation("org.springframework.boot:spring-boot-starter-jdbc:2.6.5")
    implementation(compose.desktop.currentOs)
    implementation("net.sf.ucanaccess:ucanaccess:5.0.1")
}

tasks.test {
    useJUnitPlatform()
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "15"
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