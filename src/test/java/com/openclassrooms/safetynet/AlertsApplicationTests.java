package com.openclassrooms.safetynet;

import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class AlertsApplicationTests {

	// @Autowired
	// TestRestTemplate restTemplate;

	// @Test
	// void shouldReturnPersonWhenGivenLastName() {
	// 	ResponseEntity<String> response = restTemplate
	// 		.getForEntity("/person/boyd", String.class);
	// 	assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
		
	// 	DocumentContext documentContext = JsonPath.parse(response.getBody());
	// 	String fname = documentContext.read("$.firstName");
	// 	assertThat(fname).contains("boyd");
	// }

}
