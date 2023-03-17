plugins {
    id("java")
    id("maven-publish")
}

val release = System.getenv("GRADLE_RELEASE").equals("true", ignoreCase = true)
group = "com.oskarsmc"
version = "1.0.0"

if (!release) {
    version = "$version-SNAPSHOT"
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.jetbrains:annotations:23.1.0")
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.9.2")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.9.2")
    implementation("com.google.code.gson:gson:2.10.1")
}

tasks.getByName<Test>("test") {
    useJUnitPlatform()
}

publishing {
    publications {
        create<MavenPublication>("maven") {
            groupId = project.group as String?
            artifactId = project.name
            version = project.version as String?

            from(components["java"])
        }
    }
    repositories {
        maven {
            name = "oskarsmc-repository"
            val releasesRepoUrl = uri("https://repository.oskarsmc.com/releases")
            val snapshotsRepoUrl = uri("https://repository.oskarsmc.com/snapshots")
            url = if (version.toString().endsWith("SNAPSHOT")) snapshotsRepoUrl else releasesRepoUrl
            credentials {
                username = System.getenv("MAVEN_USERNAME")
                password = System.getenv("MAVEN_SECRET")
            }
            authentication {
                create<BasicAuthentication>("basic")
            }
        }
    }
}

java {
    withJavadocJar()
    withSourcesJar()
}