rootProject.name = "KotlinProject"
// enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS") // Disabled to resolve build issue after removing Android

pluginManagement {
    repositories {
        google() // <-- Thêm lại kho của Google
        mavenCentral()
        gradlePluginPortal()
    }
}

dependencyResolutionManagement {
    repositories {
        google() // <-- Thêm lại kho của Google
        mavenCentral()
    }
}

plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "1.0.0"
}

include(":composeApp")
