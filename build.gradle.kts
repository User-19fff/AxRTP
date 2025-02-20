plugins {
    id("java")
    id("com.gradleup.shadow") version("8.3.2")
    id("io.github.revxrsal.zapper") version("1.0.2")
    id("io.freefair.lombok") version("8.11")
}

group = "net.coma112"
version = "1.0.0"

repositories {
    mavenCentral()

    maven("https://repo.papermc.io/repository/maven-public/")
    maven("https://oss.sonatype.org/content/groups/public/")
    maven("https://jitpack.io")
    maven("https://maven.enginehub.org/repo/")
    maven("https://repo.artillex-studios.com/releases")
}

dependencies {
    zap("com.github.Anon8281:UniversalScheduler:0.1.6")
    zap("io.papermc:paperlib:1.0.7")

    compileOnly("com.artillexstudios.axapi:axapi:1.4.513:all")
    compileOnly("io.papermc.paper:paper-api:1.21-R0.1-SNAPSHOT")
    compileOnly("org.projectlombok:lombok:1.18.36")
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

zapper {
    repositories { includeProjectRepositories() }
}

tasks {
    build {
        dependsOn(shadowJar)
    }

    shadowJar {
        relocate("com.github.Anon8281.universalScheduler", "net.coma112.axrtp.libs.scheduler")
        relocate("io.papermc.lib", "net.coma112.axrtp.libs.paperlib")
        relocate("com.artillexstudios.axapi", "net.coma112.axrtp.libs.axapi")
    }
}