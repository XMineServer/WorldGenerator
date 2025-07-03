plugins {
    id("java")
    id("net.minecrell.plugin-yml.bukkit") version "0.6.0"
}

bukkit {
    main = "ru.sidey383.worlgenerator.WorldGenerator"
    generateLibrariesJson = true
    apiVersion = "1.21"
    authors = listOf("sidey383")
    prefix = rootProject.name
    foliaSupported = true
    commands {
        register("generateworld") {
            description = "Generate new world"
            usage = "/generateworld <worldname>"
        }
        register("worldmove") {
            description = "Teleport into world"
            usage = "/worldmove <worldname>"
        }
    }
}

java {
    sourceCompatibility = JavaVersion.VERSION_21
    targetCompatibility = JavaVersion.VERSION_21
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}

group = "ru.sidey383"
version = "1.0-SNAPSHOT"

repositories {
    maven("https://repo.papermc.io/repository/maven-public/")
    mavenCentral()
    maven {
        name = "thenextlvlReleases"
        url = uri("https://repo.thenextlvl.net/releases")
    }
}

dependencies {
    compileOnly("io.papermc.paper:paper-api:1.21.4-R0.1-SNAPSHOT")
    implementation("net.thenextlvl:worlds:3.1.1")
}

tasks {
    withType<JavaCompile> {
        options.encoding = "UTF-8"
        options.compilerArgs.add("-parameters")
    }

    jar {
        archiveFileName.set("${project.name}-${project.version}.jar")
        duplicatesStrategy = DuplicatesStrategy.EXCLUDE
    }
}