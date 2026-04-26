import me.champeau.jmh.JMHTask

plugins {
    kotlin("jvm") version "2.3.20"
    id("me.champeau.jmh") version "0.7.3"
    id("org.jlleitschuh.gradle.ktlint") version "14.2.0"
    application
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.10.2")
    implementation("org.jetbrains.kotlinx:kotlinx-cli:0.3.6")
    implementation(files("libs/opencv-490.jar"))
    testImplementation(kotlin("test"))
    testImplementation("org.junit.jupiter:junit-jupiter-params:5.10.2")
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.8.1")
    implementation("org.jetbrains.kotlinx:kotlinx-benchmark-runtime:0.4.16")
}

tasks.withType<JavaExec> {
    jvmArgs("-Djava.library.path=libs")
}

tasks.withType<Test> {
    jvmArgs("-Djava.library.path=libs")
    useJUnitPlatform()
}


application {
    mainClass.set("org.example.MainKt")
}

kotlin {
    jvmToolchain(25)
}

ktlint {
    version = "1.4.0"
}

jmh {
    jvmArgs.set(listOf("-Djava.library.path=libs"))
}