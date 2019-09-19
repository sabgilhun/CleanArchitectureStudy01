import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id(BuildPlugins.androidLibrary)
    id(BuildPlugins.kotlinAndroid)
    id(BuildPlugins.kotlinAndroidExtensions)
    id(BuildPlugins.kotlinKapt)
}

android {
    compileSdkVersion(Versions.compileSdk)
    buildToolsVersion(Versions.buildTools)

    defaultConfig {
        minSdkVersion(Versions.minSdk)
        targetSdkVersion(Versions.targetSdk)
        versionCode = Releases.versionCode
        versionName = Releases.versionName
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
        }
    }

    dataBinding {
        isEnabled = true
    }
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}

dependencies {
    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))
    implementation(Libraries.kotlin)
    implementation(Libraries.appcompat)
    implementation(Libraries.material)
    implementation(Libraries.ktxCore)
    implementation(Libraries.constraintLayout)

    // test
    testImplementation(Libraries.junit)
    androidTestImplementation(Libraries.androidJunit)
    androidTestImplementation(Libraries.espresso)

    // koin
    implementation(Libraries.koinCore)
    implementation(Libraries.koinAndroid)
    implementation(Libraries.koinViewModel)

    // retrofit
    implementation(Libraries.retrofit)
    implementation(Libraries.gsonConverter)
    implementation(Libraries.rxjavaAdapter)
    implementation(Libraries.loggingInterceptor)

    // rxJava
    implementation(Libraries.rxJava)
    implementation(Libraries.rxkotlin)

    // timber
    implementation(Libraries.timber)

    // glide
    implementation(Libraries.glide)
    kapt(Libraries.glideCompiler)

    // anko
    implementation(Libraries.anko)

    // tedPermission
    implementation(Libraries.tedPermission)
}
