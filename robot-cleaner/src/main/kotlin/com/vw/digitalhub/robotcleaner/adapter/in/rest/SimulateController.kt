package com.vw.digitalhub.robotcleaner.adapter.`in`.rest

import com.vw.digitalhub.robotcleaner.application.SimulateUseCase
import org.slf4j.LoggerFactory
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.*

/** REST endpoint for simulation (text/plain). */
@RestController
@RequestMapping("/api/simulate")
class SimulateController(private val useCase: SimulateUseCase) {

    private val logger = LoggerFactory.getLogger(SimulateController::class.java)

    @PostMapping(consumes = [MediaType.TEXT_PLAIN_VALUE], produces = [MediaType.TEXT_PLAIN_VALUE])
    fun simulate(@RequestBody body: String): String {
        logger.info("Received simulation request: ${body.take(100)}...")

        return try {
            val result = useCase.simulate(body).joinToString("\n")
            logger.info("Simulation completed successfully. Result: $result")
            result
        } catch (e: Exception) {
            logger.error("Simulation failed with error: ${e.message}", e)
            throw e
        }
    }
}

