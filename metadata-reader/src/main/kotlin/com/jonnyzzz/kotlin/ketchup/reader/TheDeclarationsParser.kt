package com.jonnyzzz.kotlin.ketchup.reader

import com.squareup.kotlinpoet.metadata.KotlinPoetMetadataPreview
import com.squareup.kotlinpoet.metadata.specs.toFileSpec
import kotlinx.metadata.jvm.KotlinClassMetadata
import org.slf4j.LoggerFactory
import java.io.StringWriter


class TheDeclarationsParser {
  private val LOG = LoggerFactory.getLogger(TheDeclarationsParser::class.java)

  fun parseClass(classData: ByteArray) {
    val className = ClassNameReader.tryReadKotlinMetadata(classData) ?: return
    LOG.info("Processing class {}", className)

    val kotlinInfo = KotlinMetadataReader.tryReadKotlinMetadata(classData) ?: return
    LOG.info("Processing class {} kotlin info: {}", className, kotlinInfo.javaClass.simpleName)

    if (kotlinInfo is KotlinClassMetadata.Class) {
      poet(kotlinInfo)
    }
  }
}


@OptIn(KotlinPoetMetadataPreview::class)
fun poet(clazz: KotlinClassMetadata.Class) {
  val clazzz = clazz.toKmClass()

  if (clazzz.name.startsWith(".")) return

  val sq = StringWriter()
  clazzz.toFileSpec(null).writeTo(sq)
  println(sq)
}
