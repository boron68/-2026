plugins {
    kotlin("js") version "1.9.10"
}

repositories {
    mavenCentral()
}

kotlin {
    js(IR) {
        browser {
            binaries.executable()
            commonWebpackConfig {
                cssSupport {
                    enabled = true
                }
            }
        }
    }
}

dependencies {
    implementation(kotlin("stdlib-js"))
    implementation("org.jetbrains.kotlinx:kotlinx-html-js:0.8.0")
}

tasks.register("stage") {
    dependsOn("build")
}
