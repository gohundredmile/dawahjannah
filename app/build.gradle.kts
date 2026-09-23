import com.google.gms.googleservices.GoogleServicesPlugin.MissingGoogleServicesStrategy
import java.security.KeyStore
import java.io.InputStream

plugins {
  alias(libs.plugins.android.application)
  alias(libs.plugins.kotlin.compose)
  alias(libs.plugins.google.devtools.ksp)
  alias(libs.plugins.roborazzi)
  alias(libs.plugins.secrets)
  alias(libs.plugins.google.services)
}

android {
  namespace = "com.example"
  compileSdk { version = release(36) { minorApiLevel = 1 } }

  defaultConfig {
    applicationId = "com.dawahtojannah.app"
    minSdk = 24
    targetSdk = 36
    versionCode = 172
    versionName = "1.7.2"

    testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
  }

  signingConfigs {
    create("release") {
      val keystorePath = System.getenv("KEYSTORE_PATH") ?: if (file("${rootDir}/release.keystore").exists()) "${rootDir}/release.keystore" else "${rootDir}/debug.keystore"
      storeFile = file(keystorePath)
      storePassword = System.getenv("STORE_PASSWORD") ?: if (file("${rootDir}/release.keystore").exists()) "dawahtojannah" else "android"
      keyAlias = System.getenv("KEY_ALIAS") ?: if (file("${rootDir}/release.keystore").exists()) "dawahkey" else "androiddebugkey"
      keyPassword = System.getenv("KEY_PASSWORD") ?: if (file("${rootDir}/release.keystore").exists()) "dawahtojannah" else "android"
      enableV1Signing = true
      enableV2Signing = true
    }
    create("debugConfig") {
      val ksFile = if (file("${rootDir}/debug.keystore").exists()) {
        file("${rootDir}/debug.keystore")
      } else if (file("${rootDir}/release.keystore").exists()) {
        file("${rootDir}/release.keystore")
      } else {
        file("${rootDir}/debug.keystore")
      }
      var useReleaseCreds = false
      if (ksFile.exists()) {
        try {
          val ks = KeyStore.getInstance("PKCS12")
          val stream: InputStream = ksFile.inputStream()
          stream.use { s ->
            ks.load(s, "dawahtojannah".toCharArray())
            useReleaseCreds = true
          }
        } catch (_: Exception) {
          useReleaseCreds = false
        }
      }
      storeFile = ksFile
      storePassword = if (useReleaseCreds) "dawahtojannah" else "android"
      keyAlias = if (useReleaseCreds) "dawahkey" else "androiddebugkey"
      keyPassword = if (useReleaseCreds) "dawahtojannah" else "android"
      enableV1Signing = true
      enableV2Signing = true
    }
  }

  buildTypes {
    release {
      isCrunchPngs = false
      isMinifyEnabled = false
      proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
      signingConfig = signingConfigs.getByName("release")
    }
    debug { signingConfig = signingConfigs.getByName("debugConfig") }
  }
  compileOptions {
    sourceCompatibility = JavaVersion.VERSION_11
    targetCompatibility = JavaVersion.VERSION_11
  }
  buildFeatures {
    compose = true
    buildConfig = true
  }
  testOptions { unitTests { isIncludeAndroidResources = true } }
  dependenciesInfo {
    includeInApk = false
    includeInBundle = true
  }
}

// Automatic Secrets Self-Healing: Guarantee that .env and .env.example always have valid
// non-empty String values so Secrets Gradle Plugin never generates invalid Java syntax like
// 'public static final String GEMINI_API_KEY = ;' which breaks CI/CD and release builds.
val rootDirFile = rootDir
val envFile = rootDirFile.resolve(".env")
val envExampleFile = rootDirFile.resolve(".env.example")

fun sanitizeSecretsProperties(file: java.io.File, defaultVal: String = "your_api_key_here") {
    if (!file.exists()) {
        file.writeText("GEMINI_API_KEY=\"$defaultVal\"\n")
        return
    }
    val lines = file.readLines()
    var modified = false
    val newLines = lines.map { line ->
        val trimmed = line.trim()
        if (trimmed == "GEMINI_API_KEY=" || trimmed == "GEMINI_API_KEY=\"\"" || trimmed == "GEMINI_API_KEY=''") {
            modified = true
            "GEMINI_API_KEY=\"$defaultVal\""
        } else {
            line
        }
    }
    if (!lines.any { it.trim().startsWith("GEMINI_API_KEY") }) {
        file.appendText("\nGEMINI_API_KEY=\"$defaultVal\"\n")
    } else if (modified) {
        file.writeText(newLines.joinToString("\n") + "\n")
    }
}

