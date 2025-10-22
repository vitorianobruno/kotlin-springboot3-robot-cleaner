package com.vw.digitalhub.robotcleaner.config

import io.micrometer.core.instrument.Counter
import io.micrometer.core.instrument.Metrics
import io.micrometer.core.instrument.Timer
import org.springframework.stereotype.Component
import java.util.concurrent.TimeUnit

@Component
class SimulationMetrics {

    private val simulatorRequests = Counter.builder("simulator.requests")
        .description("Total simulation requests")
        .register(Metrics.globalRegistry)

    private val simulatorErrors = Counter.builder("simulator.errors")
        .description("Total simulation errors")
        .register(Metrics.globalRegistry)

    private val simulationDuration = Timer.builder("simulator.duration")
        .description("Simulation processing time")
        .register(Metrics.globalRegistry)

    fun recordSimulation(duration: Long, success: Boolean) {
        simulatorRequests.increment()
        if (!success) simulatorErrors.increment()
        simulationDuration.record(duration, TimeUnit.MILLISECONDS)
    }
}