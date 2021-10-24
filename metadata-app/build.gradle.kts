import org.jetbrains.kotlin.util.capitalizeDecapitalize.capitalizeAsciiOnly
import org.jetbrains.kotlin.util.capitalizeDecapitalize.decapitalizeAsciiOnly

plugins {
  kotlin("jvm")
  application
}

dependencies {
  implementation(project(":args-parser"))
  implementation(project(":metadata-api"))
  implementation(project(":metadata-reader"))
  implementation(project(":metadata-writer"))
}

file("src").listFiles()!!.filter { it.name.startsWith("test-data-") && it.isDirectory }.forEach { dir ->
  val sourceSetName = dir.name.split("-")
    .joinToString("") { it.capitalizeAsciiOnly() }
    .decapitalizeAsciiOnly()

  val sourceDir = File(dir, "kotlin")

  val javaSourceSet = sourceSets.create(sourceSetName) {
    java.srcDir(sourceDir)
  }

  kotlin.sourceSets.getByName(sourceSetName) {
    kotlin.srcDir(sourceDir)
  }

  println("Detecting $sourceSetName ")


  tasks.test {
    dependsOn(javaSourceSet.classesTaskName)
    doFirst {
      val classpath = javaSourceSet.output.joinToString(File.pathSeparator)
      systemProperty("ketchup.$sourceSetName", classpath)
    }
  }
}

application {
  mainClass.set("com.jonnyzzz.kotlin.ketchup.app.AppMain")
}
