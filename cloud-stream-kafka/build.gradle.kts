dependencies {
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.cloud:spring-cloud-stream:4.2.0")
    implementation("org.springframework.cloud:spring-cloud-starter-stream-kafka:4.2.0")
    implementation("io.confluent:kafka-avro-serializer:7.8.0")
    implementation("org.apache.avro:avro:1.12.0")
    implementation("org.springframework.cloud:spring-cloud-stream-schema-registry-client:4.2.0")
    compileOnly("org.projectlombok:lombok")
    annotationProcessor("org.projectlombok:lombok")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.springframework.cloud:spring-cloud-stream-test-binder:4.0.3")
    testImplementation("org.springframework.cloud:spring-cloud-schema-registry-client:1.1.5")
}

avro {
    setCreateSetters(false)
    setCreateOptionalGetters(true)
    setGettersReturnOptional(true)
    setOptionalGettersForNullableFieldsOnly(true)
}

tasks.generateAvroJava {
    source("src/main/resources/avro")
}
