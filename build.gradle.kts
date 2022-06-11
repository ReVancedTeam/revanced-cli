plugins {
    kotlin("jvm") version "1.6.21"
    id("com.github.johnrengelman.shadow") version "7.1.2"
    java
    `maven-publish`
}

group = "app.revanced"

repositories {
    mavenCentral()
    google()
    mavenLocal()
    maven {
        url = uri("https://maven.pkg.github.com/revanced/multidexlib2")
        credentials {
            username = project.findProperty("gpr.user") as String? ?: System.getenv("GITHUB_ACTOR") // DO NOT CHANGE!
            password = project.findProperty("gpr.key") as String? ?: System.getenv("GITHUB_TOKEN") // DO NOT CHANGE!
        }
    }
    maven {
        url = uri("https://jitpack.io")
    }
}

dependencies {
    implementation("org.jetbrains.kotlin:kotlin-stdlib:1.6.21")
    implementation("app.revanced:revanced-patcher:1.0.0")

    implementation("info.picocli:picocli:4.6.3")
    implementation("com.android.tools.build:apksig:7.2.1")
    implementation("com.github.revanced:jadb:master-SNAPSHOT") // updated fork
    implementation("org.bouncycastle:bcpkix-jdk15on:1.70")
    implementation("org.jetbrains.kotlin:kotlin-reflect:1.6.21")
}

java {
    withSourcesJar()
    withJavadocJar()
}

tasks {
    build {
        dependsOn(shadowJar)
    }
    shadowJar {
        manifest {
            attributes("Main-Class" to "app.revanced.cli.main.MainKt")
            attributes("Implementation-Title" to project.name)
            attributes("Implementation-Version" to project.version)
        }
    }
}

publishing {
    repositories {
        maven {
            name = "GitHubPackages"
            url = uri("https://maven.pkg.github.com/revanced/revanced-cli")
            credentials {
                username = System.getenv("GITHUB_ACTOR")
                password = System.getenv("GITHUB_TOKEN")
            }
        }
    }
    publications {
        register<MavenPublication>("gpr") {
            from(components["java"])
        }
    }
}