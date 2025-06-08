plugins {
    id("java")
    id("io.papermc.paperweight.userdev") version "2.0.0-beta.16"
    id("net.minecrell.plugin-yml.bukkit") version "0.6.0"
}

bukkit {
    main = "ru.sidey383.worlgenerator.WorldGenerator"
    generateLibrariesJson = true
    apiVersion = "1.21"
    authors = listOf("sidey383")
    prefix = rootProject.name
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
    sourceCompatibility = JavaVersion.VERSION_22
    targetCompatibility = JavaVersion.VERSION_22
    toolchain {
        languageVersion = JavaLanguageVersion.of(22)
    }
}

group = "ru.sidey383"
version = "1.0-SNAPSHOT"

repositories {
    maven("https://repo.papermc.io/repository/maven-public/")
    mavenCentral()
}

dependencies {
    compileOnly("io.papermc.paper:paper-api:1.21.4-R0.1-SNAPSHOT")
    paperweight.paperDevBundle("1.21.4-R0.1-SNAPSHOT")
}

tasks {
    assemble {
        dependsOn(reobfJar)
    }

    withType<JavaCompile> {
        options.encoding = "UTF-8"
        options.compilerArgs.add("-parameters")
    }

    jar {
        archiveFileName.set("${project.name}-${project.version}.jar")
        duplicatesStrategy = DuplicatesStrategy.EXCLUDE
    }
}