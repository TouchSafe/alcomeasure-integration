// NOTE: The following is the way to apply a plugin that is publish locally:
buildscript {
	repositories {
		dependencies {
			classpath("com.github.evanbennett:i18n-gradle-plugin:2.0.0-SNAPSHOT")
			classpath("com.github.evanbennett:sql-gradle-plugin:2.0.0-SNAPSHOT")
			classpath("com.github.evanbennett:pgmapper-gradle-plugin:2.0.0-SNAPSHOT")
		}
	}
}
apply(plugin = "com.github.evanbennett.i18n")
apply(plugin = "com.github.evanbennett.sql")
apply(plugin = "com.github.evanbennett.pgmapper")

plugins {
	`kotlin-dsl`
	`maven-publish`
	application
	id("com.github.johnrengelman.shadow") version "5.1.0"
}

group = "au.com.touchsafe"
version = "1.0.0-SNAPSHOT"
description = "TouchSafe Alcohol Testing Module."

application {
	mainClassName = "io.ktor.server.netty.EngineMain"
}

repositories {
	jcenter()
	mavenCentral()
	mavenLocal()
	maven { url = uri("https://kotlin.bintray.com/ktor") }
}

val arrowVersion = "0.10.0"
val baseCoreVersion = "2.0.0-SNAPSHOT"
val baseModuleVersion = "2.0.0-SNAPSHOT"
val jacksonVersion = "2.9.8"
val jasyncSqlVersion = "1.0.7"
val ktorVersion = "1.2.5"
val logbackVersion = "1.2.3"
val nettyTcnativeVersion = "2.0.26.Final"
val organisationsAndPeopleVersion = "1.0.0-SNAPSHOT"

dependencies {
	implementation(kotlin("stdlib"))
	implementation("au.com.touchsafe", "organisations-and-people", organisationsAndPeopleVersion)
	implementation("com.github.evanbennett", "base-core", baseCoreVersion)
	implementation("com.github.evanbennett", "base-module", baseModuleVersion)
	implementation("com.github.jasync-sql", "jasync-postgresql", jasyncSqlVersion)
	implementation("com.fasterxml.jackson.core", "jackson-databind", jacksonVersion)
	implementation("io.arrow-kt", "arrow-core", arrowVersion)
	implementation("io.ktor", "ktor-auth", ktorVersion)
	implementation("io.ktor", "ktor-server-netty", ktorVersion)
	implementation("io.ktor", "ktor-websockets", ktorVersion)
	implementation("io.netty", "netty-tcnative", nettyTcnativeVersion, classifier = "windows-x86_64")
	implementation("io.netty", "netty-tcnative-boringssl-static", nettyTcnativeVersion, classifier = "windows-x86_64")

	runtime("ch.qos.logback", "logback-classic", logbackVersion) // TODO: Work out how to implement logging without any warnings.

	"sql"("au.com.touchsafe", "organisations-and-people", organisationsAndPeopleVersion, classifier = com.github.evanbennett.gradle.sql.SqlPlugin.SQL_STRING, ext = "zip")
	"sql"("com.github.evanbennett", "base-core", baseCoreVersion, classifier = com.github.evanbennett.gradle.sql.SqlPlugin.SQL_STRING, ext = "zip")
	"sql"("com.github.evanbennett", "base-module", baseModuleVersion, classifier = com.github.evanbennett.gradle.sql.SqlPlugin.SQL_STRING, ext = "zip")
}

java {
	sourceCompatibility = JavaVersion.VERSION_11
	targetCompatibility = JavaVersion.VERSION_11
}
tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
	kotlinOptions.jvmTarget = "1.11"
}

configure<com.github.evanbennett.gradle.pgmapper.PgmapperPluginExtension> {
	projectPackage.set("$group.alcohol_testing")
	sqlReplacements.set(setOf(
			com.github.evanbennett.gradle.pgmapper.SqlReplacementString("base-module", databaseName.get()),
			com.github.evanbennett.gradle.pgmapper.SqlReplacementString("ModuleGroup", generatedSqlGroup.get()),
			com.github.evanbennett.gradle.pgmapper.SqlReplacementString("ModuleUser", generatedSqlUser.get()),
			com.github.evanbennett.gradle.pgmapper.SqlReplacementString("organisations-and-people", databaseName.get()),
			com.github.evanbennett.gradle.pgmapper.SqlReplacementString("OrganisationsAndPeopleGroup", generatedSqlGroup.get()),
			com.github.evanbennett.gradle.pgmapper.SqlReplacementString("OrganisationsAndPeopleUser", generatedSqlUser.get())
	))
	parentSqlPackages.addAll(
			com.github.evanbennett.gradle.pgmapper.ParentSqlPackage("com.github.evanbennet.core", "_core_system_setup.sql"),
			com.github.evanbennett.gradle.pgmapper.ParentSqlPackage("com.github.evanbennett.module", "module_"),
			com.github.evanbennett.gradle.pgmapper.ParentSqlPackage("au.com.touchsafe.organisations-and-people", "}organisations_and_people_")
	)
}

tasks.withType<com.github.evanbennett.gradle.i18n.CheckForDuplicateKeysAcrossFamiliesTask> {
	ignoreMessages.set(setOf(com.github.evanbennett.gradle.i18n.I18nPlugin.IgnoreMessages("""au\.com\.touchsafe\.$name\.PermissionsValues""", "")))
}

val generateJks = task("generateJks", JavaExec::class) {
	classpath = sourceSets["main"].runtimeClasspath
	main = "com.github.evanbennett.module.CertificateGenerator"
}
getTasksByName("run", false).first().dependsOn(generateJks)

/*
	mappings in Universal ++= Seq(file("conf/generated.keystore") -> "bin/conf/generated.keystore"),
	mappings in Universal ++= ((SqlKeys.dependentSources in Sql).value ++ (unmanagedSources in Sql).value ++ (managedSources in Sql).value).map { originalSqlFile =>
		val sqlReplacements = PgMapperKeys.sqlReplacements.value
		var contents = IO.read(originalSqlFile)
		sqlReplacements.foreach(sqlReplacement => contents = contents.replaceAll(sqlReplacement.source, sqlReplacement.replacement))
		val tempSqlFile = IO.temporaryDirectory / SQL_STRING / originalSqlFile.getName
		IO.write(tempSqlFile, contents)
		tempSqlFile -> (SQL_STRING + "/" + originalSqlFile.getName)
	}
 */