import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("org.springframework.boot") version "2.4.1"
    id("io.spring.dependency-management") version "1.0.10.RELEASE"
    kotlin("jvm") version "1.5.31"
    kotlin("plugin.spring") version "1.5.31"
    kotlin("plugin.jpa") version "1.5.31"
    kotlin("plugin.allopen") version "1.5.31"
}

allOpen {
    annotation("javax.persistence.Entity")
    annotation("javax.persistence.MappedSuperclass")
    annotation("javax.persistence.Embeddable")
}

group = "sinhee.kang"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_11

configurations {
    all {
        exclude(group = "org.slf4j", module = "slf4j-simple")
        exclude(group = "org.slf4j", module = "slf4j-log4j12")
    }
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-web:2.5.5")
    implementation("org.springframework.boot:spring-boot-starter-cache:2.5.5")

    implementation("org.springframework.boot:spring-boot-starter-security:2.5.5")
    implementation("org.springframework.boot:spring-boot-starter-mail:2.5.5")
    implementation("io.jsonwebtoken:jjwt:0.9.1")

    implementation("org.springframework.boot:spring-boot-starter-data-jpa:2.5.5")
    implementation("org.springframework.boot:spring-boot-starter-data-redis:2.5.5")
    implementation("it.ozimov:embedded-redis:0.7.3")
    runtimeOnly("mysql:mysql-connector-java:8.0.25")

    implementation("io.springfox:springfox-boot-starter:3.0.0")

    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-jackson:2.9.0")

    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.5.2-native-mt")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-reactor:1.5.2-native-mt")

    implementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.12.5")

    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")

    testImplementation("org.springframework.boot:spring-boot-starter-test:2.5.5") {
        exclude(group = "org.junit.vintage", module = "junit-vintage-engine")
    }
    testImplementation("org.springframework.security:spring-security-test:5.5.1")
    testImplementation("com.h2database:h2:1.4.200")
}

tasks.withType<Test> {
    useJUnitPlatform()
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs = listOf("-Xjsr305=strict")
        jvmTarget = "11"
    }
}
