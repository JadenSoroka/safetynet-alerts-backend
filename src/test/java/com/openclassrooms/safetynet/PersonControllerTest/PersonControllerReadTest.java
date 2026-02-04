package com.openclassrooms.safetynet.PersonControllerTest;

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
public class PersonControllerReadTest {

  @Autowired
  private MockMvc mvc;

  // Test GET

  @Test
  void GIVEN_valid_firstLastName_THEN_200_response_AND_return_person() throws Exception {
    this.mvc.perform(get("/person/Eric_Cadigan"))
      .andExpectAll(
        status().isOk(),
        jsonPath("$.firstName").value("Eric"),
        jsonPath("$.lastName").value("Cadigan"),
        jsonPath("$.phone").value("841-874-7458")
      );
  }

  @Test
  void GIVEN_invalid_firstLastName_THEN_404_response_AND_return_person() throws Exception {
    this.mvc.perform(get("/person/fakename"))
      .andExpect(status().isNotFound());
  }
}
