dependencies {
    implementation("org.springframework.boot:spring-boot-starter")
    implementation("org.redisson:redisson:3.49.0")
    testImplementation("org.springframework.boot:spring-boot-starter-test")

    compileOnly("org.redisson:redisson:3.49.0")
    compileOnly("org.projectlombok:lombok")

    annotationProcessor("org.projectlombok:lombok")

    testImplementation("org.testcontainers:testcontainers:1.21.1")
    testImplementation("org.testcontainers:junit-jupiter:1.21.1")
    testImplementation("org.springframework.boot:spring-boot-starter-data-redis")

    testFixturesImplementation("org.springframework.boot:spring-boot-starter-test")
    testFixturesImplementation("org.testcontainers:testcontainers:1.21.1")
    testFixturesImplementation("org.testcontainers:testcontainers:1.21.1")
    testFixturesImplementation("org.testcontainers:junit-jupiter:1.21.1")
    testFixturesImplementation("org.springframework.boot:spring-boot-starter-data-redis")
    testFixturesImplementation("org.springframework.boot:spring-boot-starter-data-r2dbc")
    testFixturesImplementation("org.redisson:redisson:3.49.0")
    testFixturesImplementation("mysql:mysql-connector-java")
    testFixturesImplementation("org.springframework.boot:spring-boot-starter-jdbc")
}
