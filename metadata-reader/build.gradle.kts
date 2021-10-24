plugins {
  kotlin("jvm")
  application
}

dependencies {
  implementation(project(":args-parser"))
  implementation("org.ow2.asm:asm:9.2")

  implementation("org.jetbrains.kotlinx:kotlinx-metadata-jvm:0.3.0")

  implementation("com.squareup:kotlinpoet-metadata:1.10.2")
  implementation("com.squareup:kotlinpoet:1.10.2")
}

application {
  mainClass.set("com.jonnyzzz.kotlin.ketchup.reader.Main")
}
