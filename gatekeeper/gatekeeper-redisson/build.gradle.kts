dependencies {
    implementation("org.springframework.boot:spring-boot-starter")
    implementation("org.redisson:redisson:3.49.0")

    compileOnly("org.redisson:redisson:3.49.0")
    compileOnly("org.projectlombok:lombok")

    annotationProcessor("org.projectlombok:lombok")

    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.testcontainers:testcontainers:1.21.1")
    testImplementation("org.testcontainers:junit-jupiter:1.21.1")
    testImplementation("org.springframework.boot:spring-boot-starter-data-redis")
    testImplementation(testFixtures(project(":gatekeeper:gatekeeper-core")))
}
