package com.aviekinvlad.sportsbetting.controller;

import com.aviekinvlad.sportsbetting.config.TestMassage;
import com.aviekinvlad.sportsbetting.model.Auto;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@TestPropertySource({
        "classpath:test.error.massage.properties",
        "classpath:test.validation.massage.properties"
})
class RatesControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private TestMassage testMessage;
    @Autowired
    private ObjectMapper objectMapper;

    @SneakyThrows
    @Test
    void getRatesBrand() {
        this.mockMvc.perform(get("/api/v1/rates").param("brand", "audi"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.brand").value("Audi"));
    }

    @SneakyThrows
    @Test
    void getRatesBrandError() {
        final String massage = testMessage.massage("error.auto.brand.empty", "audii");

        this.mockMvc.perform(get("/api/v1/rates").param("brand", "audii"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value(massage));
    }

    @SneakyThrows
    @Test
    void acceptsBets() {
        final Auto auto = new Auto("BMW", BigDecimal.valueOf(42.0));
        final String data = objectMapper.writeValueAsString(auto);

        this.mockMvc.perform(post("/api/v1/rates")
                        .content(data)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.brand").value("BMW"))
                .andExpect(jsonPath("$.amount").value("42.00"));


        this.mockMvc.perform(post("/api/v1/rates")
                        .content(data)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.brand").value("BMW"))
                .andExpect(jsonPath("$.amount").value("84.00"));

    }

    @SneakyThrows
    @Test
    void acceptsBetsValidationNotNull() {
        final String massageBrand = testMessage.massage("validate.auto.brand");
        final String massageAmount = testMessage.massage("validate.auto.amount");

        this.mockMvc.perform(post("/api/v1/rates")
                        .content("{}")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.brand").value(massageBrand))
                .andExpect(jsonPath("$.amount").value(massageAmount));

    }

    @SneakyThrows
    @Test
    void acceptsBetsValidationMinAmount() {
        final String massage = testMessage.massage("validate.auto.amount.min");

        final Auto auto = new Auto("BMW", BigDecimal.valueOf(-42.0));
        final String data = objectMapper.writeValueAsString(auto);

        this.mockMvc.perform(post("/api/v1/rates")
                        .content(data)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.amount").value(massage));

    }

    @SneakyThrows
    @Test
    void acceptsBetsError() {
        final String massage = testMessage.massage("error.auto.brand.empty", "LaFerrari");

        final Auto auto = new Auto("LaFerrari", BigDecimal.valueOf(42.0));

        final String data = objectMapper.writeValueAsString(auto);

        this.mockMvc.perform(post("/api/v1/rates")
                        .content(data)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value(massage));

    }
}