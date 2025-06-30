plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.8.0"
}
rootProject.name = "playground"
include("playground-server")
include("ratelimiter")
include("httpinterface")
include("beanprocessor")
include("functionalconfig")
include("webflux-world")
include("cloud-stream-kafka")
include("rule-engine")
include("fallbackcache")
include("bulkhead")
include("retry")
include("hunit5")
include("annotation-validator")
include("gatekeeper")
include("gatekeeper:gatekeeper-core")
findProject(":gatekeeper:gatekeeper-core")?.name = "gatekeeper-core"
include("gatekeeper:gatekeeper-redisson")
findProject(":gatekeeper:gatekeeper-redisson")?.name = "gatekeeper-redisson"
include("gatekeeper:gatekeeper-mysql")
findProject(":gatekeeper:gatekeeper-mysql")?.name = "gatekeeper-mysql"
include("gatekeeper:gatekeeper-mysql-reactive")
findProject(":gatekeeper:gatekeeper-mysql-reactive")?.name = "gatekeeper-mysql-reactive"
include("gatekeeper:gatekeeper-kotlin")
findProject(":gatekeeper:gatekeeper-kotlin")?.name = "gatekeeper-kotlin"
