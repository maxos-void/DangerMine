plugins {
	kotlin("jvm") version "2.3.0"
	id("maven-publish")
	id("com.gradleup.shadow") version "8.3.0"
	id("xyz.jpenilla.run-paper") version "2.3.1"
}

group = "com.github.maxos-void" //"me.maxos.votive"
version = "1.0.4-beta"

repositories {
	mavenCentral()
	maven("https://repo.papermc.io/repository/maven-public/") {
		name = "papermc-repo"
	}
}

dependencies {
	compileOnly("io.papermc.paper:paper-api:1.18.2-R0.1-SNAPSHOT")
	compileOnly("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
	compileOnly("net.raidstone:WorldGuardEvents:1.18.1")
}

tasks {
	runServer {
		// Configure the Minecraft version for our task.
		// This is the only required configuration besides applying the plugin.
		// Your plugin's jar (or shadowJar if present) will be used automatically.
		minecraftVersion("1.18")
	}
}

val targetJavaVersion = 17
java {
	toolchain.languageVersion.set(JavaLanguageVersion.of(targetJavaVersion))
	withSourcesJar()
	withJavadocJar()
}

kotlin {
	jvmToolchain(targetJavaVersion)
}

tasks.build {
	dependsOn("shadowJar")
}

tasks.shadowJar {
	//relocate("kotlin", "me.maxos.votive.lib.kotlin")
	//relocate("org.jetbrains", "me.maxos.votive.lib.jetbrains")
	//relocate("org.intellij", "me.maxos.votive.lib.intellij")

//	relocate("net.raidstone", "me.maxos.votive.lib.raidstone")

//	minimize {
	//	exclude(dependency("org.jetbrains.kotlin:kotlin-stdlib:.*"))
	//	exclude(dependency("org.jetbrains.kotlin:kotlin-stdlib-common:.*"))
	//	exclude(dependency("net.raidstone:.*:.*"))
//	}

	//exclude("META-INF/maven/**")
	//exclude("META-INF/*.kotlin_module")
	//exclude("META-INF/*.kotlin_builtins")
	//exclude("META-INF/versions/**")
	//exclude("**/*.kotlin_metadata")

	archiveClassifier.set("")
}

tasks.processResources {
	val props = mapOf("version" to version)
	inputs.properties(props)
	filteringCharset = "UTF-8"
	filesMatching("plugin.yml") {
		expand(props)
	}
}
