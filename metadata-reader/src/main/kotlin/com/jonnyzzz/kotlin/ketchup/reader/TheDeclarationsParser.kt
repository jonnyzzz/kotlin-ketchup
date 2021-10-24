package com.jonnyzzz.kotlin.ketchup.reader

import kotlinx.metadata.jvm.KotlinClassMetadata
import org.slf4j.LoggerFactory

class TheDeclarationsParser {
  private val LOG = LoggerFactory.getLogger(TheDeclarationsParser::class.java)

  fun parseClass(classData: ByteArray) {
    val className = ClassNameReader.tryReadKotlinMetadata(classData) ?: return
    LOG.info("Processing class {}", className)

    val kotlinInfo = KotlinMetadataReader.tryReadKotlinMetadata(classData) ?: return
    LOG.info("Processing class {} kotlin info: {}", className, kotlinInfo.javaClass.simpleName)

    if (kotlinInfo is KotlinClassMetadata.Class) {
//      poet(kotlinInfo)
    }
  }
}

