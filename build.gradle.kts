plugins {
    kotlin("jvm") version "2.3.20"
    application
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(kotlin("test"))
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.10.2")
    implementation(files("lib/opencv-490.jar"))
}

tasks.withType<JavaExec> {
    jvmArgs("-Djava.library.path=libs")
}

kotlin {
    jvmToolchain(25)
}

tasks.test {
    useJUnitPlatform()
}

application {
    mainClass.set("MainKt")
}