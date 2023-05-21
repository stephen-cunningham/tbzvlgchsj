package com.gen.weather.controllers;

import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@WebMvcTest
class GeneralExceptionHandlerIT {
    @Autowired private MockMvc mockMvc;

    @MockBean private TestService testService;

    @Mock private HttpMessageNotReadableException httpMessageNotReadableException;
    @Mock private UnexpectedException unexpectedException;

    @ParameterizedTest
    @MethodSource("httpMessageNotReadableExceptionThrownParams")
    void anyController_HttpMessageNotReadableException_HandlesCorrectly(String thrownMessage, String expectedMessage) throws Exception {
        when(testService.test()).thenThrow(httpMessageNotReadableException);
        when(httpMessageNotReadableException.getMessage()).thenReturn(thrownMessage);

        MockHttpServletResponse response = mockMvc.perform(get("/test/testing")).andReturn().getResponse();

        JSONObject resultJson = new JSONObject(response.getContentAsString());

        assertAll(
                () -> assertEquals(GeneralExceptionHandler.BAD_REQUEST_CODE, resultJson.get("code")),
                () -> assertEquals(expectedMessage, resultJson.getString("message")),
                () -> assertEquals(400, response.getStatus())
        );
    }

    static Stream<Arguments> httpMessageNotReadableExceptionThrownParams() {
        String sampleMessage = "sample error message";
        return Stream.of(
                Arguments.arguments(sampleMessage, sampleMessage),
                Arguments.arguments(null, "The supplied input is invalid.")
        );
    }

    @Test
    void anyController_UnexpectedException_HandlesCorrectly() throws Exception {
        when(testService.test()).thenThrow(unexpectedException);

        MockHttpServletResponse response = mockMvc.perform(get("/test/testing")).andReturn().getResponse();

        JSONObject resultJson = new JSONObject(response.getContentAsString());

        assertAll(
                () -> assertEquals("unexpected.error", resultJson.get("code")),
                () -> assertEquals("An unexpected error has occurred. Please contact an administrator.", resultJson.getString("message")),
                () -> assertEquals(500, response.getStatus())
        );
    }

    @RestController
    @RequestMapping("/test")
    public static class TestController {
        @Autowired private TestService testService;

        @GetMapping("/testing")
        public String test() {
            return testService.test();
        }
    }

    static class TestService {
        public String test() {
            return "";
        }

    }

    @TestConfiguration
    static class TestConfig {
        @Bean
        public TestController testController() {
            return new TestController();
        }

        @Bean
        public TestService testService() {
            return new TestService();
        }
    }

    static class UnexpectedException extends RuntimeException {}
}