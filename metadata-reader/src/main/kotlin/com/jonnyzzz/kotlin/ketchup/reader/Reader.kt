package com.jonnyzzz.kotlin.ketchup.reader

import org.slf4j.LoggerFactory
import java.nio.file.FileSystems
import java.nio.file.Files
import java.nio.file.Path
import kotlin.streams.asSequence

object Reader {
  private val LOG = LoggerFactory.getLogger(Reader::class.java)

  fun iterateClasspath(params: ReaderParameters) {
    val allFiles = params.classpath.asSequence()
      .flatMap(::unwrapArchives)
      .flatMap(::listDirectories)
      .distinct()
      .toList()

    LOG.info("Found {} files in the classpath", allFiles.size)

    val allClassFiles = allFiles
      .filter { it.fileName.toString().endsWith(".class") }
      .toList()

    LOG.info("Found {} class files in the classpath", allClassFiles.size)
  }

  private fun listDirectories(path: Path): Sequence<Path> {
    return if (Files.isDirectory(path)) {
      Files.walk(path).asSequence().filter { Files.isRegularFile(it) }
    } else {
      sequenceOf(path)
    }
  }

  private fun unwrapArchives(path: Path): Sequence<Path> {
    return if (Files.isRegularFile(path) && path.fileName.toString().endsWith(".jar")) {
      FileSystems.newFileSystem(path, null).rootDirectories.asSequence()
    } else {
      sequenceOf(path)
    }
  }
}
