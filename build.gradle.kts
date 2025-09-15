plugins {
    kotlin("jvm") version "1.9.20"
    id("io.ktor.plugin") version "2.3.7"
    application
}

group = "com.trace"
version = "0.0.1"

application {
    mainClass.set("com.trace.payment.ApplicationKt")
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("io.ktor:ktor-server-core:2.3.7")
    implementation("io.ktor:ktor-server-netty:2.3.7")
    implementation("io.ktor:ktor-server-content-negotiation:2.3.7")
    implementation("io.ktor:ktor-serialization-jackson:2.3.7")
    implementation("org.litote.kmongo:kmongo-coroutine:4.11.0")
    
    testImplementation("io.ktor:ktor-server-tests:2.3.7")
    testImplementation("org.jetbrains.kotlin:kotlin-test:1.9.20")
}
