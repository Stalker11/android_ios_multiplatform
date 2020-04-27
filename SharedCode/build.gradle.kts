import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget

plugins {
    kotlin("multiplatform")
    id("org.jetbrains.kotlin.plugin.serialization") version "1.3.61" apply true
    id("com.squareup.sqldelight") apply true
    id("com.android.library") apply true
}

sqldelight {
    database("ArticlesDb") {
        packageName = "com.olegel.sqldelight.article"
    }
}
android {
    compileSdkVersion(29)
    buildToolsVersion("29.0.2")

    defaultConfig {
        minSdkVersion(21)
        targetSdkVersion(29)
    }

    // By default the android gradle plugin expects to find the kotlin source files in
    // the folder `main` and the test in the folder `test`. This is to be able place
    // the source code files inside androidMain and androidTest folders
    sourceSets {
        getByName("main") {
            manifest.srcFile("src/androidMain/AndroidManifest.xml")
            java.srcDirs(file("src/androidMain/kotlin"))
            res.srcDirs(file("src/androidMain/res"))
        }
        getByName("test") {
            java.srcDirs(file("src/androidTest/kotlin"))
            res.srcDirs(file("src/androidTest/res"))
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
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

    targets {
        targetFromPreset(presets.getByName("android"), "android")
    }
    val ktor_client = "1.3.2"
    val kotlin_coroutines = "1.3.4"
    val serialization_version = "0.20.0"
    val kodein_version = "6.5.1"
    val sqldelight_version = "1.3.0"
    val moko_mvvm_version = "0.6.0"

    sourceSets["commonMain"].dependencies {
        implementation("org.jetbrains.kotlin:kotlin-stdlib-common")
        //Ktor client
        implementation("io.ktor:ktor-client-core:${ktor_client}")
        implementation("io.ktor:ktor-client-json:${ktor_client}")
        //Coroutines
        implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core-common:${kotlin_coroutines}")
        //Kotlin serialization
        implementation("org.jetbrains.kotlinx:kotlinx-serialization-runtime-common:${serialization_version}")
        //Kodein DI
        implementation("org.kodein.di:kodein-di-core:${kodein_version}")
        implementation("org.kodein.di:kodein-di-erased:${kodein_version}")
        //SQLDelight
        implementation("com.squareup.sqldelight:runtime:${sqldelight_version}")
        implementation("com.squareup.sqldelight:coroutines-extensions:${sqldelight_version}")
        // MOKO - MVVM
        implementation ("dev.icerock.moko:mvvm:$moko_mvvm_version")
        implementation ("androidx.lifecycle:lifecycle-viewmodel:2.2.0")
    }
    sourceSets["commonTest"].dependencies {
        implementation("org.jetbrains.kotlin:kotlin-test-common")
        implementation("org.jetbrains.kotlin:kotlin-test-annotations-common")
    }
    sourceSets["androidMain"].dependencies {
        implementation("org.jetbrains.kotlin:kotlin-stdlib")
        //Ktor client
        implementation("io.ktor:ktor-client-core-jvm:${ktor_client}")
        implementation("io.ktor:ktor-client-json-jvm:${ktor_client}")
        implementation("io.ktor:ktor-client-android:${ktor_client}")
        //Coroutines
        implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:${kotlin_coroutines}")
        //Kotlin serialization
        implementation("org.jetbrains.kotlinx:kotlinx-serialization-runtime:${serialization_version}")
        //SQLDelight
        implementation("com.squareup.sqldelight:android-driver:${sqldelight_version}")
        implementation("com.squareup.sqldelight:coroutines-extensions-jvm:${sqldelight_version}")
    }
    sourceSets["iosMain"].dependencies {
        //Ktor client
        implementation("io.ktor:ktor-client-core-native:${ktor_client}")
        implementation("io.ktor:ktor-client-ios:${ktor_client}")
        implementation("io.ktor:ktor-client-json-native:${ktor_client}")
        //Coroutines
        implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core-native:${kotlin_coroutines}")
        //Kotlin serialization
        implementation("org.jetbrains.kotlinx:kotlinx-serialization-runtime-native:${serialization_version}")
        //SQLDelight
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
