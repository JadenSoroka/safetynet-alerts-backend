package com.openclassrooms.safetynet.MedicalRecordRepositoryTest;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.openclassrooms.safetynet.domain.MedicalRecord;
import com.openclassrooms.safetynet.repository.MedicalRecordRepository;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@DisplayName("Medical Record Repository Read Tests")
public class MedicalRecordRepositoryReadTest {

    @Autowired
    private MedicalRecordRepository medicalRecordRepository;

    @Nested
    @DisplayName("readPerson")
    class ReadPerson {
        @Test
        void GIVEN_valid_first_and_last_name_THEN_return_medical_record() {
            // Given
            String name = "John Boyd";

            // When
            MedicalRecord result = medicalRecordRepository.readPerson(name);

            // Then
            assertNotNull(result);
            assertEquals("John", result.firstName());
            assertEquals("Boyd", result.lastName());
            assertEquals("03/06/1984", result.birthdate());
            assertNotNull(result.medications());
            assertNotNull(result.allergies());
        }

        @Test
        void GIVEN_invalid_name_THEN_return_null() {
            // Given
            String name = "Nonexistent Person";

            // When
            MedicalRecord result = medicalRecordRepository.readPerson(name);

            // Then
            assertNull(result);
        }

        @Test
        void GIVEN_name_with_different_case_THEN_return_medical_record() {
            // Given
            String name = "JOHN BOYD"; // Uppercase

            // When
            MedicalRecord result = medicalRecordRepository.readPerson(name);

            // Then
            assertNotNull(result);
            assertEquals("John", result.firstName());
            assertEquals("Boyd", result.lastName());
            assertEquals("03/06/1984", result.birthdate());
        }

        @Test
        void GIVEN_another_valid_name_THEN_return_correct_medical_record() {
            // Given
            String name = "Jacob Boyd";

            // When
            MedicalRecord result = medicalRecordRepository.readPerson(name);

            // Then
            assertNotNull(result);
            assertEquals("Jacob", result.firstName());
            assertEquals("Boyd", result.lastName());
            assertEquals("03/06/1989", result.birthdate());
        }

        @Test
        void GIVEN_name_with_mixed_case_THEN_return_medical_record() {
            // Given
            String name = "jAcOb BoYd"; // Mixed case

            // When
            MedicalRecord result = medicalRecordRepository.readPerson(name);

            // Then
            assertNotNull(result);
            assertEquals("Jacob", result.firstName());
            assertEquals("Boyd", result.lastName());
            assertEquals("03/06/1989", result.birthdate());
        }

        @Test
        void GIVEN_empty_name_THEN_return_null() {
            // Given
            String name = "";

            // When
            MedicalRecord result = medicalRecordRepository.readPerson(name);

            // Then
            assertNull(result);
        }

        @Test
        void GIVEN_valid_name_THEN_return_medical_record_with_medications() {
            // Given
            String name = "Sophia Zemicks";

            // When
            MedicalRecord result = medicalRecordRepository.readPerson(name);

            // Then
            assertNotNull(result);
            assertEquals("Sophia", result.firstName());
            assertEquals("Zemicks", result.lastName());
            assertFalse(result.medications().isEmpty());
            assertTrue(result.medications().size() > 0);
        }

        @Test
        void GIVEN_valid_name_THEN_return_medical_record_with_allergies() {
            // Given
            String name = "Tenley Boyd";

            // When
            MedicalRecord result = medicalRecordRepository.readPerson(name);

            // Then
            assertNotNull(result);
            assertEquals("Tenley", result.firstName());
            assertEquals("Boyd", result.lastName());
            assertFalse(result.allergies().isEmpty());
            assertTrue(result.allergies().contains("peanut"));
        }

        @Test
        void GIVEN_valid_name_THEN_return_medical_record_with_empty_lists() {
            // Given
            String name = "Roger Boyd";

            // When
            MedicalRecord result = medicalRecordRepository.readPerson(name);

            // Then
            assertNotNull(result);
            assertEquals("Roger", result.firstName());
            assertEquals("Boyd", result.lastName());
            assertTrue(result.medications().isEmpty());
            assertTrue(result.allergies().isEmpty());
        }
    }
}
