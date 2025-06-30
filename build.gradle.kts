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
}

project(":httpinterface") {
	apply(plugin = "org.springframework.boot")
	apply(plugin = "io.spring.dependency-management")
}

project(":beanprocessor") {
	apply(plugin = "org.springframework.boot")
	apply(plugin = "io.spring.dependency-management")
}

project(":functionalconfig") {
}

project(":webflux-world") {
	apply(plugin = "org.springframework.boot")
	apply(plugin = "io.spring.dependency-management")
}

project(":cloud-stream-kafka") {
	apply(plugin = "org.springframework.boot")
	apply(plugin = "io.spring.dependency-management")
	apply(plugin = "com.github.davidmc24.gradle.plugin.avro")
}

project(":rule-engine") {
	apply(plugin = "org.springframework.boot")
	apply(plugin = "io.spring.dependency-management")
}

project(":fallbackcache") {
	apply(plugin = "org.springframework.boot")
	apply(plugin = "io.spring.dependency-management")
}

project(":bulkhead") {
	apply(plugin = "org.springframework.boot")
	apply(plugin = "io.spring.dependency-management")
}

project(":retry") {
	apply(plugin = "org.springframework.boot")
	apply(plugin = "io.spring.dependency-management")
}

project(":annotation-validator") {
	apply(plugin = "org.springframework.boot")
	apply(plugin = "io.spring.dependency-management")
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
}

project(":gatekeeper:gatekeeper-redisson") {
	dependencies {
		api(project(":gatekeeper:gatekeeper-core"))
	}
}

project(":gatekeeper:gatekeeper-mysql") {
	dependencies {
		api(project(":gatekeeper:gatekeeper-core"))
	}
}

project(":gatekeeper:gatekeeper-mysql-reactive") {
	dependencies {
		api(project(":gatekeeper:gatekeeper-core"))
	}
}

project(":gatekeeper:gatekeeper-kotlin") {
	dependencies {
		api(project(":gatekeeper:gatekeeper-core"))
		api(project(":gatekeeper:gatekeeper-mysql"))
		api(project(":gatekeeper:gatekeeper-mysql-reactive"))
		api(project(":gatekeeper:gatekeeper-redisson"))
	}
}
