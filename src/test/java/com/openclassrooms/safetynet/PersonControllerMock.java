package com.openclassrooms.safetynet;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.when;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.web.client.RestTemplate;

import com.openclassrooms.safetynet.domain.InfoPerson;
import com.openclassrooms.safetynet.domain.Person;
import com.openclassrooms.safetynet.service.SafetyNetService;

@SpringBootTest(webEnvironment=SpringBootTest.WebEnvironment.RANDOM_PORT)
public class PersonControllerMock {

  private final RestTemplate restTemplate = new RestTemplate();

  @MockitoBean
  SafetyNetService personService;

  @Test
  @DirtiesContext
  public void testGetPersonInfoByLastName() throws Exception {
    // Arrange
    InfoPerson person1 = new InfoPerson("Jaden", "Soroka", "jadensoroka@gmail.com", List.of(), List.of());
    InfoPerson person2 = new InfoPerson("John", "Doe", "john.doe@gmail.com", List.of(), List.of("Amoxicillin", "Peanuts"));
    
    List<InfoPerson> mockPersons = List.of(person1, person2);
    
    when(personService.findPersonsByLastName("Boyd")).thenReturn(mockPersons);
    
    // Act
    ResponseEntity<List<Person>> response = restTemplate.exchange(
        "http://localhost:8080/personInfo?lastName=Boyd",
        HttpMethod.GET,
        null,
        new ParameterizedTypeReference<List<Person>>() {}
    );
    
    // Assert
    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertEquals(6, response.getBody().size());
    assertEquals("Boyd", response.getBody().get(0).lastName());
}
}
