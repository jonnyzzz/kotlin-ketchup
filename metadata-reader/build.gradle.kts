plugins {
  kotlin("jvm")
  application
}

dependencies {
  implementation(project(":args-parser"))
  implementation("org.ow2.asm:asm:9.2")

}

application {
  mainClass.set("com.jonnyzzz.kotlin.ketchup.reader.Main")
}
