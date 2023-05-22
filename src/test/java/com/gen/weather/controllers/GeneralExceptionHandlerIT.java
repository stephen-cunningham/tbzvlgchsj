package com.gen.weather.controllers;

import com.gen.weather.exceptions.NotFoundException;
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
import org.springframework.core.convert.ConversionFailedException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@WebMvcTest
class GeneralExceptionHandlerIT {
    private static final String SAMPLE_ERROR_MESSAGE = "sample error message";

    @Autowired private MockMvc mockMvc;

    @MockBean private TestService testService;

    @Mock private HttpMessageNotReadableException httpMessageNotReadableException;
    @Mock private DataIntegrityViolationException dataIntegrityViolationException;
    @Mock private ConversionFailedException conversionFailedException;
    @Mock private IllegalArgumentException illegalArgumentException;
    @Mock private ResourceNotFoundException resourceNotFoundException;
    @Mock private NotFoundException notFoundException;
    @Mock private UnexpectedException unexpectedException;

    @ParameterizedTest
    @MethodSource("handleBadRequestsParams")
    void anyController_HttpMessageNotReadableException_HandlesCorrectly(String thrownMessage, String expectedMessage) throws Exception {
        when(testService.test()).thenThrow(httpMessageNotReadableException);
        when(httpMessageNotReadableException.getMessage()).thenReturn(thrownMessage);

        performGetRequestAndAssertHandledCorrectly(GeneralExceptionHandler.BAD_REQUEST_CODE, expectedMessage, 400);
    }

    static Stream<Arguments> handleBadRequestsParams() {
                return Stream.of(
                Arguments.arguments(SAMPLE_ERROR_MESSAGE, SAMPLE_ERROR_MESSAGE),
                Arguments.arguments(null, "The supplied input is invalid.")
        );
    }

    private void performGetRequestAndAssertHandledCorrectly(String expectedCode, String expectedMessage, int expectedStatus) throws Exception {
        MockHttpServletResponse response = mockMvc.perform(get("/test/testing")).andReturn().getResponse();

        JSONObject resultJson = new JSONObject(response.getContentAsString());

        assertAll(
                () -> assertEquals(expectedCode, resultJson.get("code")),
                () -> assertEquals(expectedMessage, resultJson.getString("message")),
                () -> assertEquals(expectedStatus, response.getStatus())
        );
    }

    @Test
    void anyController_DataIntegrityViolationException_HandlesCorrectly() throws Exception {
        when(testService.test()).thenThrow(dataIntegrityViolationException);
        when(dataIntegrityViolationException.getMessage()).thenReturn(SAMPLE_ERROR_MESSAGE);

        performGetRequestAndAssertHandledCorrectly(GeneralExceptionHandler.BAD_REQUEST_CODE,  SAMPLE_ERROR_MESSAGE, 400);
    }

    @Test
    void anyController_ConversionFailedException_HandlesCorrectly() throws Exception {
        when(testService.test()).thenThrow(conversionFailedException);
        when(conversionFailedException.getMessage()).thenReturn(SAMPLE_ERROR_MESSAGE);

        performGetRequestAndAssertHandledCorrectly(GeneralExceptionHandler.BAD_REQUEST_CODE, SAMPLE_ERROR_MESSAGE, 400);
    }

    @Test
    void anyController_IllegalArgumentException_HandlesCorrectly() throws Exception {
        when(testService.test()).thenThrow(illegalArgumentException);
        when(illegalArgumentException.getMessage()).thenReturn(SAMPLE_ERROR_MESSAGE);

        performGetRequestAndAssertHandledCorrectly(GeneralExceptionHandler.BAD_REQUEST_CODE, SAMPLE_ERROR_MESSAGE, 400);
    }

    @Test
    void anyController_ResourceNotFoundException_HandlesCorrectly() throws Exception {
        when(testService.test()).thenThrow(resourceNotFoundException);
        when(resourceNotFoundException.getMessage()).thenReturn(SAMPLE_ERROR_MESSAGE);

        performGetRequestAndAssertHandledCorrectly(GeneralExceptionHandler.NOT_FOUND_CODE, "The resource was not found", 404);
    }

    @Test
    void anyController_NotFoundException_HandlesCorrectly() throws Exception {
        when(testService.test()).thenThrow(notFoundException);
        when(notFoundException.getMessage()).thenReturn(SAMPLE_ERROR_MESSAGE);

        performGetRequestAndAssertHandledCorrectly(GeneralExceptionHandler.NOT_FOUND_CODE, SAMPLE_ERROR_MESSAGE, 404);
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