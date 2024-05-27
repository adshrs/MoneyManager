plugins {
	id("com.android.application")
	id("org.jetbrains.kotlin.android")
	id("kotlin-kapt")
	id("com.google.dagger.hilt.android")
}

android {
	namespace = "com.example.moneymanager"
	compileSdk = 34

	defaultConfig {
		applicationId = "com.example.moneymanager"
		minSdk = 26
		targetSdk = 34
		versionCode = 1
		versionName = "1.0"

		testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
		vectorDrawables {
			useSupportLibrary = true
		}
	}

	buildTypes {
		release {
			isMinifyEnabled = false
			proguardFiles(
				getDefaultProguardFile("proguard-android-optimize.txt"),
				"proguard-rules.pro"
			)
		}
	}
	compileOptions {
		sourceCompatibility = JavaVersion.VERSION_1_8
		targetCompatibility = JavaVersion.VERSION_1_8
	}
	kotlinOptions {
		jvmTarget = "1.8"
	}
	buildFeatures {
		compose = true
	}
	composeOptions {
		kotlinCompilerExtensionVersion = "1.5.1"
	}
	packaging {
		resources {
			excludes += "/META-INF/{AL2.0,LGPL2.1}"
		}
	}
}

dependencies {
	//androidx
	implementation("androidx.core:core-ktx:1.12.0")
	implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.7.0")
	implementation("androidx.activity:activity-compose:1.8.2")
	implementation("androidx.navigation:navigation-compose:2.7.7")
	implementation("androidx.hilt:hilt-navigation-compose:1.2.0")
	implementation(platform("androidx.compose:compose-bom:2024.04.00"))
	implementation("androidx.compose.ui:ui")
	implementation("androidx.compose.ui:ui-graphics")
	implementation("androidx.compose.ui:ui-tooling-preview")
	implementation("androidx.compose.material3:material3")
	implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.7.0")
	implementation("androidx.compose.ui:ui-text-google-fonts:1.6.5")

	// UI
	implementation("com.marosseleng.android:compose-material3-datetime-pickers:0.7.2")
	implementation("com.github.skydoves:colorpicker-compose:1.0.7")
	implementation("me.saket.swipe:swipe:1.3.0")
	implementation("io.github.serpro69:kotlin-faker:1.13.0")
	implementation("com.github.tehras:charts:0.2.4-alpha")

	// Dependency Injection
	implementation("com.google.dagger:hilt-android:2.51.1")
	kapt("com.google.dagger:hilt-android-compiler:2.51.1")

	// Network
	implementation("com.squareup.retrofit2:retrofit:2.11.0")
	implementation("com.squareup.retrofit2:converter-gson:2.11.0")
	implementation("com.squareup.okhttp3:okhttp:4.12.0")

	//JWT
	implementation("io.jsonwebtoken:jjwt-api:0.12.5")
	implementation("io.jsonwebtoken:jjwt-impl:0.12.5")

	//PDF GENERATION
	implementation("com.itextpdf:itext7-core:7.1.15")

	// Test
	testImplementation("junit:junit:4.13.2")
	androidTestImplementation("androidx.test.ext:junit:1.1.5")
	androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
	androidTestImplementation(platform("androidx.compose:compose-bom:2024.04.00"))
	androidTestImplementation("androidx.compose.ui:ui-test-junit4")
	debugImplementation("androidx.compose.ui:ui-tooling")
	debugImplementation("androidx.compose.ui:ui-test-manifest")
}
