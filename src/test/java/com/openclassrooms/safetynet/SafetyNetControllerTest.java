package com.openclassrooms.safetynet;


import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.in;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@AutoConfigureMockMvc
@SpringBootTest
public class SafetyNetControllerTest {

  @Autowired
  private MockMvc mvc;

  @Test
  void GIVEN_valid_station_number_THEN_200_response_AND_return_all_covered_persons() throws Exception {
    List<String> validAddresses = List.of("644 Gershwin Cir", "908 73rd St", "947 E. Rose Dr");
    
    this.mvc.perform(get("/firestation")
        .param("stationNumber", "1"))
      .andExpectAll(
        status().isOk(),
        content().contentType("application/json"),
        jsonPath("$.coveredPersons[0].address").value(is(in(validAddresses))),
        jsonPath("$.coveredPersons[-1].address").value(is(in(validAddresses)))
      );
  }

  @Test
  void GIVEN_valid_last_name_THEN_200_response_AND_returns_all_matching_persons() throws Exception {
    this.mvc.perform(get("/personInfo")
        .param("lastName", "Boyd"))
      .andExpectAll(
        status().isOk(),
        jsonPath("$[0].lastName").value("Boyd"),
        jsonPath("$[-1].lastName").value("Boyd")
      );
  }

  @Test
  void GIVEN_invalid_last_name_THEN_200_response_AND_returns_empty_array() throws Exception {
    this.mvc.perform(get("/personInfo")
        .param("lastName", "Soroka"))
      .andExpectAll(
        status().isOk(),
        content().contentType("application/json")
    );
  }

  @Test
  void GIVEN_valid_city_THEN_200_response_AND_returns_all_matching_emails() throws Exception {
    this.mvc.perform(get("/communityEmail")
        .param("city", "Culver"))
      .andExpectAll(
        status().isOk(),
        content().contentType("application/json")
      );
  }

  @Test
  void GIVEN_invalid_city_THEN_return_empty_array() throws Exception {
    this.mvc.perform(get("/communityEmail")
        .param("city", "Chicago"))
      .andExpect(status().isNotFound());
  }
}
