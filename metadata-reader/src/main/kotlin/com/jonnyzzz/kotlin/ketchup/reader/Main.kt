package com.jonnyzzz.kotlin.ketchup.reader

import com.jonnyzzz.args.ArgsParser
import java.nio.file.Path
import kotlin.system.exitProcess

interface ReaderParameters {
  val classpath: List<Path>
}

class ArgsImpl(parser: ArgsParser) : ReaderParameters{
  override val classpath by parser
    .arg("classpath", "specify classpath elements to read declarations")
    .strings()
    .andMap { it.flatMap { e -> e.split(System.getProperty("path.separator")) } }
    .andMap { it.map { e -> Path.of(e) } }

  override fun toString() = buildString {
    appendLine("Commandline arguments:")
    appendLine("  classpath:")
    if (classpath.isEmpty()) {
      appendLine("    <none>")
    } else {
      classpath.forEach { e ->
        appendLine("    $e")
      }
    }
    appendLine()
  }
}

object Main {
  @JvmStatic
  fun main(vararg args: String) {
    println("Running Kotlin Ketchup...")

    val parser = ArgsParser(args.toList())
    val params = ArgsImpl(parser)
    try {
      parser.tryReadAll()
    } catch (t: Throwable) {
      println(parser.usage(true))
      exitProcess(1)
    }

    println("Params: $params")

    Reader.iterateClasspath(params)
  }
}
