plugins {
    kotlin("jvm") version "2.0.0"
}

kotlin {
    jvmToolchain(21)
}

dependencies {
    implementation("io.projectreactor:reactor-core:3.7.7")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-reactor:1.10.2")
    testImplementation("io.projectreactor:reactor-test")
    testImplementation(kotlin("test"))
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.10.2")
    testImplementation(testFixtures(project(":gatekeeper:gatekeeper-core")))
}
