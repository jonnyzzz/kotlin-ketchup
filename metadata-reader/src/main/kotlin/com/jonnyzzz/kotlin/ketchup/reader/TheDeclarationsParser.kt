package com.jonnyzzz.kotlin.ketchup.reader

import com.jonnyzzz.kotlin.ketchup.api.*
import kotlinx.metadata.jvm.KotlinClassMetadata
import org.slf4j.LoggerFactory


class TheDeclarationsParser {
  private val LOG = LoggerFactory.getLogger(TheDeclarationsParser::class.java)

  private val declarations = mutableListOf<PackageDeclaration>()

  fun build(): List<PackageDeclaration> = declarations.toList()

  fun parseClass(classData: ByteArray) {
    val className = ClassNameReader.tryReadKotlinMetadata(classData) ?: return
    LOG.info("Processing class {}", className)

    val kotlinInfo = KotlinMetadataReader.tryReadKotlinMetadata(classData) ?: return
    LOG.info("Processing class {} kotlin info: {}", className, kotlinInfo.javaClass.simpleName)

    when (kotlinInfo) {
      is KotlinClassMetadata.Class -> {
        val clazz = kotlinInfo.toKmClass()
        declarations.add(PackageClassDeclaration(clazz.name))
      }

      is KotlinClassMetadata.FileFacade -> {
        val facade = kotlinInfo.toKmPackage()
        facade.functions.forEach { declarations += PackageKotlinFunctionDeclaration(it.name) }
        facade.properties.forEach { declarations += PackageKotlinPropertyDeclaration(it.name) }
        facade.typeAliases.forEach { declarations += PackageKotlinTypeAliasDeclaration(it.name) }
      }
    }


  }
}

