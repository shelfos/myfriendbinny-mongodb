//import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "2.1.0"
    kotlin("plugin.serialization") version "1.8.0"
    id("application")
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.8.0")
    implementation(kotlin("stdlib"))
    testImplementation(kotlin("test"))

    // Database dependencies
    implementation("org.litote.kmongo:kmongo:4.3.0")
    implementation("io.github.cdimascio:dotenv-kotlin:6.4.1")

    // Logging dependencies (updated versions)
    implementation("org.slf4j:slf4j-api:2.0.9")           // Latest SLF4J version
    implementation("ch.qos.logback:logback-classic:1.4.14")
    implementation("ch.qos.logback:logback-core:1.4.14")

    implementation("com.fasterxml.jackson.core:jackson-databind:2.13.4.2")

}

tasks.register<Copy>("copyEnv") {
    from(file(".env"))
    into(file("build/libs"))
}

tasks.withType<CreateStartScripts> {
    dependsOn(tasks.named("copyEnv"))
}

tasks.test {
    useJUnitPlatform()
}

tasks.build {
    dependsOn(tasks.named("copyEnv"))
}

kotlin {
    jvmToolchain(23)
}

application {
    mainClass.set("org.example.MainKt")
}

tasks.jar {
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
    manifest {
        attributes["Main-Class"] = "org.example.MainKt"
    }
    from({
        configurations.runtimeClasspath.get().map { if (it.isDirectory) it else zipTree(it) }
    })
}