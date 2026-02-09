package com.openclassrooms.safetynet.MedicalRecordControllerTest;

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
@DisplayName("Medical Record Controller Read Tests")
public class MedicalRecordControllerReadTest {

    @Autowired
    private MockMvc mvc;

    @Nested
    @DisplayName("GET /medicalRecord")
    class MedicalRecordEndpointGet {
        @Test
        void GIVEN_valid_firstLastName_THEN_200_response_AND_return_fire_station() throws Exception {
            mvc.perform(get("/medicalRecord/John_Boyd"))
                .andExpectAll(
                    status().isOk(),
                    jsonPath("$.firstName").value("John"),
                    jsonPath("$.lastName").value("Boyd"),
                    jsonPath("$.birthdate").value("03/06/1984")
                );
        }

        @Test
        void GIVEN_invalid_firstLastName_THEN_404_response() throws Exception {
            mvc.perform(get("/medicalRecord/Fake_Name"))
                .andExpect(status().isNotFound());
        }
    }
}
