package com.vw.digitalhub.robotcleaner.application

import com.vw.digitalhub.robotcleaner.config.SimulationMetrics
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.mockito.kotlin.*

/**
 * Unit tests for SimulateService.
 * Uses a mocked SimulationMetrics instance to avoid touching Micrometer.
 */
class SimulateServiceTest {

    // Create a SimulationMetrics mock (so we don't need a full Spring context)
    private val metrics: SimulationMetrics = mock()

    // Inject the mock into the service
    private val service = SimulateService(metrics)

    @Test
    fun `sample case`() {
        // Test the standard sample case with two robots
        // Grid: 5x5, Robot1: starts at (1,2) North, moves: LMLMLMLMM
        // Robot2: starts at (3,3) East, moves: MMRMMRMRRM
        // Expected results with concatenated coordinates: "13 N" and "51 E"
        val input = """
            5 5
            1 2 N
            LMLMLMLMM
            3 3 E
            MMRMMRMRRM
        """.trimIndent()

        val expected = listOf("13 N", "51 E")

        // When
        val result = service.simulate(input)

        // Then
        assertEquals(expected, result)
        verify(metrics, atLeastOnce()).recordSimulation(any(), any())
    }

    @Test
    fun `ignore out-of-bounds`() {
        // Test boundary handling: robot tries to move south beyond grid boundaries
        // Grid: 1x1, Robot: starts at (0,0) South, moves: MMMM (attempts to move south 4 times)
        // Expected: robot stays at original position "00 S" since all moves are out-of-bounds
        val input = """
            1 1
            0 0 S
            MMMM
        """.trimIndent()

        val expected = listOf("00 S")

        // When
        val result = service.simulate(input)

        // Then
        assertEquals(expected, result)
        verify(metrics, atLeastOnce()).recordSimulation(any(), any())
    }
}
