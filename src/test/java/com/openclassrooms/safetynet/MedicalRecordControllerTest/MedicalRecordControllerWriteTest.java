package com.openclassrooms.safetynet.MedicalRecordControllerTest;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import com.openclassrooms.safetynet.domain.MedicalRecord;
import com.openclassrooms.safetynet.service.MedicalRecordService;

import tools.jackson.databind.ObjectMapper;

@AutoConfigureMockMvc
@SpringBootTest
@DisplayName("Medical Record Controller Write Tests")
public class MedicalRecordControllerWriteTest {

    @Autowired
    private MockMvc mvc;

    @MockitoBean
    private MedicalRecordService medicalRecordService;

    @Autowired
    private ObjectMapper objectMapper;

    @Nested
    @DisplayName("POST /medicalRecord")
    class MedicalRecordEndpointPost {
        @Test
        void GIVEN_valid_name_THEN_201_response_AND_return_person() throws Exception {
            MedicalRecord mockMedicalRecord = new MedicalRecord("Jaden", "Soroka", "11/05/2001", List.of(), List.of());
            when(medicalRecordService.createMedicalRecord(any())).thenReturn(mockMedicalRecord);

            mvc.perform(post("/medicalRecord")
                    .content(objectMapper.writeValueAsString(mockMedicalRecord))
                    .contentType("application/json"))
                .andExpectAll(
                    status().isCreated(),
                    jsonPath("$.firstName").value("Jaden"),
                    jsonPath("$.lastName").value("Soroka")
                );
        }
    }

    @Nested
    @DisplayName("PUT /medicalRecord")
    class MedicalRecordEndpointPut {
        @Test
        void GIVEN_valid_name_and_medical_record_ON_update_THEN_204_response() throws Exception {
            MedicalRecord mockMedicalRecord = new MedicalRecord("Jaden", "Soroka", "11/05/2001", List.of(), List.of());
            when(medicalRecordService.updateMedicalRecord(any(), any())).thenReturn(true);

            mvc.perform(put("/medicalRecord/Jaden_Soroka")
                    .content(objectMapper.writeValueAsString(mockMedicalRecord))
                    .contentType("application/json"))
                .andExpect(status().isNoContent());
        }

        @Test
        void GIVEN_invalid_name_ON_update_THEN_404_response() throws Exception {
            MedicalRecord mockMedicalRecord = new MedicalRecord("Jaden", "Soroka", "11/05/2001", List.of(), List.of());
            when(medicalRecordService.updateMedicalRecord(any(), any())).thenReturn(false);
        
            mvc.perform(put("/medicalRecord/fake_name")
                    .content(objectMapper.writeValueAsString(mockMedicalRecord))
                    .contentType("application/json"))
                .andExpect(status().isNotFound());
        }
    }

    @Nested
    @DisplayName("DELETE /medicalRecord")
    class MedicalRecordEndpointDelete {
        @Test
        void GIVEN_valid_name_ON_delete_THEN_204_response() throws Exception {
            MedicalRecord mockMedicalRecord = new MedicalRecord("Jaden", "Soroka", "11/05/2001", List.of(), List.of());
            when(medicalRecordService.deleteMedicalRecord(any())).thenReturn(true);
        
            mvc.perform(delete("/medicalRecord/Jaden_Soroka")
                    .content(objectMapper.writeValueAsString(mockMedicalRecord))
                    .contentType("application/json"))
                .andExpect(status().isNoContent());
        }

        @Test
        void GIVEN_invalid_name_ON_delete_THEN_204_response() throws Exception {
            MedicalRecord mockMedicalRecord = new MedicalRecord("Jaden", "Soroka", "11/05/2001", List.of(), List.of());
            when(medicalRecordService.deleteMedicalRecord(any())).thenReturn(false);
        
            mvc.perform(delete("/medicalRecord/fake_name")
                    .content(objectMapper.writeValueAsString(mockMedicalRecord))
                    .contentType("application/json"))
                .andExpect(status().isNotFound());
        }
    }
}
