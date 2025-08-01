package kioskware.kstateflows.kioskware.kstateflows

import kioskware.kstateflows.combineStateFlows
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch



// Przykład użycia combineStateFlows z pięcioma StateFlow
@OptIn(DelicateCoroutinesApi::class)
internal suspend fun main() {
    // Tworzymy scope dla funkcji
    val customScope = CoroutineScope(Dispatchers.Default)

    // StateFlow do testów
    val s1 = MutableStateFlow(1)
    val s2 = MutableStateFlow(2)
    val s3 = MutableStateFlow(3)
    val s4 = MutableStateFlow(4)
    val s5 = MutableStateFlow(5)

    val combined = combineStateFlows(
        s1, s2, s3, s4, s5,
        transform = { values ->
            values.joinToString("")
        }
    )

    GlobalScope.apply {

        println("Initial value: ${combined.value}") // Powinno być "12345"

        launch {
            repeat(10){
                delay(500)
                s1.value += 1
            }
        }
        launch {
            repeat(20){
                delay(240)
                s4.value += 1
            }
        }

    }

    delay(17000) // Czekamy na zakończenie testów

    println("Final value: ${combined.value}") // Powinno być "23456"
}

