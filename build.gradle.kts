plugins {
    java
    kotlin("jvm") version "1.3.72"
}
subprojects {
    apply(plugin = "java")
    apply(plugin = "kotlin")
    group = "rug.tbp"
    version = "1.0"
    repositories {
        mavenCentral()
    }
    dependencies {
        implementation(kotlin("stdlib-jdk8"))
        "testImplementation"("junit:junit:4.12")
    }
    configure<JavaPluginConvention> {
        sourceCompatibility = JavaVersion.VERSION_1_8
    }
    tasks {
        compileKotlin {
            kotlinOptions.jvmTarget = "1.8"
        }
        compileTestKotlin {
            kotlinOptions.jvmTarget = "1.8"
        }
    }
}