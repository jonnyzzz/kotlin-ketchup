plugins {
  kotlin("jvm")
  application
}

dependencies {
  implementation(project(":metadata-api"))

  implementation("com.squareup:kotlinpoet-metadata:1.10.2")
  implementation("com.squareup:kotlinpoet:1.10.2")
}

application {

}
