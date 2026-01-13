// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.jetbrains.kotlin.android) apply false
    alias(libs.plugins.compose.compiler) apply false

    alias(libs.plugins.hilt) apply false

    id("com.google.devtools.ksp") version "2.3.0" apply false
    id("androidx.room") version "2.8.4" apply false

    id("org.jetbrains.kotlin.plugin.serialization") version "2.3.0" apply false
}

buildscript {
    dependencies {
        classpath(libs.secrets.gradle.plugin)
    }
}