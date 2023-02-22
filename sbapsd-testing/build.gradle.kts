import org.gradle.api.tasks.testing.logging.TestLogEvent

plugins {
    id("java-base-conventions")
}

java {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
}

dependencies {
    testImplementation(project(":sbapsd-server"))

    testImplementation("org.springframework.boot:spring-boot-starter-webflux:2.7.7")
    testImplementation("org.springframework.boot:spring-boot-starter-test:2.7.7")

    testImplementation("org.junit.jupiter:junit-jupiter")
    testImplementation("org.assertj:assertj-core")
}

tasks.getByName<Test>("test") {
    systemProperty("sba.version", findProperty("sba.version") ?: "0.0.0")
    systemProperty("sba.server.port", findProperty("sba.server.port") ?: "8080")
    testLogging {
        events(TestLogEvent.PASSED, TestLogEvent.FAILED, TestLogEvent.SKIPPED)
    }
}
