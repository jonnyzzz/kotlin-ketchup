package com.jonnyzzz.kotlin.ketchup.reader

import org.slf4j.LoggerFactory

data class TypeDefinition(
  val fqn: String
)


class ClassesParser {
  private val LOG = LoggerFactory.getLogger(ClassesParser::class.java)

  fun parseClass(classData: ByteArray) {
    val className = ClassNameReader.tryReadKotlinMetadata(classData) ?: return
    LOG.info("Processing class {}", className)

    val kotlinInfo = KotlinMetadataReader.tryReadKotlinMetadata(classData) ?: return
    LOG.info("Processing class {} kotlin info: ", className, kotlinInfo)
  }
}
