plugins {
    id("java")
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    // Selenium Java bindings for browser automation
    implementation("org.seleniumhq.selenium:selenium-java:4.22.0")

    // WebDriverManager to automatically download and manage browser drivers
    implementation("io.github.bonigarcia:webdrivermanager:6.2.0")

    testImplementation("org.testng:testng:7.10.2")

    testImplementation("org.slf4j:slf4j-simple:2.0.13")
}

tasks.test {
    // Use TestNG as the test framework
    useTestNG()

    // Set a timeout for tests to prevent them from running indefinitely
    testLogging {
        events("started", "passed", "skipped", "failed")
    }
}