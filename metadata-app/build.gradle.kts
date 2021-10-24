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

application {

}
