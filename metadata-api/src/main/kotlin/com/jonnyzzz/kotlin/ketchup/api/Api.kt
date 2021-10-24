package com.jonnyzzz.kotlin.ketchup.api


class Declarations {
  private val packageToInfo = mutableMapOf<String, MutableList<PackageDeclaration>>()

  fun listDeclarations(): Map<String, List<PackageDeclaration>> {
    return packageToInfo.mapValues { it.value.toList() }
  }

  fun addDeclaration(pkg: String, decl: PackageDeclaration) {
    packageToInfo.computeIfAbsent(pkg){ mutableListOf() }.add(decl)
  }
}

sealed class PackageDeclaration

data class PackageClassDeclaration(val name: String) : PackageDeclaration() {
  override fun toString() = "PackageClassDeclaration(name='$name')"
}

data class PackageKotlinFunctionDeclaration(val name: String) : PackageDeclaration() {
  override fun toString() = "PackageKotlinFunctionDeclaration(name='$name')"
}

data class PackageKotlinPropertyDeclaration(val name: String) : PackageDeclaration() {
  override fun toString() = "PackageKotlinPropertyDeclaration(name='$name')"
}

data class PackageKotlinTypeAliasDeclaration(val name: String) : PackageDeclaration() {
  override fun toString() = "PackageKotlinTypeAliasDeclaration(name='$name')"
}
