plugins {
    kotlin("jvm") version "1.8.21"
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
}

tasks.test {
    useJUnitPlatform()
}

kotlin {
    jvmToolchain(11)
}