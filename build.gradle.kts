import org.jlleitschuh.gradle.ktlint.KtlintExtension

plugins {
    // 플러그인
    kotlin("jvm") version "1.9.25"
    kotlin("plugin.jpa") version "1.9.25"
    id("org.jetbrains.kotlin.plugin.spring") version "1.9.25" // 스프링 어노테이션 처리 지원(open 관련)
    id("org.springframework.boot") version "3.4.0" // 스프링 부트
    id("io.spring.dependency-management") version "1.1.6" // 스프링 의존성 관리
    id("org.jlleitschuh.gradle.ktlint") version "12.1.0" // 린트
}

group = "uket"
version = "0.0.1-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("org.springframework.boot:spring-boot-starter-web")

    // test
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.junit.jupiter:junit-jupiter:5.10.2")
    testImplementation("org.mockito.kotlin:mockito-kotlin:4.1.0")
    testImplementation("org.assertj:assertj-core:3.25.3")

    testImplementation("io.kotest:kotest-runner-junit5:5.8.1")
    testImplementation("io.kotest:kotest-assertions-core:5.8.1") // Kotest assertions
    testImplementation("io.kotest.extensions:kotest-extensions-spring:1.3.0") // Spring 통합 (필요시)
    testImplementation("io.mockk:mockk:1.13.5") // MockK 라이브러리

    testImplementation("org.jeasy:easy-random-core:5.0.0")
    testImplementation("com.h2database:h2:2.2.224")

    // jwt
    implementation("io.jsonwebtoken:jjwt-api:0.12.3")
    implementation("io.jsonwebtoken:jjwt-impl:0.12.3")
    implementation("io.jsonwebtoken:jjwt-jackson:0.12.3")

    // swagger
    implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.3.0")

    // security
    implementation("org.springframework.boot:spring-boot-starter-security")
    testImplementation("org.springframework.security:spring-security-test")

    // DB
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    runtimeOnly("com.mysql:mysql-connector-j")
    implementation("com.github.f4b6a3:ulid-creator:5.2.3")

    // mail
    implementation("org.springframework.boot:spring-boot-starter-mail")

    // redis
    implementation("org.springframework.boot:spring-boot-starter-data-redis")
    implementation("org.redisson:redisson-spring-boot-starter:3.30.0")
    annotationProcessor("org.springframework.boot:spring-boot-configuration-processor")

    // aop
    implementation("org.springframework.boot:spring-boot-starter-aop")

    // logger
    implementation("org.slf4j:slf4j-api:2.0.9")
    implementation("io.github.microutils:kotlin-logging-jvm:3.0.5")
    runtimeOnly("ch.qos.logback:logback-classic:1.4.11")

    // s3
    implementation("io.awspring.cloud:spring-cloud-aws-starter:3.1.1")
    implementation(platform("software.amazon.awssdk:bom:2.25.46"))
    implementation("software.amazon.awssdk:s3")
}

tasks.test {
    useJUnitPlatform() // 테스트 시, JUnit 사용
}

kotlin {
    jvmToolchain(21)
}

allOpen {
    // 해당 어노테이션이 붙은 클래스는 자동으로 open 처리
    annotation("javax.persistence.Entity")
    annotation("javax.persistence.Embeddable")
    annotation("javax.persistence.MappedSuperclass")
}

configure<KtlintExtension> {
    verbose.set(true)
}
