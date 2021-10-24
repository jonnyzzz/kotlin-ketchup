package com.jonnyzzz.kotlin.ketchup.app

import org.junit.jupiter.api.Test

class IntegrationTest {
  @Test
  fun testOnSelfClasspath() {
    AppMain.main("--classpath=${System.getProperty("java.class.path")}")
  }

  @Test
  fun testOnTestData01() {
    val path = System.getProperty("ketchup.testData01")!!
    AppMain.main("--classpath=$path")
  }
}
