package com.vw.digitalhub.robotcleaner.application

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class SimulateServiceTest {
    private val service = SimulateService()

    @Test
    fun `sample case`() {
        // Test the standard sample case with two robots
        // Grid: 5x5, Robot1: starts at (1,2) North, moves: LMLMLMLMM
        // Robot2: starts at (3,3) East, moves: MMRMMRMRRM
        // Expected results with concatenated coordinates: "13 N" and "51 E"
        val input = """5 5
                    1 2 N
                    LMLMLMLMM
                    3 3 E
                    MMRMMRMRRM"""
        assertEquals(listOf("13 N", "51 E"), service.simulate(input))
    }

    @Test
    fun `ignore out-of-bounds`() {
        // Test boundary handling: robot tries to move south beyond grid boundaries
        // Grid: 1x1, Robot: starts at (0,0) South, moves: MMMM (attempts to move south 4 times)
        // Expected: robot stays at original position "00 S" since all moves are out-of-bounds
        val input = """1 1
                    0 0 S
                    MMMM"""
        assertEquals(listOf("00 S"), service.simulate(input))
    }
}