import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget

plugins {
    kotlin("multiplatform")
    id("org.jetbrains.kotlin.plugin.serialization") version "1.3.61" apply true
    id("com.squareup.sqldelight") apply true
}

sqldelight {
    database("ArticlesDb") {
        packageName = "com.olegel.sqldelight.article"
    }
}

kotlin {
    //select iOS target platform depending on the Xcode environment variables
    val iOSTarget: (String, KotlinNativeTarget.() -> Unit) -> KotlinNativeTarget =
        if (System.getenv("SDK_NAME")?.startsWith("iphoneos") == true)
            ::iosArm64
        else
            ::iosX64

    iOSTarget("ios") {
        binaries {
            framework {
                baseName = "SharedCode"
            }
        }
    }
    jvm("android") {
        val main by compilations.getting {
            kotlinOptions {
                // Setup the Kotlin compiler options for the 'main' compilation:
                jvmTarget = "1.8"
            }

            compileKotlinTask // get the Kotlin task 'compileKotlinJvm'
            output // get the main compilation output
        }
    }

    val ktor_client = "1.3.2"
    val kotlin_coroutines = "1.3.4"
    val serialization_version = "0.20.0"
    val kodein_version = "6.5.1"
    val sqldelight_version = "1.2.2"

    sourceSets["commonMain"].dependencies {
        implementation("org.jetbrains.kotlin:kotlin-stdlib-common")
        implementation("io.ktor:ktor-client-core:${ktor_client}")
        implementation("io.ktor:ktor-client-json:${ktor_client}")
        implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core-common:${kotlin_coroutines}")
        implementation("org.jetbrains.kotlinx:kotlinx-serialization-runtime-common:${serialization_version}")
        implementation("org.jetbrains.kotlinx:kotlinx-serialization-runtime-common:$serialization_version")
        implementation("org.kodein.di:kodein-di-core:${kodein_version}")
        implementation("org.kodein.di:kodein-di-erased:${kodein_version}")
        implementation("com.squareup.sqldelight:runtime:${sqldelight_version}")
        implementation("com.squareup.sqldelight:coroutines-extensions:${sqldelight_version}")
    }
    sourceSets["commonTest"].dependencies {
        implementation("org.jetbrains.kotlin:kotlin-test-common")
        implementation("org.jetbrains.kotlin:kotlin-test-annotations-common")
    }
    sourceSets["androidMain"].dependencies {
        implementation("org.jetbrains.kotlin:kotlin-stdlib")
        implementation("io.ktor:ktor-client-core-jvm:${ktor_client}")
        implementation("io.ktor:ktor-client-json-jvm:${ktor_client}")
        implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:${kotlin_coroutines}")
        implementation("org.jetbrains.kotlinx:kotlinx-serialization-runtime:${serialization_version}")
        implementation("com.squareup.sqldelight:android-driver:${sqldelight_version}")
        implementation("com.squareup.sqldelight:coroutines-extensions-jvm:${sqldelight_version}")
    }
    sourceSets["iosMain"].dependencies {
        implementation("io.ktor:ktor-client-core-native:${ktor_client}")
        implementation("io.ktor:ktor-client-ios:${ktor_client}")
        implementation("io.ktor:ktor-client-json-native:${ktor_client}")
        implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core-native:${kotlin_coroutines}")
        implementation("org.jetbrains.kotlinx:kotlinx-serialization-runtime-native:${serialization_version}")
        implementation("com.squareup.sqldelight:native-driver:${sqldelight_version}")
    }

}

val packForXcode by tasks.creating(Sync::class) {
    group = "build"

    //selecting the right configuration for the iOS framework depending on the Xcode environment variables
    val mode = System.getenv("CONFIGURATION") ?: "DEBUG"
    val framework = kotlin.targets.getByName<KotlinNativeTarget>("ios").binaries.getFramework(mode)

    inputs.property("mode", mode)
    dependsOn(framework.linkTask)

    val targetDir = File(buildDir, "xcode-frameworks")
    from({ framework.outputDirectory })
    into(targetDir)

    doLast {
        val gradlew = File(targetDir, "gradlew")
        gradlew.writeText("#!/bin/bash\nexport 'JAVA_HOME=${System.getProperty("java.home")}'\ncd '${rootProject.rootDir}'\n./gradlew \$@\n")
        gradlew.setExecutable(true)
    }
}

tasks.getByName("build").dependsOn(packForXcode)
