import com.diffplug.spotless.extra.wtp.EclipseWtpFormatterStep

plugins {
    id("java")
    id("org.springframework.boot") version "3.4.3"
    id("io.spring.dependency-management") version "1.1.7"
    id("com.diffplug.spotless") version "7.0.2"
    id("checkstyle")
    id("org.ec4j.editorconfig") version "0.1.0"
}

group = "com.jeein"
version = "0.0.1-SNAPSHOT"

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}

spotless {
    java {
        removeUnusedImports()
        importOrder()
        googleJavaFormat().aosp()
    }

    kotlinGradle {
        target("**/*.gradle.kts", "*.gradle.kts")

        ktlint()
        trimTrailingWhitespace()
        endWithNewline()
    }

    yaml {
        target("**/*.yml", "**/*.yaml")
        jackson()
            .yamlFeature("ALWAYS_QUOTE_NUMBERS_AS_STRINGS", false)
            .yamlFeature("WRITE_DOC_START_MARKER", false)
            .yamlFeature("INDENT_ARRAYS_WITH_INDICATOR", true)
    }

    format("xml") {
        target("**/*.xml")

        eclipseWtp(EclipseWtpFormatterStep.XML)
    }
}

checkstyle {
    toolVersion = "10.23.0"
}

editorconfig {
    excludes = listOf("build")
}

repositories {
    mavenCentral()
}

val springCloudVersion = "2024.0.0"

dependencies {
    implementation("org.springframework.boot:spring-boot-starter")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.8.5")
    implementation("org.springframework.cloud:spring-cloud-starter-openfeign")
    implementation("org.springframework.cloud:spring-cloud-starter-netflix-eureka-client")
    implementation(platform("org.springframework.cloud:spring-cloud-dependencies:$springCloudVersion"))
    implementation("io.micrometer:micrometer-registry-prometheus:1.15.0-M2")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.postgresql:postgresql:42.6.2")
    compileOnly("org.projectlombok:lombok")
    annotationProcessor("org.projectlombok:lombok")

    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
    testCompileOnly("org.projectlombok:lombok")
    testAnnotationProcessor("org.projectlombok:lombok")

    // For Gradle
    implementation("com.diffplug.spotless:spotless-lib-extra:3.1.1")
}

// CheckStyle Task Configuration
tasks.withType<Checkstyle>().configureEach {
    reports {
        xml.required = false
        html.required = true
    }
}

tasks.named("checkstyleMain") {
    dependsOn("spotlessApply")
}

tasks.named("checkstyleTest") {
    dependsOn("spotlessApply")
}

tasks.named("spotlessApply") {
    dependsOn("editorconfigFormat")
}

tasks.withType<Test> {
    useJUnitPlatform()
}

tasks.jar {
    enabled = false
}

tasks.bootJar {
    archiveFileName.set("event-service.jar")
}
