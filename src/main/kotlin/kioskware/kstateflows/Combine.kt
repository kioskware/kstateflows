package kioskware.kstateflows.kioskware.kstateflows

import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine

/**
 * Combines two StateFlows into a single StateFlow using a transform function.
 *
 * The resulting StateFlow will emit a new value whenever any of the source StateFlows
 * emit a new value. The initial value is computed from the current values of both flows.
 *
 * @param flow1 The first StateFlow to combine
 * @param flow2 The second StateFlow to combine
 * @param transform A function that takes the current values from both flows and returns the combined result
 * @return A new StateFlow that emits the transformed values
 */
fun <S1, S2, R> combineStateFlows(
    flow1: StateFlow<S1>,
    flow2: StateFlow<S2>,
    transform: (S1, S2) -> R
): StateFlow<R> = CombinedStateFlow(flow1, flow2) { values ->
    @Suppress("UNCHECKED_CAST")
    transform(values[0] as S1, values[1] as S2)
}

/**
 * Combines three StateFlows into a single StateFlow using a transform function.
 *
 * The resulting StateFlow will emit a new value whenever any of the source StateFlows
 * emit a new value. The initial value is computed from the current values of all three flows.
 *
 * @param flow1 The first StateFlow to combine
 * @param flow2 The second StateFlow to combine
 * @param flow3 The third StateFlow to combine
 * @param transform A function that takes the current values from all three flows and returns the combined result
 * @return A new StateFlow that emits the transformed values
 */
fun <S1, S2, S3, R> combineStateFlows(
    flow1: StateFlow<S1>,
    flow2: StateFlow<S2>,
    flow3: StateFlow<S3>,
    transform: (S1, S2, S3) -> R
): StateFlow<R> = CombinedStateFlow(flow1, flow2, flow3) { values ->
    @Suppress("UNCHECKED_CAST")
    transform(values[0] as S1, values[1] as S2, values[2] as S3)
}

/**
 * Combines four StateFlows into a single StateFlow using a transform function.
 *
 * The resulting StateFlow will emit a new value whenever any of the source StateFlows
 * emit a new value. The initial value is computed from the current values of all four flows.
 *
 * @param flow1 The first StateFlow to combine
 * @param flow2 The second StateFlow to combine
 * @param flow3 The third StateFlow to combine
 * @param flow4 The fourth StateFlow to combine
 * @param transform A function that takes the current values from all four flows and returns the combined result
 * @return A new StateFlow that emits the transformed values
 */
fun <S1, S2, S3, S4, R> combineStateFlows(
    flow1: StateFlow<S1>,
    flow2: StateFlow<S2>,
    flow3: StateFlow<S3>,
    flow4: StateFlow<S4>,
    transform: (S1, S2, S3, S4) -> R
): StateFlow<R> = CombinedStateFlow(flow1, flow2, flow3, flow4) { values ->
    @Suppress("UNCHECKED_CAST")
    transform(values[0] as S1, values[1] as S2, values[2] as S3, values[3] as S4)
}

/**
 * Combines five StateFlows into a single StateFlow using a transform function.
 *
 * The resulting StateFlow will emit a new value whenever any of the source StateFlows
 * emit a new value. The initial value is computed from the current values of all five flows.
 *
 * @param flow1 The first StateFlow to combine
 * @param flow2 The second StateFlow to combine
 * @param flow3 The third StateFlow to combine
 * @param flow4 The fourth StateFlow to combine
 * @param flow5 The fifth StateFlow to combine
 * @param transform A function that takes the current values from all five flows and returns the combined result
 * @return A new StateFlow that emits the transformed values
 */
fun <S1, S2, S3, S4, S5, R> combineStateFlows(
    flow1: StateFlow<S1>,
    flow2: StateFlow<S2>,
    flow3: StateFlow<S3>,
    flow4: StateFlow<S4>,
    flow5: StateFlow<S5>,
    transform: (S1, S2, S3, S4, S5) -> R
): StateFlow<R> = CombinedStateFlow(flow1, flow2, flow3, flow4, flow5) { values ->
    @Suppress("UNCHECKED_CAST")
    transform(values[0] as S1, values[1] as S2, values[2] as S3, values[3] as S4, values[4] as S5)
}

/**
 * Combines any number of StateFlows into a single StateFlow using a transform function.
 *
 * The resulting StateFlow will emit a new value whenever any of the source StateFlows
 * emit a new value. The initial value is computed from the current values of all source flows.
 *
 * @param flows Variable number of StateFlows to combine (can be of different types)
 * @param transform A function that takes the current values from all flows and returns the combined result
 * @return A new StateFlow that emits the transformed values
 */
fun <R> combineStateFlows(
    vararg flows: StateFlow<*>,
    transform: (List<Any?>) -> R
): StateFlow<R> = CombinedStateFlow(*flows, transform = transform)


/**
 * A StateFlow that combines multiple StateFlows into a single StateFlow using a transform function.
 *
 * This class allows combining any number of StateFlows with potentially different types.
 * The transform function receives a list of values (as Any?) and must handle type casting manually.
 * The resulting StateFlow will have an initial value computed from the current values of all source StateFlows.
 *
 * Value of this [StateFlow] will be updated whenever any of the source StateFlows emit a new value.
 *
 * **Note:** this flow was not designed for performance and should not be used in performance-critical paths.
 * For most not too heavy use cases, it should work fine.
 *
 * @param flows Variable number of StateFlows to combine (can be of different types)
 * @param transform A function that receives a list of all current values and returns the combined result.
 *                  Values are provided as Any? and need to be cast to appropriate types.
 */
class CombinedStateFlow<R>(
    private vararg val flows: StateFlow<*>,
    private val transform: (List<Any?>) -> R
) : StateFlow<R> {

    override val replayCache: List<R>
        get() = listOf(value)

    override val value: R
        get() = transform(flows.map { it.value })

    override suspend fun collect(collector: FlowCollector<R>): Nothing {
        combine(flows.asList()) {
            transform(it.toList())
        }.collect {
            collector.emit(it)
        }
        // Should never reach here, as collect is a suspending function
        throw Exception()
    }

}