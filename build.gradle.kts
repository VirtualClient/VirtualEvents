import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.7.0"
    id("maven-publish")
}

group = "gg.virtualclient"
version = "1.0.1-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(kotlin("test"))
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}

configure<PublishingExtension> {
    repositories {
        maven {
            name = "virtualclientRepository"
            credentials(PasswordCredentials::class)
            url = uri("https://repo.virtualclient.gg/artifactory/virtualclient-public/")
        }
    }
    publications {
        create<MavenPublication>("maven") {
            groupId = "gg.virtualclient"
            artifactId = "VirtualEvents"
            version = "1.0.3-SNAPSHOT"
            from(components["java"])
        }
    }
}



tasks.test {
    useJUnitPlatform()
}