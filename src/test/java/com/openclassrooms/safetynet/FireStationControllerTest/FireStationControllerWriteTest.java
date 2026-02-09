package com.openclassrooms.safetynet.FireStationControllerTest;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.openclassrooms.safetynet.domain.FireStation;
import com.openclassrooms.safetynet.service.FireStationService;

import tools.jackson.databind.ObjectMapper;

@AutoConfigureMockMvc
@SpringBootTest
@DisplayName("Fire Station Controller Write Tests")
public class FireStationControllerWriteTest {

    @Autowired
    private MockMvc mvc;

    @MockitoBean
    private FireStationService fireStationService;

    @Autowired
    private ObjectMapper objectMapper;

    @Nested
    @DisplayName("POST /firestation")
    class FireStationEndpointPost {
        @Test
        void GIVEN_valid_address_THEN_201_response_AND_return_fire_station() throws Exception {
            FireStation mockFireStation = new FireStation("123 Sesame St", "5");
            when(fireStationService.createFireStation(any())).thenReturn(mockFireStation);

            mvc.perform(post("/firestation")
                    .content(objectMapper.writeValueAsString(mockFireStation))
                    .contentType("application/json"))
                .andExpectAll(
                    status().isCreated(),
                    jsonPath("$.address").value("123 Sesame St"),
                    jsonPath("$.station").value("5")
                );
        }
    }

    @Nested
    @DisplayName("PUT /firestation")
    class FireStationEndpointPut {
        @Test
        void GIVEN_valid_address_THEN_204_response() throws Exception {
            FireStation mockFireStation = new FireStation("123 Sesame St", "5");
            when(fireStationService.updateFireStation(any(), any())).thenReturn(true);

            mvc.perform(put("/firestation/123_Sesame_st")
                    .content(objectMapper.writeValueAsString(mockFireStation))
                    .contentType("application/json"))
                .andExpect(status().isNoContent());
        }

        @Test
        void GIVEN_invalid_address_THEN_404_response() throws Exception {
            FireStation mockFireStation = new FireStation("123 Sesame S", "5");
            when(fireStationService.updateFireStation(any(), any())).thenReturn(false);

            mvc.perform(put("/firestation/123_Sesame_s")
                    .content(objectMapper.writeValueAsString(mockFireStation))
                    .contentType("application/json"))
                .andExpect(status().isNotFound());
        }
    }

    @Nested
    @DisplayName("DELETE /firestation")
    class FireStationEndpointDelete {
        @Test
        void GIVEN_valid_address_THEN_204_response() throws Exception {
            when(fireStationService.deleteFireStation(any())).thenReturn(true);

            mvc.perform(delete("/firestation/123_Sesame_st"))
                .andExpect(status().isNoContent());
        }

        @Test
        void GIVEN_invalid_address_THEN_404_response() throws Exception {
            when(fireStationService.deleteFireStation(any())).thenReturn(false);

            mvc.perform(delete("/firestation/123_Sesame_s"))
                .andExpect(status().isNotFound());
        }
    }
}
