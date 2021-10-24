# kotlin-ketchup
A project aiming to generate KMP declarations from several library versions

# Why

Assume one does a library or a plugin that has to be compatible with
several host versions. Think about an IntelliJ plugin (or maybe an Android App?)

There are may ways to set up the project. One is to have a dedicated `src/kotlin`
source roots per different versions. The shared code can still be in the `src/main/kotlin`
source set. That scenario usually have a parameter somewhere in Gradle to specify
what actual version of the platform to use.

More formally, it may look like
```
project
   \
     src
     + kotlin   <-- here comes the common part
     + v2021.1  <-- a specific code to include for 2021.1 version
     + v2021.2
     + v2021.3
```

Something similar we may have with resource folders, tests, ect.


## Problem

This approach has problems
 * every time there are un-included source folders
 * refactorings are tricky to implement as un-included folders has to be processed
 * the code in common part may assume a class declaration from the version part

These problems are not showstoppers from one hand. From the other hand,
this projects aims to hack the solution with the help of
[Kotlin Multiplatform](https://kotlinlang.org/docs/multiplatform.html)

## Solution

We try to generate necessary `expect/actual` declarations with
[Kotlin Multiplatform](https://kotlinlang.org/docs/multiplatform.html)
and allow having all source folders included as if there were several
target directories.

The main trick in the generation is that it should only include symbols
which are available in the intersection of platform libraries.

To achieve that we use [ASM](https://asm.ow2.io/) library to read the bytecode
of the dependencies from the classpath. We are going to use
[KotlinPoet](https://square.github.io/kotlinpoet/)
to generate the necessary declarations as Kotlin code.

Next is the Gradle part. This is a Gradle plugin. It intersects the classpath
and attaches the generated Kotlin code to the common (with `actual`s) and
to each of version-specific targets. On the other hand, it could be
a dedicated sub-project, which can be generated once and be ready for use.

## Downsides

Parsing JVM bytecode with ASM and
[kotlinx-metadata](https://github.com/JetBrains/kotlin/tree/master/libraries/kotlinx-metadata/jvm)
requires an additional code to map its object into Kotlin code declarations back.

We have to have a duality - one approach uses ASM and JVM to recover Java declarations,
the second uses Kotlin own metadata to generate the proper declarations.

Kotlin compiler parameters or package-wide nullability options has to be included and
supported as well. That is harder to implement and requires effective testing.

Generated declarations would miss original javadocs/dokka annotations. It could make
using the libraries harder as well. An alternative could be to parse **sources**
instead of the compiled bytecode to enrich the generated code with annotations.


## Alternative approaches

At that point it seems more practical to implement the logic as a Kotlin compiler
plugin. It is hard to say how easy or if that could be easier to implement.
