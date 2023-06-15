plugins {
    kotlin("jvm") version "1.8.21"
    id("io.gitlab.arturbosch.detekt").version("1.22.0")
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(kotlin("test"))

    implementation("com.h2database:h2:2.1.214")
    // https://mvnrepository.com/artifact/com.h2database/h2

    implementation("com.zaxxer:HikariCP:5.0.1")
    // https://mvnrepository.com/artifact/com.zaxxer/HikariCP

    implementation("ch.qos.logback:logback-classic:1.4.5")
    // https://mvnrepository.com/artifact/ch.qos.logback/logback-classic/1.4.5

    implementation("org.slf4j:slf4j-api:2.0.3")
    // https://mvnrepository.com/artifact/org.slf4j/slf4j-api/2.0.3

    implementation("de.m3y.kformat:kformat:0.9")
    // https://mvnrepository.com/artifact/de.m3y.kformat/kformat/0.9
}


tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}

tasks.withType<io.gitlab.arturbosch.detekt.Detekt>().configureEach {
    reports {
        html.required.set(true) // observe findings in your browser with structure and code snippets
        xml.required.set(true) // checkstyle like format mainly for integrations like Jenkins
        txt.required.set(true) // similar to the console output, contains issue signature to manually edit baseline files
        sarif.required.set(true) // standardized SARIF format (https://sarifweb.azurewebsites.net/) to support integrations with Github Code Scanning
        md.required.set(true) // simple Markdown format
    }
}

tasks.test {
    useJUnitPlatform()
}

kotlin {
    jvmToolchain(17)
}