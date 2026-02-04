package com.openclassrooms.safetynet;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.in;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.lessThan;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@AutoConfigureMockMvc
@SpringBootTest
@DisplayName("Person Controller Tests")
public class SafetyNetControllerTest {

  @Autowired
  private MockMvc mvc;

  @Nested
  @DisplayName("GET /firestation")
  class FireStationEndpoint {
    @Test
    void GIVEN_valid_station_number_THEN_200_response_AND_return_all_covered_persons() throws Exception {
      List<String> validAddresses = List.of("644 Gershwin Cir", "908 73rd St", "947 E. Rose Dr");
      
      mvc.perform(get("/firestation")
          .param("stationNumber", "1"))
        .andExpectAll(
          status().isOk(),
          content().contentType("application/json"),
          jsonPath("$.coveredPersons[0].address").value(is(in(validAddresses))),
          jsonPath("$.coveredPersons[-1].address").value(is(in(validAddresses))),
          jsonPath("$.adultCount").value(greaterThan(0)),
          jsonPath("$.childCount").value(greaterThanOrEqualTo(0))
        );
    }
  
    @Test
    void GIVEN_invalid_station_number_THEN_200_response_AND_returns_empty_array_and_zero_adults_and_zero_children() throws Exception {    
      mvc.perform(get("/firestation")
          .param("stationNumber", "99999"))
        .andExpectAll(
          status().isOk(),
          jsonPath("$.coveredPersons").isArray(),
          jsonPath("$.coveredPersons").isEmpty(),
          jsonPath("$.adultCount").value(0),
          jsonPath("$.childCount").value(0)
        );
    }
  }

  @Nested
  @DisplayName("GET /childAlert")
  class ChildAlertEndpoint {
    @Test
    void GIVEN_valid_address_THEN_200_response_AND_returns_all_children_and_their_family_members() throws Exception {
      mvc.perform(get("/childAlert")
          .param("address", "1509 Culver St"))
        .andExpectAll(
          status().isOk(),
          jsonPath("$[0].age").value(lessThan(18)),
          jsonPath("$[-1].age").value(lessThan(18))
        );
    }
  
    @Test
    void GIVEN_invalid_address_THEN_200_response_AND_returns_empty_array() throws Exception {
      mvc.perform(get("/childAlert")
          .param("address", "123 test st"))
        .andExpectAll(
          status().isOk(),
          jsonPath("$").isArray(),
          jsonPath("$").isEmpty()
        );
    }
  }

  @Nested
  @DisplayName("GET /phoneAlert")
  class PhoneAlertEndpoint {
    @Test
    void GIVEN_valid_firestation_number_THEN_200_response_AND_returns_all_covered_person_phone_numbers() throws Exception {
      mvc.perform(get("/phoneAlert")
          .param("firestation", "3"))
        .andExpectAll(
          status().isOk(),
          jsonPath("$[0]").isString(),
          jsonPath("$[-1]").isString()
        );
    }
  
    @Test
    void GIVEN_invalid_firestation_number_THEN_200_response_AND_returns_empty_array() throws Exception {
      mvc.perform(get("/phoneAlert")
          .param("firestation", "9999"))
        .andExpectAll(
          status().isOk(),
          jsonPath("$").isArray(),
          jsonPath("$").isEmpty()
        );
    }
  }

  @Nested
  @DisplayName("GET /fire")
  class FireEndpoint {
    @Test 
    void GIVEN_valid_address_THEN_200_response_AND_returns_station_number_and_list_of_covered_persons() throws Exception {
      mvc.perform(get("/fire")
          .param("address", "1509 Culver St"))
        .andExpectAll(
          status().isOk(),
          jsonPath("$.stationNumber").value(3),
          jsonPath("$.persons").isArray(),
          jsonPath("$.persons").isNotEmpty(),
          jsonPath("$.persons[0].firstName").isString()
        );
    }
  
    @Test 
    void GIVEN_invalid_address_THEN_404_response() throws Exception {
      mvc.perform(get("/fire")
          .param("address", "123 test road"))
        .andExpect(status().isNotFound());
    }
  }

  @Nested
  @DisplayName("GET /flood/stations")
  class FloodStationsEndpoint {
    @Test 
    void GIVEN_list_of_valid_station_numbers_THEN_200_response_AND_returns_all_covered_persons() throws Exception {
      
      mvc.perform(get("/flood/stations")
          .param("stations", "1", "2"))
        .andExpectAll(
          status().isOk(),
          jsonPath("$[0].stationNumber").value("1"),
          jsonPath("$[0].households").isNotEmpty(),
          jsonPath("$[1].stationNumber").value("2"),
          jsonPath("$[0].households").isNotEmpty()
        );
    }
  
    @Test 
    void GIVEN_invalid_station_numbers_THEN_200_response_AND_returns_station_number_and_empty_households_array() throws Exception {
      
      mvc.perform(get("/flood/stations")
          .param("stations", "99999", "2"))
        .andExpectAll(
          status().isOk(),
          jsonPath("$[0].stationNumber").value("99999"),
          jsonPath("$[0].households").isEmpty(),
          jsonPath("$[1].stationNumber").value("2"),
          jsonPath("$[1].households").isNotEmpty()
        );
    }
  }

  @Nested
  @DisplayName("GET /personInfo")
  class PersonInfoEndpoint {
    @Test
    void GIVEN_valid_last_name_THEN_200_response_AND_returns_all_matching_persons() throws Exception {
      mvc.perform(get("/personInfo")
          .param("lastName", "Boyd"))
        .andExpectAll(
          status().isOk(),
          jsonPath("$[0].lastName").value("Boyd"),
          jsonPath("$[-1].lastName").value("Boyd")
        );
    }
  
    @Test
    void GIVEN_invalid_last_name_THEN_200_response_AND_returns_empty_array() throws Exception {
      mvc.perform(get("/personInfo")
          .param("lastName", "Test"))
        .andExpectAll(
          status().isOk(),
          jsonPath("$").isArray(),
          jsonPath("$").isEmpty()
        );
    }
  }

  @Nested
  @DisplayName("GET /communityEmail")
  class CommunityEmailEndpoint {
    @Test
    void GIVEN_valid_city_THEN_200_response_AND_returns_all_matching_emails() throws Exception {
      mvc.perform(get("/communityEmail")
          .param("city", "Culver"))
        .andExpectAll(
          status().isOk(),
          jsonPath("$").isArray(),
          jsonPath("$[0]").isString(),
          jsonPath("$[-1]").isString()
        );
    }
  
    @Test
    void GIVEN_invalid_city_THEN_200_response_AND_returns_empty_array() throws Exception {
      mvc.perform(get("/communityEmail")
          .param("city", "Chicago"))
        .andExpectAll(
          status().isOk(),
          jsonPath("$").isArray(),
          jsonPath("$").isEmpty()
        );
    }
  }
}
