# KStateFlows

A lightweight Kotlin library that provides convenient utilities for combining multiple StateFlows into a single StateFlow.

Developed by [kioskware.co](https://kioskware.co) - company delivering professional Android kiosk software and hardware. Based in Poland 🇵🇱

## Overview

KStateFlows extends Kotlin's coroutines library by offering type-safe functions to combine 2-5 StateFlows or any number of StateFlows with different types. Unlike the standard `combine` function, these utilities return a StateFlow (not just a Flow), preserving the immediate access to current values.

## Features

- ✅ Combine 2-5 StateFlows with type safety
- ✅ Combine any number of StateFlows with flexible typing
- ✅ Immediate access to current values (StateFlow.value)
- ✅ Reactive updates when any source StateFlow changes
- ✅ Built on top of Kotlin Coroutines
- ✅ Lightweight with minimal dependencies

## Installation

### Gradle (Kotlin DSL)

```kotlin
dependencyResolutionManagement {
		repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
		repositories {
			mavenCentral()
			maven { url = uri("https://jitpack.io") }
		}
	}
```

```kotlin
dependencies {
	        implementation("com.github.kioskware:kstateflows:0.1.1-alpha")
}
```

## Usage

### Basic Example - Combining Two StateFlows

```kotlin
import kioskware.kstateflows.combineStateFlows
import kotlinx.coroutines.flow.MutableStateFlow

val firstName = MutableStateFlow("John")
val lastName = MutableStateFlow("Doe")

val fullName = combineStateFlows(firstName, lastName) { first, last ->
    "$first $last"
}

println(fullName.value) // "John Doe"

// Update any source StateFlow
firstName.value = "Jane"
println(fullName.value) // "Jane Doe"
```

### Combining Multiple StateFlows (2-5)

```kotlin
val s1 = MutableStateFlow(1)
val s2 = MutableStateFlow(2)
val s3 = MutableStateFlow(3)
val s4 = MutableStateFlow(4)
val s5 = MutableStateFlow(5)

val combined = combineStateFlows(s1, s2, s3, s4, s5) { v1, v2, v3, v4, v5 ->
    listOf(v1, v2, v3, v4, v5).sum()
}

println(combined.value) // 15
```

### Combining Any Number of StateFlows

```kotlin
val flows = arrayOf(
    MutableStateFlow("Hello"),
    MutableStateFlow(" "),
    MutableStateFlow("World"),
    MutableStateFlow("!")
)

val message = combineStateFlows(*flows) { values ->
    values.joinToString("")
}

println(message.value) // "Hello World!"
```

### Reactive Collection

```kotlin
import kotlinx.coroutines.*

val scope = CoroutineScope(Dispatchers.Default)

scope.launch {
    combined.collect { value ->
        println("New combined value: $value")
    }
}
```

## API Reference

### Type-Safe Combinations (2-5 StateFlows)

```kotlin
fun <S1, S2, R> combineStateFlows(
    flow1: StateFlow<S1>,
    flow2: StateFlow<S2>,
    transform: (S1, S2) -> R
): StateFlow<R>

// Similar overloads available for 3, 4, and 5 StateFlows
```

### Flexible Combination (Any Number)

```kotlin
fun <R> combineStateFlows(
    vararg flows: StateFlow<*>,
    transform: (List<Any?>) -> R
): StateFlow<R>
```

### CombinedStateFlow Class

```kotlin
class CombinedStateFlow<R>(
    private vararg val flows: StateFlow<*>,
    private val transform: (List<Any?>) -> R
) : StateFlow<R>
```

## Key Benefits

1. **StateFlow Preservation**: Unlike `combine()`, the result is a StateFlow with immediate value access
2. **Type Safety**: Overloaded functions provide compile-time type checking for 2-5 StateFlows
3. **Flexibility**: Support for combining any number of StateFlows with different types
4. **Reactive**: Automatically updates when any source StateFlow emits a new value
5. **Immediate Values**: Access current combined value without collection

## Requirements

- Kotlin 1.8+
- Kotlinx Coroutines Core 1.8.1+
- JVM 17+

## License

This project is licensed under the [MIT License](LICENSE).

## Contributing

Contributions are welcome! Please feel free to submit a Pull Request.

## Changelog

### 0.1.0-alpha
- Initial release
- Support for combining 2-5 StateFlows with type safety
- Support for combining any number of StateFlows
- Basic documentation and examples
