package com.openclassrooms.safetynet.PersonRepositoryTest;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.openclassrooms.safetynet.domain.Person;
import com.openclassrooms.safetynet.repository.PersonRepository;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;

@SpringBootTest
@DisplayName("Person Repository Write Tests")
public class PersonRepositoryWriteTest {

    @Autowired
    private PersonRepository personRepository;

    @AfterEach
    void cleanup() throws IOException {
        // Clean up any test data that may have been created
        personRepository.deletePerson("Test Person");
        personRepository.deletePerson("Another Test");
    }

    @Nested
    @DisplayName("createPerson")
    class CreatePerson {
        @Test
        void GIVEN_new_person_THEN_create_and_return_person() throws IOException {
            // Given
            Person newPerson = new Person(
                "Test",
                "Person",
                "123 Test St",
                "TestCity",
                "12345",
                "123-456-7890",
                "test@email.com"
            );

            // When
            Person result = personRepository.createPerson(newPerson);

            // Then
            assertNotNull(result);
            assertEquals("Test", result.firstName());
            assertEquals("Person", result.lastName());
            assertEquals("123 Test St", result.address());
            assertEquals("TestCity", result.city());
            assertEquals("12345", result.zip());
            assertEquals("123-456-7890", result.phone());
            assertEquals("test@email.com", result.email());

            // Verify it was saved
            Person saved = personRepository.readPerson("Test Person");
            assertNotNull(saved);
            assertEquals("Test", saved.firstName());
            assertEquals("Person", saved.lastName());
        }

        @Test
        void GIVEN_person_with_minimal_info_THEN_create_successfully() throws IOException {
            // Given
            Person newPerson = new Person(
                "Test",
                "Person",
                "456 Test Ave",
                "City",
                "00000",
                "000-000-0000",
                "test2@email.com"
            );

            // When
            Person result = personRepository.createPerson(newPerson);

            // Then
            assertNotNull(result);
            assertEquals("Test", result.firstName());
            assertEquals("Person", result.lastName());
            assertEquals("456 Test Ave", result.address());
        }
    }

    @Nested
    @DisplayName("updatePerson")
    class UpdatePerson {
        @Test
        void GIVEN_existing_person_THEN_update_and_return_true() throws IOException {
            // Given - First create a person
            Person originalPerson = new Person(
                "Test",
                "Person",
                "123 Test St",
                "TestCity",
                "12345",
                "123-456-7890",
                "test@email.com"
            );
            personRepository.createPerson(originalPerson);

            Person updatedPerson = new Person(
                "Test",
                "Person",
                "456 Updated St",
                "UpdatedCity",
                "54321",
                "098-765-4321",
                "updated@email.com"
            );

            // When
            boolean result = personRepository.updatePerson("Test Person", updatedPerson);

            // Then
            assertTrue(result);

            // Verify the update
            Person saved = personRepository.readPerson("Test Person");
            assertNotNull(saved);
            assertEquals("456 Updated St", saved.address());
            assertEquals("UpdatedCity", saved.city());
            assertEquals("54321", saved.zip());
            assertEquals("098-765-4321", saved.phone());
            assertEquals("updated@email.com", saved.email());
        }

        @Test
        void GIVEN_non_existent_person_THEN_return_false() throws IOException {
            // Given
            Person updatedPerson = new Person(
                "Nonexistent",
                "Person",
                "123 Test St",
                "TestCity",
                "12345",
                "123-456-7890",
                "test@email.com"
            );

            // When
            boolean result = personRepository.updatePerson("Nonexistent Person", updatedPerson);

            // Then
            assertFalse(result);
        }

        @Test
        void GIVEN_existing_person_with_different_case_THEN_update_and_return_true() throws IOException {
            // Given - First create a person
            Person originalPerson = new Person(
                "Another",
                "Test",
                "123 Test St",
                "TestCity",
                "12345",
                "123-456-7890",
                "test@email.com"
            );
            personRepository.createPerson(originalPerson);

            Person updatedPerson = new Person(
                "Another",
                "Test",
                "789 New St",
                "NewCity",
                "99999",
                "999-999-9999",
                "new@email.com"
            );

            // When - Update using different case
            boolean result = personRepository.updatePerson("another test", updatedPerson);

            // Then
            assertTrue(result);

            // Verify the update
            Person saved = personRepository.readPerson("Another Test");
            assertNotNull(saved);
            assertEquals("789 New St", saved.address());
            assertEquals("NewCity", saved.city());
        }

        @Test
        void GIVEN_valid_person_from_data_json_THEN_update_and_return_true() throws IOException {
            // Given - Use an existing person from data.json
            String existingPerson = "Eric Cadigan";
            Person originalPerson = personRepository.readPerson(existingPerson);
            assertNotNull(originalPerson);

            Person updatedPerson = new Person(
                "Eric",
                "Cadigan",
                "999 Updated St",
                "UpdatedCity",
                "99999",
                "999-999-9999",
                "newemail@test.com"
            );

            // When
            boolean result = personRepository.updatePerson(existingPerson, updatedPerson);

            // Then
            assertTrue(result);

            // Verify the update
            Person saved = personRepository.readPerson(existingPerson);
            assertNotNull(saved);
            assertEquals("999 Updated St", saved.address());

            // Restore original data
            personRepository.updatePerson(existingPerson, originalPerson);
        }
    }

    @Nested
    @DisplayName("deletePerson")
    class DeletePerson {
        @Test
        void GIVEN_existing_person_THEN_delete_and_return_true() throws IOException {
            // Given - First create a person
            Person person = new Person(
                "Test",
                "Person",
                "123 Test St",
                "TestCity",
                "12345",
                "123-456-7890",
                "test@email.com"
            );
            personRepository.createPerson(person);

            // When
            boolean result = personRepository.deletePerson("Test Person");

            // Then
            assertTrue(result);

            // Verify deletion
            Person deleted = personRepository.readPerson("Test Person");
            assertNull(deleted);
        }

        @Test
        void GIVEN_non_existent_person_THEN_return_false() throws IOException {
            // Given
            String name = "Nonexistent Person";

            // When
            boolean result = personRepository.deletePerson(name);

            // Then
            assertFalse(result);
        }

        @Test
        void GIVEN_existing_person_with_different_case_THEN_delete_and_return_true() throws IOException {
            // Given - First create a person
            Person person = new Person(
                "Another",
                "Test",
                "123 Test St",
                "TestCity",
                "12345",
                "123-456-7890",
                "test@email.com"
            );
            personRepository.createPerson(person);

            // When - Delete using different case
            boolean result = personRepository.deletePerson("ANOTHER TEST");

            // Then
            assertTrue(result);

            // Verify deletion
            Person deleted = personRepository.readPerson("Another Test");
            assertNull(deleted);
        }

        @Test
        void GIVEN_valid_person_from_data_json_THEN_delete_and_return_true() throws IOException {
            // Given - Use an existing person from data.json
            String existingPerson = "Clive Ferguson";
            Person originalPerson = personRepository.readPerson(existingPerson);
            assertNotNull(originalPerson);

            // When
            boolean result = personRepository.deletePerson(existingPerson);

            // Then
            assertTrue(result);

            // Verify deletion
            Person deleted = personRepository.readPerson(existingPerson);
            assertNull(deleted);

            // Restore original data
            personRepository.createPerson(originalPerson);
        }

        @Test
        void GIVEN_empty_name_THEN_return_false() throws IOException {
            // Given
            String name = "";

            // When
            boolean result = personRepository.deletePerson(name);

            // Then
            assertFalse(result);
        }
    }
}
