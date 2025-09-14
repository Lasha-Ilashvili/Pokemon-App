plugins {
    // Android
    alias(libs.plugins.android.application) apply false

    // Kotlin
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.kotlin.compose) apply false

    // Dependency Injection
    alias(libs.plugins.hilt) apply false

    // Code Generation
    alias(libs.plugins.ksp) apply false
}