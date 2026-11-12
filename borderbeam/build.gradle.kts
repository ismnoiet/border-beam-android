plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
    id("maven-publish")
}

android {
    namespace = "com.ismnoiet.borderbeam"
    compileSdk = 34

    defaultConfig {
        minSdk = 24
        consumerProguardFiles("consumer-rules.pro")
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    kotlinOptions {
        jvmTarget = "17"
    }

    buildFeatures {
        compose = true
    }

    composeOptions {
        // Compose compiler 1.5.10 pairs with Kotlin 1.9.22.
        kotlinCompilerExtensionVersion = "1.5.10"
    }

    publishing {
        singleVariant("release") {
            withSourcesJar()
        }
    }
}

dependencies {
    // Compose is part of the public API (Modifier, Color, Dp), so expose it to consumers.
    api(platform("androidx.compose:compose-bom:2024.02.00"))
    api("androidx.compose.ui:ui")
    api("androidx.compose.foundation:foundation")

    testImplementation("junit:junit:4.13.2")
}

afterEvaluate {
    publishing {
        publications {
            register<MavenPublication>("release") {
                from(components["release"])
                groupId = "com.github.ismnoiet"   // JitPack-style coordinates
                artifactId = "border-beam-android"
                version = "1.0.0"
            }
        }
    }
}
