package com.jonnyzzz.kotlin.ketchup.reader

import java.nio.file.Path

interface ReaderParameters {
  val classpath: List<Path>
}
