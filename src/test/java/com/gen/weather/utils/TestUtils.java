package com.gen.weather.utils;

import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

public class TestUtils {
    public static MockHttpServletResponse performGetRequest(MockMvc mockMvc, String uri) throws Exception {
        return mockMvc.perform(get(uri)).andReturn().getResponse();
    }
}
