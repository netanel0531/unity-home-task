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
    implementation("io.github.bonigarcia:webdrivermanager:5.9.1")

    // TestNG for our test framework
    testImplementation("org.testng:testng:7.10.2")
}

tasks.test {
    // Use TestNG as the test framework
    useTestNG()

    // Set a timeout for tests to prevent them from running indefinitely
    testLogging {
        events("started", "passed", "skipped", "failed")
    }
}