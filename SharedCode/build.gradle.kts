import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget

plugins {
    kotlin("multiplatform")
    id ("org.jetbrains.kotlin.plugin.serialization") version "1.3.50" apply true
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

    jvm("android")

    val ktor_client = "1.2.4"
    val kotlin_coroutines = "1.3.0"
    val kotlin_serialization = "0.12.0"
    val serialization_version = "0.13.0"

    sourceSets["commonMain"].dependencies {
        implementation("org.jetbrains.kotlin:kotlin-stdlib-common")
        implementation("io.ktor:ktor-client-core:${ktor_client}")
        implementation("io.ktor:ktor-client-json:${ktor_client}")
        implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core-common:${kotlin_coroutines}")
        implementation("org.jetbrains.kotlinx:kotlinx-serialization-runtime-common:${kotlin_serialization}")
        implementation ("org.jetbrains.kotlinx:kotlinx-serialization-runtime-common:$serialization_version")
    }

    sourceSets["androidMain"].dependencies {
        implementation("org.jetbrains.kotlin:kotlin-stdlib")
        implementation("io.ktor:ktor-client-core-jvm:${ktor_client}")
        implementation("io.ktor:ktor-client-json-jvm:${ktor_client}")
        implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:${kotlin_coroutines}")
        implementation("org.jetbrains.kotlinx:kotlinx-serialization-runtime:${serialization_version}")
    }
    sourceSets["iosMain"].dependencies {
        implementation("io.ktor:ktor-client-core-native:${ktor_client}")
        implementation("io.ktor:ktor-client-ios:${ktor_client}")
        implementation("io.ktor:ktor-client-json-native:${ktor_client}")
        implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core-native:${kotlin_coroutines}")
        implementation("org.jetbrains.kotlinx:kotlinx-serialization-runtime-native:${serialization_version}")
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
