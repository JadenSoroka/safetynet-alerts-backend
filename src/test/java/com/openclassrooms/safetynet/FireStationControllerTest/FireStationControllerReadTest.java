package com.openclassrooms.safetynet.FireStationControllerTest;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest
@DisplayName("Fire Station Controller Read Tests")
public class FireStationControllerReadTest {

    @Autowired
    private MockMvc mvc;

    @Nested
    @DisplayName("GET /firestation")
    class FireStationEndpointGet {
        @Test
        void GIVEN_valid_address_THEN_200_response_AND_return_fire_station() throws Exception {
            mvc.perform(get("/firestation/1509_culver_st"))
                .andExpectAll(
                    status().isOk(),
                    jsonPath("$.address").value("1509 Culver St"),
                    jsonPath("$.station").value(3)
                );
        }

        @Test
        void GIVEN_invalid_address_THEN_404_response() throws Exception {
            mvc.perform(get("/firestation/1509_culver_s"))
                .andExpect(status().isNotFound());
        }
    }
}
