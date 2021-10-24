package com.jonnyzzz.kotlin.ketchup.writer

import com.squareup.kotlinpoet.metadata.KotlinPoetMetadataPreview
import com.squareup.kotlinpoet.metadata.specs.toFileSpec
import kotlinx.metadata.jvm.KotlinClassMetadata
import java.io.StringWriter


@OptIn(KotlinPoetMetadataPreview::class)
fun poet(clazz: KotlinClassMetadata.Class) {
  val clazzz = clazz.toKmClass()

  if (clazzz.name.startsWith(".")) return

  val sq = StringWriter()
  clazzz.toFileSpec(null).writeTo(sq)
  println(sq)
}

