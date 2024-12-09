import org.jlleitschuh.gradle.ktlint.KtlintExtension

plugins {
    kotlin("jvm") version "1.9.25"
    id("org.jetbrains.kotlin.plugin.spring") version "1.9.25"
    id("org.springframework.boot") version "3.4.0"
    id("io.spring.dependency-management") version "1.1.6"
    id("org.jlleitschuh.gradle.ktlint") version "12.1.0"
}

allprojects {
    group = "uket"
    version = "0.0.1-SNAPSHOT"

    repositories {
        mavenCentral()
    }

    tasks {
        withType<Test> {
            useJUnitPlatform()
        }
    }
}

allOpen {
    annotation("javax.persistence.Entity")
    annotation("javax.persistence.Embeddable")
    annotation("javax.persistence.MappedSuperclass")
}

subprojects {
    apply(plugin = "kotlin")
    apply(plugin = "org.springframework.boot")
    apply(plugin = "org.jetbrains.kotlin.plugin.spring")
    apply(plugin = "io.spring.dependency-management")
    apply(plugin = "org.jlleitschuh.gradle.ktlint")

    kotlin {
        jvmToolchain(21)
    }

    dependencies {
        implementation("org.jetbrains.kotlin:kotlin-reflect")
        implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
        testImplementation("org.junit.jupiter:junit-jupiter:5.10.2")
        testImplementation("org.assertj:assertj-core:3.25.3")
        testImplementation("io.kotest:kotest-runner-junit5:5.8.0")
        testImplementation("org.jeasy:easy-random-core:5.0.0")
    }

    configure<KtlintExtension> {
        verbose.set(true)
    }
}
