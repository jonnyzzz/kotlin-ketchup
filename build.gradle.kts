plugins {
  kotlin("jvm") version "1.5.31" apply false
  `java-library`
}


group = "com.jonnyzzz.kotlin-ketchup"
version = System.getenv("BUILD_NUMBER") ?: "0.0.1-SNAPSHOT"

subprojects {
  apply(plugin = "java-library")

  group = rootProject.group
  version = rootProject.version

  tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
    kotlinOptions {
      freeCompilerArgs = listOf("-Xjsr305=strict")
      jvmTarget = "11"
    }
  }

  extensions.configure(JavaPluginExtension::class) {
    sourceCompatibility = JavaVersion.VERSION_11
    targetCompatibility = JavaVersion.VERSION_11
  }

  tasks.withType<Test> {
    useJUnitPlatform()
    jvmArgs("-XX:+HeapDumpOnOutOfMemoryError")
  }

  repositories {
    mavenCentral()
  }

  dependencies {
    testImplementation(platform("org.junit:junit-bom:5.8.1"))
    testImplementation("org.junit.jupiter:junit-jupiter")

    implementation("org.slf4j:slf4j-api:1.7.32")
    testImplementation("org.slf4j:slf4j-simple:1.7.32")
  }
}
