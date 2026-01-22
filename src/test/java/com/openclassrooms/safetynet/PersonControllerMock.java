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

import com.openclassrooms.safetynet.domain.Person;
import com.openclassrooms.safetynet.service.SafetyNetService;

@SpringBootTest(webEnvironment=SpringBootTest.WebEnvironment.RANDOM_PORT)
public class PersonControllerMock {

  private final RestTemplate restTemplate = new RestTemplate();

  @MockitoBean
  SafetyNetService personService;

  @Test
  @DirtiesContext
  public void testGetAllPersonsByLastName() throws Exception {
    // Arrange
    Person person1 = new Person("Jaden", "Soroka", "123 Sesame St", "Cleveland", "67890", "123-456-7890", "jadensoroka@gmail.com");
    Person person2 = new Person("John", "Doe", "456 Main St", "Indianapolis", "34567", "456-789-0123", "john.doe@gmail.com");
    
    List<Person> mockPersons = List.of(person1, person2);
    
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
    assertEquals("Boyd", response.getBody().get(0).getLastName());
}
}
