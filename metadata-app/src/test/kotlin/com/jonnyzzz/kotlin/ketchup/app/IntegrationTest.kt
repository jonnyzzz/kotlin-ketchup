package com.jonnyzzz.kotlin.ketchup.app

import com.jonnyzzz.kotlin.ketchup.api.*
import com.jonnyzzz.kotlin.ketchup.reader.ClasspathScanner
import com.jonnyzzz.kotlin.ketchup.reader.ReaderParameters
import com.jonnyzzz.kotlin.ketchup.reader.TheDeclarationsParser
import org.junit.jupiter.api.Assertions
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

    Assertions.assertEquals(setOf(
      PackageClassDeclaration("JavaAnnotation"),
      PackageClassDeclaration("JavaEnum"),
      PackageClassDeclaration("JavaInterface"),
    ), classes)
  }

  @Test
  fun testOnTestData02() {
    val testDataName = "testData02"
    val classes = loadClassesFromTestData(testDataName).toSet()

    Assertions.assertEquals(setOf(
      PackageKotlinFunctionDeclaration(name = "thisIsGlobFun"),
      PackageKotlinPropertyDeclaration(name = "thisIsGlobVal"),
      PackageKotlinTypeAliasDeclaration(name = "KotlinAlias"),
      PackageClassDeclaration(name = "ThisIsKotlinInterface"),
      PackageClassDeclaration(name = "ThisIsKotlinObject"),
      PackageClassDeclaration(name = "ThisIsKotlinClass"),
    ), classes)
  }

  private fun loadClassesFromTestData(testDataName: String): List<PackageDeclaration> {
    val paths = System.getProperty("ketchup.$testDataName")!!.split(File.pathSeparator).map { Path.of(it) }
    val classes = ClasspathScanner.iterateClasspath(object : ReaderParameters {
      override val classpath: List<Path> = paths
    })

    val parser = TheDeclarationsParser()
    classes.classFiles.forEach {
      parser.parseClass(Files.readAllBytes(it))
    }

    val build = parser.build()
    println("Computed declarations:\n" + build.joinToString("") { "\n  $it" })
    return build
  }
}
