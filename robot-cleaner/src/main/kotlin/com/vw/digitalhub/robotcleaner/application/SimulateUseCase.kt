package com.vw.digitalhub.robotcleaner.application

/** Port defining simulation behavior. */
interface SimulateUseCase {
    fun simulate(input: String): List<String>
}
