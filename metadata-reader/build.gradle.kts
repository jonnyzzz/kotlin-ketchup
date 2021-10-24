plugins {
  kotlin("jvm")
  application
}

dependencies {
  implementation(project(":args-parser"))
  implementation(project(":metadata-api"))
  implementation("org.ow2.asm:asm:9.2")

  implementation("org.jetbrains.kotlinx:kotlinx-metadata-jvm:0.3.0")

}

application {
  mainClass.set("com.jonnyzzz.kotlin.ketchup.reader.Main")
}
