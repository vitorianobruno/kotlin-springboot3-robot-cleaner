package com.vw.digitalhub.robotcleaner

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

/**
 * Entry point for Robot Cleaner application using Spring Boot 3 and Kotlin.
 */
@SpringBootApplication
open class RobotCleanerApplication

fun main(args: Array<String>) {
    runApplication<RobotCleanerApplication>(*args)
}
