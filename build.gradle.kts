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
	`maven-publish`
	application
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

val accessControlCommonVersion = "1.0.0-SNAPSHOT"
val arrowVersion = "0.10.5"
val baseCoreVersion = "2.0.0-SNAPSHOT"
val baseModuleVersion = "2.0.0-SNAPSHOT"
val jacksonVersion = "2.10.2"
val jasyncSqlVersion = "1.0.7"
val ktorVersion = "1.3.2"
val logbackVersion = "1.2.3"
val nettyTcnativeVersion = "2.0.26.Final"
val organisationsAndPeopleVersion = "1.0.0-SNAPSHOT"

dependencies {
	implementation(kotlin("stdlib"))
	implementation("au.com.touchsafe", "access-control-common", accessControlCommonVersion)
	implementation("au.com.touchsafe", "organisations-and-people", organisationsAndPeopleVersion)
	implementation("com.fasterxml.jackson.core", "jackson-databind", jacksonVersion)
	implementation("com.github.evanbennett", "base-core", baseCoreVersion)
	implementation("com.github.evanbennett", "base-module", baseModuleVersion)
	implementation("com.github.jasync-sql", "jasync-postgresql", jasyncSqlVersion)
	implementation("io.arrow-kt", "arrow-core", arrowVersion)
	implementation("io.ktor", "ktor-auth", ktorVersion)
	implementation("io.ktor", "ktor-server-netty", ktorVersion)
	implementation("io.ktor", "ktor-websockets", ktorVersion)
	implementation("io.netty", "netty-tcnative", nettyTcnativeVersion, classifier = "windows-x86_64")
	implementation("io.netty", "netty-tcnative-boringssl-static", nettyTcnativeVersion, classifier = "windows-x86_64")

	runtimeOnly("ch.qos.logback", "logback-classic", logbackVersion)

	"sql"("au.com.touchsafe", "access-control-common", accessControlCommonVersion, classifier = com.github.evanbennett.gradle.sql.SqlPlugin.SQL_STRING, ext = "zip")
	"sql"("au.com.touchsafe", "organisations-and-people", organisationsAndPeopleVersion, classifier = com.github.evanbennett.gradle.sql.SqlPlugin.SQL_STRING, ext = "zip")
	"sql"("com.github.evanbennett", "base-core", baseCoreVersion, classifier = com.github.evanbennett.gradle.sql.SqlPlugin.SQL_STRING, ext = "zip")
	"sql"("com.github.evanbennett", "base-module", baseModuleVersion, classifier = com.github.evanbennett.gradle.sql.SqlPlugin.SQL_STRING, ext = "zip")
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
	kotlinOptions.jvmTarget = "11"
}

configure<com.github.evanbennett.gradle.pgmapper.PgmapperPluginExtension> {
	projectPackage.set("$group.alcohol_testing")
	sqlReplacements.set(setOf(
			com.github.evanbennett.gradle.pgmapper.SqlReplacementString("base-module", databaseName.get()),
			com.github.evanbennett.gradle.pgmapper.SqlReplacementString("ModuleGroup", generatedSqlGroup.get()),
			com.github.evanbennett.gradle.pgmapper.SqlReplacementString("ModuleUser", generatedSqlUser.get()),
			com.github.evanbennett.gradle.pgmapper.SqlReplacementString("organisations-and-people", databaseName.get()),
			com.github.evanbennett.gradle.pgmapper.SqlReplacementString("OrganisationsAndPeopleGroup", generatedSqlGroup.get()),
			com.github.evanbennett.gradle.pgmapper.SqlReplacementString("OrganisationsAndPeopleUser", generatedSqlUser.get()),
			com.github.evanbennett.gradle.pgmapper.SqlReplacementString("access-control-common", databaseName.get()),
			com.github.evanbennett.gradle.pgmapper.SqlReplacementString("AccessControlCommonGroup", generatedSqlGroup.get()),
			com.github.evanbennett.gradle.pgmapper.SqlReplacementString("AccessControlCommonUser", generatedSqlUser.get()),
			com.github.evanbennett.gradle.pgmapper.SqlReplacementString(" Configuration ", " ${deploymentSchema.get()} ")
	))
	parentSqlPackages.addAll(
			com.github.evanbennett.gradle.pgmapper.ParentSqlPackage("com.github.evanbennett.core", "_core_system_setup.sql"),
			com.github.evanbennett.gradle.pgmapper.ParentSqlPackage("com.github.evanbennett.module", "module_"),
			com.github.evanbennett.gradle.pgmapper.ParentSqlPackage("au.com.touchsafe.organisations-and-people", "}organisations_and_people_"),
			com.github.evanbennett.gradle.pgmapper.ParentSqlPackage("au.com.touchsafe.access-control-common", "}access_control_common_")
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