try {
    sanitizeSecretsProperties(envExampleFile)
    sanitizeSecretsProperties(envFile)
} catch (_: Exception) {
    // Gracefully continue if filesystem access is restricted
}

// Configure the Secrets Gradle Plugin to use .env and .env.example files
// to match the convention used in Web projects.
secrets {
  propertiesFileName = ".env"
  defaultPropertiesFileName = ".env.example"
  ignoreList.add("FIREBASE_APPCHECK_DEBUG_TOKEN")
}

googleServices { missingGoogleServicesStrategy = MissingGoogleServicesStrategy.WARN }

// Some unused dependencies are commented out below instead of being removed.
// This makes it easy to add them back in the future if needed.
dependencies {
  implementation(platform(libs.androidx.compose.bom))
  implementation(platform(libs.firebase.bom))
  // implementation(libs.accompanist.permissions)
  implementation(libs.androidx.activity.compose)
  implementation(libs.androidx.camera.camera2)
  implementation(libs.androidx.camera.core)
  implementation(libs.androidx.camera.lifecycle)
  implementation(libs.androidx.camera.view)
  implementation(libs.androidx.compose.material.icons.core)
  implementation(libs.androidx.compose.material.icons.extended)
  implementation(libs.androidx.compose.material3)
  implementation(libs.androidx.compose.ui)
  implementation(libs.androidx.compose.ui.text.google.fonts)
  implementation(libs.androidx.compose.ui.graphics)
  implementation(libs.androidx.compose.ui.tooling.preview)
  implementation(libs.androidx.core.ktx)
  implementation(libs.androidx.datastore.preferences)
  implementation(libs.androidx.lifecycle.runtime.compose)
  implementation(libs.androidx.lifecycle.runtime.ktx)
  implementation(libs.androidx.lifecycle.viewmodel.compose)
  // implementation(libs.androidx.navigation.compose)
  implementation(libs.androidx.room.ktx)
  implementation(libs.androidx.room.runtime)
  implementation(libs.coil.compose)
  implementation(libs.converter.moshi)
  implementation(libs.firebase.ai)
  // Uncomment to use Firestore:
  // implementation(libs.firebase.firestore)

  // Uncomment ALL FOUR of the following dependencies together to use Firebase Auth and Google
  // Sign-In via Credential Manager:
  // implementation(libs.firebase.auth)
  // implementation(libs.androidx.credentials)
  // implementation(libs.androidx.credentials.play.services)
  // implementation(libs.googleid)
  implementation(libs.firebase.appcheck.recaptcha)
  implementation(libs.firebase.appcheck.debug)
  implementation(libs.kotlinx.coroutines.android)
  implementation(libs.kotlinx.coroutines.core)
  implementation(libs.logging.interceptor)
  implementation(libs.moshi.kotlin)
  implementation(libs.okhttp)
  // implementation(libs.play.services.location)
  implementation(libs.retrofit)
  testImplementation(libs.androidx.compose.ui.test.junit4)
  testImplementation(libs.androidx.core)
  testImplementation(libs.androidx.junit)
  testImplementation(libs.junit)
  testImplementation(libs.kotlinx.coroutines.test)
  testImplementation(libs.robolectric)
  testImplementation(libs.roborazzi)
  testImplementation(libs.roborazzi.compose)
  testImplementation(libs.roborazzi.junit.rule)
  androidTestImplementation(platform(libs.androidx.compose.bom))
  androidTestImplementation(libs.androidx.compose.ui.test.junit4)
  androidTestImplementation(libs.androidx.espresso.core)
  androidTestImplementation(libs.androidx.junit)
  androidTestImplementation(libs.androidx.runner)
  debugImplementation(libs.androidx.compose.ui.test.manifest)
  debugImplementation(libs.androidx.compose.ui.tooling)
  "ksp"(libs.androidx.room.compiler)
  "ksp"(libs.moshi.kotlin.codegen)
}
