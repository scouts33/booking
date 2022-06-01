import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.springframework.boot.gradle.tasks.bundling.BootJar

plugins {
    id("org.springframework.boot") version "2.7.0"
    id("io.spring.dependency-management") version "1.0.11.RELEASE"
    id("org.jlleitschuh.gradle.ktlint") version "10.3.0"
    kotlin("jvm") version "1.6.21"
    kotlin("plugin.spring") version "1.6.21"
}

allprojects {
    repositories {
        mavenLocal()
        mavenCentral()
        maven(url = "https://repo.spring.io/release")
    }
}

subprojects {
    apply(plugin = "kotlin")
    apply(plugin = "kotlin-kapt")
    apply(plugin = "org.springframework.boot")
    apply(plugin = "io.spring.dependency-management")
    apply(plugin = "org.jlleitschuh.gradle.ktlint")
    apply(plugin = "org.jetbrains.kotlin.plugin.spring")

    group = "com.sungmin"
    java.sourceCompatibility = JavaVersion.VERSION_11

    dependencies {
        implementation("org.springframework.boot:spring-boot-starter-web")
        implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
        implementation("org.jetbrains.kotlin:kotlin-reflect")
        implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
        implementation("org.springframework.cloud:spring-cloud-starter-config:3.1.3")
        testImplementation("org.springframework.boot:spring-boot-starter-test")
        testImplementation("io.mockk:mockk:1.12.4")
        testImplementation("com.ninja-squad:springmockk:3.1.1")
        testImplementation("org.springframework.boot:spring-boot-starter-test") {
            exclude(module = "mockito-core")
        }
    }

    tasks {
        withType<KotlinCompile> {
            kotlinOptions {
                freeCompilerArgs = listOf("-Xjsr305=strict")
                jvmTarget = "11"
            }
        }

        withType<Test> {
            useJUnitPlatform()
        }

        ktlint {
            version.set("0.43.0")
        }
    }
}

project(":common") {
    val jar: Jar by tasks
    val bootJar: BootJar by tasks
    bootJar.enabled = false
    jar.enabled = true
}

project(":domain") {
    val jar: Jar by tasks
    val bootJar: BootJar by tasks
    bootJar.enabled = false
    jar.enabled = true

    dependencies {
        implementation(project(":common"))

        implementation("org.springframework:spring-context")
        implementation("org.springframework.boot:spring-boot-starter")
        implementation("org.springframework.boot:spring-boot-starter-data-mongodb")
        testImplementation("de.flapdoodle.embed:de.flapdoodle.embed.mongo")
    }
}

project(":app:app-client-api") {
    val snippetsDir = file("build/generated-snippets")
    val jar: Jar by tasks
    val bootJar: BootJar by tasks
    bootJar.enabled = true
    jar.enabled = false

    tasks {
        test {
            outputs.dir(snippetsDir)
        }
    }

    dependencies {
        implementation(project(":common"))
        implementation(project(":domain"))

        implementation("org.springframework.boot:spring-boot-starter-web")
        implementation("org.springframework.boot:spring-boot-starter-validation")
    }
}
