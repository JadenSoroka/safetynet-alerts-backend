package com.openclassrooms.safetynet.PersonRepositoryTest;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.openclassrooms.safetynet.domain.Person;
import com.openclassrooms.safetynet.repository.PersonRepository;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@DisplayName("Person Repository Read Tests")
public class PersonRepositoryReadTest {

    @Autowired
    private PersonRepository personRepository;

    @Nested
    @DisplayName("readPerson")
    class ReadPerson {
        @Test
        void GIVEN_valid_first_and_last_name_THEN_return_person() {
            // Given
            String name = "Jacob Boyd";

            // When
            Person result = personRepository.readPerson(name);

            // Then
            assertNotNull(result);
            assertEquals("Jacob", result.firstName());
            assertEquals("Boyd", result.lastName());
            assertEquals("1509 Culver St", result.address());
            assertEquals("Culver", result.city());
            assertEquals("97451", result.zip());
            assertEquals("841-874-6513", result.phone());
            assertEquals("drk@email.com", result.email());
        }

        @Test
        void GIVEN_invalid_name_THEN_return_null() {
            // Given
            String name = "Nonexistent Person";

            // When
            Person result = personRepository.readPerson(name);

            // Then
            assertNull(result);
        }

        @Test
        void GIVEN_name_with_different_case_THEN_return_person() {
            // Given
            String name = "JACOB BOYD"; // Uppercase

            // When
            Person result = personRepository.readPerson(name);

            // Then
            assertNotNull(result);
            assertEquals("Jacob", result.firstName());
            assertEquals("Boyd", result.lastName());
            assertEquals("1509 Culver St", result.address());
        }

        @Test
        void GIVEN_another_valid_name_THEN_return_correct_person() {
            // Given
            String name = "Sophia Zemicks";

            // When
            Person result = personRepository.readPerson(name);

            // Then
            assertNotNull(result);
            assertEquals("Sophia", result.firstName());
            assertEquals("Zemicks", result.lastName());
            assertEquals("892 Downing Ct", result.address());
            assertEquals("841-874-7878", result.phone());
        }

        @Test
        void GIVEN_name_with_mixed_case_THEN_return_person() {
            // Given
            String name = "sOpHiA zEmIcKs"; // Mixed case

            // When
            Person result = personRepository.readPerson(name);

            // Then
            assertNotNull(result);
            assertEquals("Sophia", result.firstName());
            assertEquals("Zemicks", result.lastName());
            assertEquals("892 Downing Ct", result.address());
        }

        @Test
        void GIVEN_empty_name_THEN_return_null() {
            // Given
            String name = "";

            // When
            Person result = personRepository.readPerson(name);

            // Then
            assertNull(result);
        }

        @Test
        void GIVEN_valid_name_THEN_return_person_with_all_fields() {
            // Given
            String name = "Warren Zemicks";

            // When
            Person result = personRepository.readPerson(name);

            // Then
            assertNotNull(result);
            assertEquals("Warren", result.firstName());
            assertEquals("Zemicks", result.lastName());
            assertNotNull(result.address());
            assertNotNull(result.city());
            assertNotNull(result.zip());
            assertNotNull(result.phone());
            assertNotNull(result.email());
        }

        @Test
        void GIVEN_person_with_same_address_THEN_return_correct_person() {
            // Given - Multiple people live at "1509 Culver St"
            String name = "Tenley Boyd";

            // When
            Person result = personRepository.readPerson(name);

            // Then
            assertNotNull(result);
            assertEquals("Tenley", result.firstName());
            assertEquals("Boyd", result.lastName());
            assertEquals("1509 Culver St", result.address());
            assertEquals("841-874-6512", result.phone());
            assertEquals("tenz@email.com", result.email());
        }

        @Test
        void GIVEN_different_person_at_same_address_THEN_return_correct_person() {
            // Given - Another person at "1509 Culver St"
            String name = "Roger Boyd";

            // When
            Person result = personRepository.readPerson(name);

            // Then
            assertNotNull(result);
            assertEquals("Roger", result.firstName());
            assertEquals("Boyd", result.lastName());
            assertEquals("1509 Culver St", result.address());
            assertEquals("841-874-6512", result.phone());
            assertEquals("jaboyd@email.com", result.email());
        }
    }
}
