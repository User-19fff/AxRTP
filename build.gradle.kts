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
    implementation("com.artillexstudios.axapi:axapi:1.4.513:all")

    zap("com.github.Anon8281:UniversalScheduler:0.1.6")
    zap("io.papermc:paperlib:1.0.7")

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
    libsFolder = "libs"
    relocationPrefix = "net.coma112.axrtp.libs"

    repositories { includeProjectRepositories() }

    relocate("com.github.Anon8281.universalScheduler", "universalScheduler")
    relocate("io.papermc.lib", "paperlib")
    relocate("com.artillexstudios.axapi", "axapi")
}