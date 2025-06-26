plugins {
	java
	id("org.springframework.boot") version "3.4.2"
	id("io.spring.dependency-management") version "1.1.7"
	id("com.github.davidmc24.gradle.plugin.avro") version "1.9.1"
	id("java-library")
	id("java-test-fixtures")
}

java {
	toolchain {
		languageVersion = JavaLanguageVersion.of(21)
	}
}

configurations {
	compileOnly {
		extendsFrom(configurations.annotationProcessor.get())
	}
}

allprojects {
	group = "com.teamhide"
	version = "0.0.1-SNAPSHOT"

	repositories {
		mavenCentral()
		maven { url = uri("https://packages.confluent.io/maven/") }
	}
}

subprojects {
	apply(plugin = "java")

	dependencies {
		testImplementation("org.assertj:assertj-core:3.27.3")
		testRuntimeOnly("org.junit.platform:junit-platform-launcher")
	}

	tasks.withType<Test> {
		useJUnitPlatform()
	}
}

project(":playground-server") {
	apply(plugin = "org.springframework.boot")
	apply(plugin = "io.spring.dependency-management")

	dependencies {
		implementation("org.springframework.boot:spring-boot-starter-validation")
		implementation("org.springframework.boot:spring-boot-starter-web")
		compileOnly("org.projectlombok:lombok")
		annotationProcessor("org.projectlombok:lombok")
		testImplementation("org.springframework.boot:spring-boot-starter-test")
	}
}

project(":httpinterface") {
	apply(plugin = "org.springframework.boot")
	apply(plugin = "io.spring.dependency-management")

	dependencies {
		implementation("org.springframework.boot:spring-boot-starter-web")
		compileOnly("org.projectlombok:lombok")
		annotationProcessor("org.projectlombok:lombok")
		testImplementation("org.springframework.boot:spring-boot-starter-test")
	}
}

project(":beanprocessor") {
	apply(plugin = "org.springframework.boot")
	apply(plugin = "io.spring.dependency-management")

	dependencies {
		implementation("org.springframework.boot:spring-boot-starter-web")
		compileOnly("org.projectlombok:lombok")
		annotationProcessor("org.projectlombok:lombok")
		testImplementation("org.springframework.boot:spring-boot-starter-test")
	}
}

project(":functionalconfig") {
	dependencies {}
}

project(":webflux-world") {
	apply(plugin = "org.springframework.boot")
	apply(plugin = "io.spring.dependency-management")

	dependencies {
		implementation("org.springframework.boot:spring-boot-starter-data-r2dbc")
		implementation("org.springframework.boot:spring-boot-starter-data-mongodb-reactive")
		implementation("org.springframework.boot:spring-boot-starter-data-redis-reactive")
		implementation("org.springframework.boot:spring-boot-starter-web")
		implementation("org.springframework.boot:spring-boot-starter-webflux")
		implementation("org.springframework.boot:spring-boot-starter-validation")
		implementation("io.asyncer:r2dbc-mysql:1.3.1")
		implementation("org.flywaydb:flyway-core")
		implementation("org.flywaydb:flyway-mysql")
		implementation("io.netty:netty-resolver-dns-native-macos:4.1.68.Final:osx-aarch_64")
		compileOnly("org.projectlombok:lombok")
		runtimeOnly("com.mysql:mysql-connector-j")
		annotationProcessor("org.projectlombok:lombok")
		testImplementation("org.springframework.boot:spring-boot-starter-test")
		testImplementation("io.projectreactor:reactor-test")
		testRuntimeOnly("org.junit.platform:junit-platform-launcher")
	}
}

project(":cloud-stream-kafka") {
	apply(plugin = "org.springframework.boot")
	apply(plugin = "io.spring.dependency-management")
	apply(plugin = "com.github.davidmc24.gradle.plugin.avro")

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
}

project(":rule-engine") {
	apply(plugin = "org.springframework.boot")
	apply(plugin = "io.spring.dependency-management")

	dependencies {
		testImplementation("org.springframework.boot:spring-boot-starter-test")
	}
}

project(":fallbackcache") {
	apply(plugin = "org.springframework.boot")
	apply(plugin = "io.spring.dependency-management")

	dependencies {
		testImplementation("org.springframework.boot:spring-boot-starter-test")
	}
}

project(":bulkhead") {
	apply(plugin = "org.springframework.boot")
	apply(plugin = "io.spring.dependency-management")

	dependencies {
		testImplementation("org.springframework.boot:spring-boot-starter-test")
	}
}

project(":retry") {
	apply(plugin = "org.springframework.boot")
	apply(plugin = "io.spring.dependency-management")

	dependencies {
		testImplementation("org.springframework.boot:spring-boot-starter-test")
	}
}

project(":annotation-validator") {
	apply(plugin = "org.springframework.boot")
	apply(plugin = "io.spring.dependency-management")

	dependencies {
		testImplementation("org.springframework.boot:spring-boot-starter-test")
	}
}

project(":gatekeeper") {
	allprojects {
		apply(plugin = "org.springframework.boot")
		apply(plugin = "io.spring.dependency-management")
		apply(plugin = "java-library")
		apply(plugin = "java-test-fixtures")
	}
}

project(":gatekeeper:gatekeeper-core") {
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
		testFixturesImplementation("org.redisson:redisson:3.49.0")
	}
}

project(":gatekeeper:gatekeeper-redisson") {
	dependencies {
		api(project(":gatekeeper:gatekeeper-core"))
		implementation("org.springframework.boot:spring-boot-starter")
		implementation("org.redisson:redisson:3.49.0")
		testImplementation("org.springframework.boot:spring-boot-starter-test")
		compileOnly("org.redisson:redisson:3.49.0")
		compileOnly("org.projectlombok:lombok")
		annotationProcessor("org.projectlombok:lombok")
		testImplementation("org.testcontainers:testcontainers:1.21.1")
		testImplementation("org.testcontainers:junit-jupiter:1.21.1")
		testImplementation("org.springframework.boot:spring-boot-starter-data-redis")
		testImplementation(testFixtures(project(":gatekeeper:gatekeeper-core")))
	}
}

project(":gatekeeper:gatekeeper-mysql") {
	dependencies {
		api(project(":gatekeeper:gatekeeper-core"))
		implementation("org.springframework.boot:spring-boot-starter")
		testImplementation("org.springframework.boot:spring-boot-starter-test")
		compileOnly("org.projectlombok:lombok")
		annotationProcessor("org.projectlombok:lombok")
		testImplementation("org.testcontainers:testcontainers:1.21.1")
		testImplementation("org.testcontainers:junit-jupiter:1.21.1")
		testImplementation(testFixtures(project(":gatekeeper:gatekeeper-core")))
	}
}

project(":gatekeeper:gatekeeper-mysql-reactive") {
	dependencies {
		api(project(":gatekeeper:gatekeeper-core"))
		implementation("org.springframework.boot:spring-boot-starter")
		testImplementation("org.springframework.boot:spring-boot-starter-test")
		compileOnly("org.projectlombok:lombok")
		annotationProcessor("org.projectlombok:lombok")
		testImplementation("org.testcontainers:testcontainers:1.21.1")
		testImplementation("org.testcontainers:junit-jupiter:1.21.1")
		testImplementation("io.projectreactor:reactor-test")
		testImplementation(testFixtures(project(":gatekeeper:gatekeeper-core")))
	}
}
