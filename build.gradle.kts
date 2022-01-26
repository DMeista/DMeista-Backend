import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("org.springframework.boot") version "2.6.2"
    id("io.spring.dependency-management") version "1.0.10.RELEASE"
    kotlin("jvm") version "1.6.0"
    kotlin("plugin.spring") version "1.6.0"
    kotlin("plugin.jpa") version "1.6.0"
}

group = "sinhee.kang"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_11

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-web:2.6.2")
    implementation("org.springframework.boot:spring-boot-starter-actuator:2.6.2")
    implementation("org.springframework.boot:spring-boot-starter-mail:2.6.2")

    implementation("org.springframework.security:spring-security-crypto:5.5.1")
    implementation("io.jsonwebtoken:jjwt:0.9.1")

    implementation("org.springframework.boot:spring-boot-starter-data-jpa:2.6.2")
    implementation("org.springframework.boot:spring-boot-starter-data-redis:2.6.2")
    implementation("it.ozimov:embedded-redis:0.7.3") {
        exclude(group = "org.slf4j", module = "slf4j-simple")
    }
    runtimeOnly("mysql:mysql-connector-java:8.0.25")

    implementation("com.squareup.retrofit2:retrofit:2.7.2")
    implementation("com.squareup.retrofit2:converter-jackson:2.7.2")

    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.0-native-mt")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-reactor:1.6.0-native-mt")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core-jvm:1.6.0-native-mt")

    implementation("javax.validation:validation-api:2.0.1.Final")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.13.1")

    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")

    testImplementation("org.springframework.boot:spring-boot-starter-test:2.6.2")
    testImplementation("com.h2database:h2:1.4.200")
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs = listOf("-Xjsr305=strict")
        jvmTarget = "11"
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}

tasks.getByName<Jar>("jar") {
    enabled = false
}

allOpen {
    annotation("javax.persistence.Entity")
    annotation("javax.persistence.MappedSuperclass")
    annotation("javax.persistence.Embeddable")
}
