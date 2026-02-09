package com.openclassrooms.safetynet.PersonControllerTest;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import tools.jackson.databind.ObjectMapper;

import com.openclassrooms.safetynet.domain.Person;
import com.openclassrooms.safetynet.service.PersonService;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@AutoConfigureMockMvc
@SpringBootTest
@DisplayName("Person Controller Write Tests")
public class PersonControllerWriteTest {

  @Autowired
  private MockMvc mvc;

  @MockitoBean
  private PersonService personService;

  @Autowired
  private ObjectMapper objectMapper;

  @Nested
  @DisplayName("POST /person")
  class PersonEndpointPost {
    @Test
    void GIVEN_valid_person_THEN_201_response_AND_return_person() throws Exception {
      Person mockPerson = new Person("Jaden", "Soroka", "123 Sesame St", "Cleveland", "44103", "123-456-7890", "jaden@gmail.com");
      when(personService.createPerson(any())).thenReturn(mockPerson);
      
      mvc.perform(post("/person")
          .content(objectMapper.writeValueAsString(mockPerson))
          .contentType("application/json"))
        .andExpectAll(
          status().isCreated(),
          jsonPath("$.firstName").value("Jaden"),
          jsonPath("$.lastName").value("Soroka")
        );
    }
  }


  @Nested
  @DisplayName("PUT /person")
  class PersonEndpointPut {
    @Test
    void GIVEN_valid_person_on_update_THEN_204_response() throws Exception {
      Person mockPerson = new Person("Eric", "Cadigan", "951 LoneTree Rd", "Culver", "97451", "123-456-7890", "gramps@email.com");
      when(personService.updatePerson(any(), any())).thenReturn(true);
      
      mvc.perform(put("/person/Eric_Cadigan")
          .content(objectMapper.writeValueAsString(mockPerson))
          .contentType("application/json"))
        .andExpect(status().isNoContent());
    }

    @Test
    void GIVEN_invalid_person_on_update_THEN_404_response() throws Exception {
      Person mockPerson = new Person("Eric", "Cadigan", "951 LoneTree Rd", "Culver", "97451", "123-456-7890", "gramps@email.com");
      when(personService.updatePerson(any(), any())).thenReturn(false);
      
      mvc.perform(put("/person/Eric_Cadigan")
          .content(objectMapper.writeValueAsString(mockPerson))
          .contentType("application/json"))
        .andExpect(status().isNotFound());
    }
  }

  @Nested
  @DisplayName("DELETE /person")
  class PersonEndpointDelete {
    @Test
    void GIVEN_valid_person_on_delete_THEN_204_response() throws Exception {
      when(personService.deletePerson(any())).thenReturn(true);
  
      mvc.perform(delete("/person/Eric_Cadigan"))
        .andExpect(status().isNoContent());
    }

    @Test
    void GIVEN_invalid_person_on_delete_THEN_204_response() throws Exception {
      when(personService.deletePerson(any())).thenReturn(false);
  
      mvc.perform(delete("/person/Fake_Name"))
        .andExpect(status().isNotFound());
    }
  }
}