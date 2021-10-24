package com.jonnyzzz.kotlin.ketchup.app

import com.jonnyzzz.kotlin.ketchup.reader.ClasspathScanResult
import com.jonnyzzz.kotlin.ketchup.reader.ClasspathScanner
import com.jonnyzzz.kotlin.ketchup.reader.ReaderParameters
import com.jonnyzzz.kotlin.ketchup.reader.TheDeclarationsParser
import org.junit.jupiter.api.Test
import java.io.File
import java.nio.file.Files
import java.nio.file.Path

class IntegrationTest {
  @Test
  fun testOnSelfClasspath() {
    AppMain.main("--classpath=${System.getProperty("java.class.path")}")
  }

  @Test
  fun testOnTestData01() {
    val testDataName = "testData01"
    val classes = loadClassesFromTestData(testDataName)

    classes.classFiles.forEach {
      TheDeclarationsParser().parseClass(Files.readAllBytes(it))
    }
  }

  @Test
  fun testOnTestData02() {
    val testDataName = "testData02"
    val classes = loadClassesFromTestData(testDataName)

    classes.classFiles.forEach {
      TheDeclarationsParser().parseClass(Files.readAllBytes(it))
    }
  }

  private fun loadClassesFromTestData(testDataName: String): ClasspathScanResult {
    val paths = System.getProperty("ketchup.$testDataName")!!.split(File.pathSeparator).map { Path.of(it) }
    return ClasspathScanner.iterateClasspath(object : ReaderParameters {
      override val classpath: List<Path> = paths
    })
  }
}
