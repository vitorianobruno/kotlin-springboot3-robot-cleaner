package com.vw.digitalhub.robotcleaner.adapter.`in`.rest

import com.vw.digitalhub.robotcleaner.application.SimulateUseCase
import org.junit.jupiter.api.Test
import org.mockito.BDDMockito.given
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*

/**
 * Controller layer test using @WebMvcTest and Mockito.
 * It focuses only on SimulateController, mocking SimulateUseCase.
 */
@WebMvcTest(SimulateController::class)
class SimulateControllerTest(@Autowired val mvc: MockMvc) {

    // Mock the use case dependency injected into the controller
    @MockBean
    private lateinit var useCase: SimulateUseCase

    @Test
    fun `endpoint works`() {
        // Given: sample input and expected service response
        val input = """
            5 5
            1 2 N
            LMLMLMLMM
            3 3 E
            MMRMMRMRRM
        """.trimIndent()
        val expectedResult = listOf("13 N", "51 E")

        // Configure mock behavior: when simulate() is called, return expectedResult
        given(useCase.simulate(input)).willReturn(expectedResult)

        // When + Then: perform POST and verify response
        // 1. HTTP status is 200 OK
        // 2. Response content matches expected robot final positions
        //    First robot: "13 N" (position 1,3 facing North)
        //    Second robot: "51 E" (position 5,1 facing East)
        mvc.perform(post("/api/simulate")
            .contentType(MediaType.TEXT_PLAIN)
            .content(input))
            .andExpect(status().isOk)
            .andExpect(content().string("13 N\n51 E"))
    }
}
