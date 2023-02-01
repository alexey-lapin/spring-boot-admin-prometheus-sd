plugins {
    id("java-library")
}

dependencies {
    api(project(":sbapsd-core"))

    annotationProcessor("org.projectlombok:lombok:1.18.18")
    compileOnly("org.projectlombok:lombok:1.18.18")

    compileOnly("de.codecentric:spring-boot-admin-server:2.7.10")
    compileOnly("org.springframework.boot:spring-boot-starter-web:2.7.7")

    testImplementation("org.junit.jupiter:junit-jupiter-api:5.8.1")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.8.1")
}

tasks.getByName<Test>("test") {
    useJUnitPlatform()
}
