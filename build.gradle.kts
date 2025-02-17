plugins {
    java
    id("com.gradleup.shadow") version "8.3.2"
    id("io.github.revxrsal.zapper") version "1.0.2"
    id("io.freefair.lombok") version "8.11"
}

group = "net.coma112"
version = "1.0.0"

repositories {
    mavenCentral()
    maven("https://repo.papermc.io/repository/maven-public/")
    maven("https://oss.sonatype.org/content/groups/public/")
    maven("https://jitpack.io")
    maven("https://repo.extendedclip.com/content/repositories/placeholderapi/")
    maven("https://maven.enginehub.org/repo/")
}

dependencies {
    implementation("io.github.revxrsal:lamp.bukkit:4.0.0-beta.23") {
        exclude(module = "lamp.common")
        exclude(module = "lamp.brigadier")
    }

    zap("io.github.revxrsal:lamp.common:4.0.0-beta.23")
    zap("io.github.revxrsal:lamp.brigadier:4.0.0-beta.23")
    zap("com.github.Anon8281:UniversalScheduler:0.1.6")
    zap("io.papermc:paperlib:1.0.7")

    compileOnly("io.papermc.paper:paper-api:1.21-R0.1-SNAPSHOT")
    compileOnly("org.projectlombok:lombok:1.18.36")
    compileOnly("me.clip:placeholderapi:2.11.6")
    compileOnly("com.sk89q.worldguard:worldguard-bukkit:7.0.13")
    compileOnly("com.github.MilkBowl:VaultAPI:1.7.1") {
        exclude(module = "org.bukkit.bukkit")
    }
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(21))
    }
}

tasks.compileJava {
    options.compilerArgs.add("-parameters")
}

zapper {
    libsFolder = "libs"
    relocationPrefix = "net.coma112.axrtp.libraries"

    repositories { includeProjectRepositories() }

    relocate("com.github.Anon8281.universalScheduler", "universalScheduler")
    relocate("io.papermc.lib", "paperlib")
}

tasks.shadowJar {
    exclude("lamp.lamp_pt.properties")
    exclude("lamp.lamp_en.properties")
    exclude("lamp.lamp_it.properties")
    exclude("lamp.lamp_fr.properties")
    exclude("lamp.lamp-bukkit_pt.properties")
    exclude("lamp.lamp-bukkit_it.properties")
    exclude("lamp.lamp-bukkit_fr.properties")
    exclude("lamp.lamp-bukkit_en.properties")
}