plugins {
    kotlin("jvm") version "2.1.21"
}

group = "kioskware.kstateflows"
version = "0.1.0-alpha"


repositories {
    mavenCentral()
}

dependencies {
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.8.1")
    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(17)
}