package com.jonnyzzz.kotlin.ketchup.reader

import org.junit.jupiter.api.Test

class IntegrationTest {
  @Test
  fun testOnSelfClasspath() {
    Main.main("--classpath=${System.getProperty("java.class.path")}")
  }
}
