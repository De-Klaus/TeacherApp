plugins {
    id("org.springframework.boot") version "3.3.2"
    id("io.spring.dependency-management") version "1.1.4"
    id("java")
}

group = "org.teacher"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("jakarta.servlet:jakarta.servlet-api:6.0.0")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-security")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("org.springframework.boot:spring-boot-starter-data-rest")
    implementation("com.fasterxml.jackson.core:jackson-databind")
    implementation("org.springframework.kafka:spring-kafka")
    implementation("org.mapstruct:mapstruct:1.5.5.Final")
    //WhatsAppClient
    implementation("com.twilio.sdk:twilio:8.31.1")
    // Шифрование паролей
    implementation("org.springframework.security:spring-security-crypto")
    implementation("org.postgresql:postgresql")
    implementation("org.projectlombok:lombok")
    implementation("io.jsonwebtoken:jjwt-api:0.12.6")

    annotationProcessor("org.mapstruct:mapstruct-processor:1.5.5.Final")
    annotationProcessor("org.projectlombok:lombok")

    runtimeOnly("io.jsonwebtoken:jjwt-impl:0.12.6")
    runtimeOnly("io.jsonwebtoken:jjwt-jackson:0.12.6")

    testImplementation("com.h2database:h2:2.2.224")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.springframework.kafka:spring-kafka-test")
}


tasks.test {
    useJUnitPlatform()
}