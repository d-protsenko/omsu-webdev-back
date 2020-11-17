plugins {
    kotlin("jvm") version "1.4.10"
}

group = "omsu.webdev"
version = "0.1.0"

repositories {
    mavenCentral()
}

dependencies {
    implementation(kotlin("stdlib"))
    testImplementation("io.kotlintest:kotlintest-runner-junit5:3.4.0")
    testImplementation("org.seleniumhq.selenium:selenium-java:3.4.0")
    testImplementation("org.seleniumhq.selenium:selenium-chrome-driver:3.4.0")
}

tasks.withType<Test> {
    useJUnitPlatform()
}