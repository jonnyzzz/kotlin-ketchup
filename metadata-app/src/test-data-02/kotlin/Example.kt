@file:Suppress("unused", "UNUSED_PARAMETER")

class ThisIsKotlinClass(x1: String)

object ThisIsKotlinObject

interface ThisIsKotlinInterface

fun thisIsGlobFun(): ThisIsKotlinInterface = TODO()

val thisIsGlobVal get() = ThisIsKotlinObject

