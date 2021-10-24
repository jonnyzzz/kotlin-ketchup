package com.jonnyzzz.kotlin.ketchup

import com.jonnyzzz.args.ArgsParser
import kotlin.system.exitProcess

class Args(parser: ArgsParser) {

  override fun toString() = buildString {
    appendLine("Commandline arguments:")
    appendLine("none")
    appendLine()
  }
}

object Main {
  @JvmStatic
  fun main(args: Array<String>) {
    println("Running Kotlin Ketchup...")

    val parser = ArgsParser(args.toList())
    val params = Args(parser)
    try {
      parser.tryReadAll()
    } catch (t: Throwable) {
      println(parser.usage(true))
      exitProcess(1)
    }

    println("Params: $params")
  }
}
