package com.vw.digitalhub.robotcleaner.application

import com.vw.digitalhub.robotcleaner.domain.*
import com.vw.digitalhub.robotcleaner.config.SimulationMetrics
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

/**
 * Service implementing the simulation logic.
 *
 * This version includes observability metrics using SimulationMetrics.
 * It records total requests, errors, and processing duration.
 */
@Service
class SimulateService(
    private val metrics: SimulationMetrics // Inject metrics component
) : SimulateUseCase {

    private val logger = LoggerFactory.getLogger(SimulateService::class.java)

    override fun simulate(input: String): List<String> {
        val startTime = System.currentTimeMillis()
        var success = true

        return try {
            logger.debug("Processing simulation input")

            val lines = input.lines().map { it.trim() }.filter { it.isNotEmpty() }
            if (lines.isEmpty()) return emptyList()

            val boundsParts = lines[0].split(" ")
            val bounds = Position(boundsParts[0].toInt(), boundsParts[1].toInt())

            val result = mutableListOf<String>()
            var i = 1
            while (i < lines.size) {
                val (x, y, d) = lines[i++].split(" ")
                val robot = Robot(Position(x.toInt(), y.toInt()), Direction.valueOf(d))
                val moves = lines[i++].trim().uppercase()
                for (c in moves) robot.execute(c, bounds)
                result.add(robot.toString())
            }

            logger.info("Processed ${result.size} robots successfully")
            result
        } catch (ex: Exception) {
            success = false
            logger.error("Simulation failed: ${ex.message}", ex)
            throw ex
        } finally {
            // Always record metrics, whether success or failure
            val duration = System.currentTimeMillis() - startTime
            metrics.recordSimulation(duration, success)
            logger.debug("Simulation metrics recorded: duration=${duration}ms, success=$success")
        }
    }
}
