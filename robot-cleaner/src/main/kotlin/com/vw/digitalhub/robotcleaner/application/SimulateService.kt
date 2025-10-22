package com.vw.digitalhub.robotcleaner.application

import com.vw.digitalhub.robotcleaner.domain.*
import org.slf4j.LoggerFactory

/**
 * Service implementing the simulation logic.
 */
@org.springframework.stereotype.Service
class SimulateService : SimulateUseCase {

    private val logger = LoggerFactory.getLogger(SimulateService::class.java)

    override fun simulate(input: String): List<String> {
        logger.debug("Processing simulation input")
        val lines=input.lines().map{it.trim()}.filter{it.isNotEmpty()}
        if(lines.isEmpty()) return emptyList()
        val boundsParts=lines[0].split(" ")
        val bounds=Position(boundsParts[0].toInt(),boundsParts[1].toInt())
        val result= mutableListOf<String>()
        var i=1
        while(i<lines.size){
            val (x,y,d)=lines[i++].split(" ")
            val robot=Robot(Position(x.toInt(),y.toInt()),Direction.valueOf(d))
            val moves=lines[i++].trim().uppercase()
            for(c in moves) robot.execute(c,bounds)
            result.add(robot.toString())
        }
        logger.info("Processed ${result.size} robots")
        return result
    }
}
