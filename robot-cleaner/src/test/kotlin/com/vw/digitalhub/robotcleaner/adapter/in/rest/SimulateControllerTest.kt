package com.vw.digitalhub.robotcleaner.adapter.`in`.rest

import org.junit.jupiter.api.Test
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*

@SpringBootTest
@AutoConfigureMockMvc
class SimulateControllerTest(@Autowired val mvc: MockMvc) {

    @Test
    fun `endpoint works`() {
        // Test input simulating two robots with their initial positions and movement commands
        val input = """5 5
                    1 2 N
                    LMLMLMLMM
                    3 3 E
                    MMRMMRMRRM"""

        // Perform POST request to the simulation endpoint and verify:
        // 1. HTTP status is 200 OK
        // 2. Response content matches expected robot final positions without spaces between coordinates
        //    First robot: "13 N" (position 1,3 facing North)
        //    Second robot: "51 E" (position 5,1 facing East)
        mvc.perform(post("/api/simulate")
            .contentType(MediaType.TEXT_PLAIN)
            .content(input))
            .andExpect(status().isOk)
            .andExpect(content().string("13 N\n51 E"))
    }
}