pluginManagement {
	repositories {
		gradlePluginPortal()
		mavenLocal()
		maven { url = uri("https://jitpack.io") }
		maven { url = uri("https://kotlin.bintray.com/ktor") }
	}
}

rootProject.name = "alcomeasure-integration"
