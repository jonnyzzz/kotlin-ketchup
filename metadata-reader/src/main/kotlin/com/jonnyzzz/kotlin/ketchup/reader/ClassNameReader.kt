package com.jonnyzzz.kotlin.ketchup.reader

import kotlinx.metadata.jvm.KotlinClassHeader
import kotlinx.metadata.jvm.KotlinClassMetadata
import org.objectweb.asm.AnnotationVisitor
import org.objectweb.asm.ClassReader
import org.objectweb.asm.ClassVisitor
import org.objectweb.asm.Opcodes
import org.slf4j.LoggerFactory

object ClassNameReader {
  private val LOG = LoggerFactory.getLogger(ClassNameReader::class.java)

  fun tryReadKotlinMetadata(classData: ByteArray): String? {
    lateinit var className: String

    val visitor = object : ClassVisitor(Opcodes.ASM9) {
      override fun visit(version: Int, access: Int, name: String, signature: String?, superName: String?, interfaces: Array<out String>?) {
        super.visit(version, access, name, signature, superName, interfaces)
        className = name
      }
    }

    return try {
      ClassReader(classData)
        .accept(visitor, ClassReader.SKIP_CODE or ClassReader.SKIP_DEBUG or ClassReader.SKIP_FRAMES)
      className
    } catch (t: Throwable) {
      LOG.warn("Failed to read class with ASM {}", t.message, t)
      null
    }
  }

}
