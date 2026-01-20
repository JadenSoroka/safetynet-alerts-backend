package com.openclassrooms.safetynet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.resttestclient.TestRestTemplate;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import com.openclassrooms.safetynet.service.PersonService;

@SpringBootTest(webEnvironment=SpringBootTest.WebEnvironment.RANDOM_PORT)
public class PersonControllerMock {
  @Autowired
  TestRestTemplate restTemplate;

  @MockitoBean
  PersonService personService;

  // @Test
  // public void testGetAllPersonsByLastName() throws Exception {
  //   ResponseEntity<List<Person>> response = restTemplate
  //     .getForEntity("/personInfoLastName=Boyd", List<Person>.class);
}
