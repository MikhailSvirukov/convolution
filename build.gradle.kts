plugins {
    kotlin("jvm") version "2.3.20"
    id("org.jlleitschuh.gradle.ktlint") version "14.2.0"
    application
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(kotlin("test"))
    testImplementation("org.junit.jupiter:junit-jupiter-params:5.10.2")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.10.2")
    implementation(files("libs/opencv-490.jar"))
    implementation("org.jetbrains.kotlinx:kotlinx-cli:0.3.6")
}

tasks.withType<JavaExec> {
    jvmArgs("-Djava.library.path=libs")
}

tasks.withType<Test> {
    jvmArgs("-Djava.library.path=libs")
}

kotlin {
    jvmToolchain(25)
}

ktlint {
    version = "1.4.0"
}

tasks.test {
    useJUnitPlatform()
}

application {
    mainClass.set("MainKt")
}
