package com.jonnyzzz.kotlin.ketchup.reader

import kotlinx.metadata.jvm.KotlinClassHeader
import kotlinx.metadata.jvm.KotlinClassMetadata
import org.objectweb.asm.AnnotationVisitor
import org.objectweb.asm.ClassReader
import org.objectweb.asm.ClassVisitor
import org.objectweb.asm.Opcodes
import org.slf4j.LoggerFactory

object KotlinMetadataReader {
  private val LOG = LoggerFactory.getLogger(KotlinMetadataReader::class.java)

  fun tryReadKotlinMetadata(classData: ByteArray): KotlinClassMetadata? {
    lateinit var className: String
    var header: KotlinClassHeader? = null

    val visitor = object : ClassVisitor(Opcodes.ASM9) {
      override fun visit(version: Int, access: Int, name: String, signature: String?, superName: String?, interfaces: Array<out String>?) {
        super.visit(version, access, name, signature, superName, interfaces)
        className = name
      }

      override fun visitAnnotation(descriptor: String?, visible: Boolean): AnnotationVisitor? {
        if (descriptor == "Lkotlin/Metadata;") {
          return KotlinMetadataReaderVisitor { header = it }
        }
        return null
      }
    }

    val kotlinHeader = try {
      ClassReader(classData)
        .accept(visitor, ClassReader.SKIP_CODE or ClassReader.SKIP_DEBUG or ClassReader.SKIP_FRAMES)

      header
    } catch (t: Throwable) {
      LOG.warn("Failed to read class with ASM {}", t.message, t)
      return null
    } ?: return null

    return try {
      val m = KotlinClassMetadata.read(kotlinHeader)
      if (m == null) {
        LOG.debug("No kotlin metadata for {}", className)
      }
      m
    } catch (t: Throwable) {
      LOG.warn("Failed to decode Kotlin metadata for {}: {}", className, t)
      null
    }
  }

  private class KotlinMetadataReaderVisitor(
    private val callback: (KotlinClassHeader) -> Unit,
  ) : AnnotationVisitor(Opcodes.ASM9) {
    // var kind: Int
    // var mv: IntArray
    // var bv: IntArray
    // var d1: Array<String>
    // var d2: Array<String>
    // var extraString: String = ""
    // var pn: String
    // var extraInt: Int = 0
    private val fieldToValue = mutableMapOf<String, Any?>()

    override fun visit(name: String, value: Any?) {
      require(name !in fieldToValue) { "Name '$name' is already included to $name"}
      fieldToValue[name] = value
    }

    override fun visitEnum(name: String?, descriptor: String?, value: String?) {
      error("Visit enum value is not implemented: $name, $descriptor, $value")
    }

    override fun visitArray(outerName: String): AnnotationVisitor {
      val r = mutableListOf<Any?>()
      val that = this
      return object : AnnotationVisitor(Opcodes.ASM9) {
        override fun visit(name: String?, value: Any?) {
          r += value
        }

        override fun visitEnum(name: String?, descriptor: String?, value: String?) {
          error("Visit nested enum value is not implemented: $name, $descriptor")
        }

        override fun visitAnnotation(name: String?, descriptor: String?): AnnotationVisitor {
          error("Visit nested annotation value is not implemented: $name, $descriptor")
        }

        override fun visitArray(name: String?): AnnotationVisitor {
          error("Visit nested array value is not implemented: $name")
        }

        override fun visitEnd() {
          that.visit(outerName, r.toTypedArray())
        }
      }
    }

    override fun visitAnnotation(name: String?, descriptor: String?): AnnotationVisitor? {
      error("Visit annotation value is not implemented: $name, $descriptor")
    }

    override fun visitEnd() {
      val kind = fieldToValue["k"] as? Int
      val metadataVersion = fieldToValue["mv"] as? IntArray
      val data1 = (fieldToValue["d1"] as? Array<*>)?.map { it as String }?.toTypedArray()
      val data2 = (fieldToValue["d2"] as? Array<*>)?.map { it as String }?.toTypedArray()
      val extraString = fieldToValue["xs"] as? String
      val packageName = fieldToValue["pn"] as? String
      val extra = fieldToValue["xi"] as? Int

      callback(KotlinClassHeader(
        kind = kind,
        metadataVersion = metadataVersion,
        data1 = data1,
        data2 = data2,
        extraString = extraString,
        packageName = packageName,
        extraInt = extra
      ))
    }
  }
}
