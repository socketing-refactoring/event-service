import com.diffplug.spotless.extra.wtp.EclipseWtpFormatterStep
import org.asciidoctor.gradle.jvm.AsciidoctorTask
import org.springframework.boot.gradle.tasks.bundling.BootJar

plugins {
    id("java")
    id("org.springframework.boot") version "3.4.3"
    id("io.spring.dependency-management") version "1.1.7"
    id("com.diffplug.spotless") version "7.0.2"
    id("checkstyle")
    id("org.ec4j.editorconfig") version "0.1.0"
    id("org.asciidoctor.jvm.convert") version "4.0.4"
    id("org.ajoberstar.git-publish") version "4.2.0"
    id("com.epages.restdocs-api-spec") version "0.18.4"
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
        targetExclude("**/ErrorCode.java")

        removeUnusedImports()
        importOrder()
//        googleJavaFormat().aosp()
        eclipse().configFile("eclipse-java-google-style.xml")
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

configurations {
    create("asciidoctorExt")
}

repositories {
    mavenCentral()
}

val springCloudVersion = "2024.0.0"
val snippetsDir = file("build/generated-snippets")
val asciidoctorOutputDir = layout.buildDirectory.dir("docs/asciidoc/event-service")

dependencies {
    implementation("org.springframework.boot:spring-boot-starter")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("org.springframework.cloud:spring-cloud-starter-openfeign")
//    implementation("org.springframework.cloud:spring-cloud-starter-config")
    implementation("org.springframework.cloud:spring-cloud-starter-netflix-eureka-client")
    implementation(platform("org.springframework.cloud:spring-cloud-dependencies:$springCloudVersion"))
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
//    implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.8.5")
    implementation("org.postgresql:postgresql:42.6.2")
    implementation("org.mindrot:jbcrypt:0.4")
    implementation("io.micrometer:micrometer-registry-prometheus:1.15.0-M2")
    compileOnly("org.projectlombok:lombok")
    annotationProcessor("org.projectlombok:lombok")

    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.junit.jupiter:junit-jupiter:5.10.1")
    testImplementation("net.bytebuddy:byte-buddy-agent:1.15.11")
    testImplementation("org.springframework.restdocs:spring-restdocs-mockmvc")
    testImplementation("com.epages:restdocs-api-spec-mockmvc:0.18.4")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
    testCompileOnly("org.projectlombok:lombok")
    testAnnotationProcessor("org.projectlombok:lombok")

    // For Gradle
    add("asciidoctorExt", "org.springframework.restdocs:spring-restdocs-asciidoctor")
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

// Test Task Configuration (Spring Rest Docs)
tasks.withType<Test> {
    useJUnitPlatform()

    // Remove JVM warning message
    jvmArgs = listOf("-Xshare:off")
    doFirst {
        val agentJar =
            configurations.testRuntimeClasspath
                .get()
                .files
                .find { it.name.contains("byte-buddy-agent") }
                ?: throw GradleException("Byte Buddy Agent JAR not found")

        jvmArgs("-javaagent:${agentJar.absolutePath}")
    }

    outputs.dir(snippetsDir)
}

val asciidoctorTask =
    tasks.named<AsciidoctorTask>("asciidoctor") {
        inputs.dir(snippetsDir)
        configurations("asciidoctorExt")
        dependsOn(tasks.test)

        sources(
            delegateClosureOf<PatternSet> {
                include("index.adoc")
            },
        )

        baseDirFollowsSourceFile() // required to include adoc into index.adoc
        setOutputDir(layout.buildDirectory.dir("docs/asciidoc/event-service"))
    }

// Packaging Jar
tasks.named<BootJar>("bootJar") {
    archiveFileName.set("event-service.jar")

    from(asciidoctorTask.map { it.outputDir }) {
        into("static/docs/asciidoc")
    }

    from(layout.buildDirectory.dir("api-spec")) {
        into("static/docs/openapi")
    }

    dependsOn(asciidoctorTask, tasks.named("openapi3"))
}

tasks.named<Jar>("jar") {
    enabled = false
}

// Publishing Document
tasks.named("gitPublishCopy") {
    dependsOn("asciidoctor")
}

gitPublish {
    repoUri.set("git@github.com:socketing-refactoring/socketing-refactoring.github.io.git")
    branch.set("main")
    contents {
        from(asciidoctorOutputDir) {
            into("docs/event-service")
        }

        preserve {
            include("**")
        }
    }
    commitMessage.set("Update Event Service API documentation")
}

openapi3 {
    this.setServer("https://api.socketing.jeein.xyz")
    title = "My API"
    description = "My API description"
    version = "0.1.0"
    format = "json" // or yml
}
