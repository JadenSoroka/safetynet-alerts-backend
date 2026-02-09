package com.openclassrooms.safetynet.MedicalRecordRepositoryTest;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.openclassrooms.safetynet.domain.MedicalRecord;
import com.openclassrooms.safetynet.repository.MedicalRecordRepository;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.List;

@SpringBootTest
@DisplayName("Medical Record Repository Write Tests")
public class MedicalRecordRepositoryWriteTest {

    @Autowired
    private MedicalRecordRepository medicalRecordRepository;

    @AfterEach
    void cleanup() {
        // Clean up any test data that may have been created
        medicalRecordRepository.deleteMedicalRecord("Test Person");
        medicalRecordRepository.deleteMedicalRecord("Another Test");
    }

    @Nested
    @DisplayName("createMedicalRecord")
    class CreateMedicalRecord {
        @Test
        void GIVEN_new_medical_record_THEN_create_and_return_medical_record() {
            // Given
            List<String> medications = Arrays.asList("aspirin:100mg", "ibuprofen:200mg");
            List<String> allergies = Arrays.asList("pollen");
            MedicalRecord newMedicalRecord = new MedicalRecord(
                "Test", 
                "Person", 
                "01/01/2000",
                medications,
                allergies
            );

            // When
            MedicalRecord result = medicalRecordRepository.createMedicalRecord(newMedicalRecord);

            // Then
            assertNotNull(result);
            assertEquals("Test", result.firstName());
            assertEquals("Person", result.lastName());
            assertEquals("01/01/2000", result.birthdate());
            assertEquals(2, result.medications().size());
            assertEquals(1, result.allergies().size());

            // Verify it was saved
            MedicalRecord saved = medicalRecordRepository.readPerson("Test Person");
            assertNotNull(saved);
            assertEquals("Test", saved.firstName());
            assertEquals("Person", saved.lastName());
        }

        @Test
        void GIVEN_medical_record_with_empty_lists_THEN_create_successfully() {
            // Given
            MedicalRecord newMedicalRecord = new MedicalRecord(
                "Test", 
                "Person", 
                "05/15/1990",
                Arrays.asList(),
                Arrays.asList()
            );

            // When
            MedicalRecord result = medicalRecordRepository.createMedicalRecord(newMedicalRecord);

            // Then
            assertNotNull(result);
            assertEquals("Test", result.firstName());
            assertEquals("Person", result.lastName());
            assertTrue(result.medications().isEmpty());
            assertTrue(result.allergies().isEmpty());
        }
    }

    @Nested
    @DisplayName("updateMedicalRecord")
    class UpdateMedicalRecord {
        @Test
        void GIVEN_existing_person_THEN_update_and_return_true() {
            // Given - First create a medical record
            List<String> originalMedications = Arrays.asList("aspirin:100mg");
            List<String> originalAllergies = Arrays.asList("pollen");
            MedicalRecord originalRecord = new MedicalRecord(
                "Test",
                "Person",
                "01/01/2000",
                originalMedications,
                originalAllergies
            );
            medicalRecordRepository.createMedicalRecord(originalRecord);

            List<String> updatedMedications = Arrays.asList("aspirin:200mg", "lisinopril:10mg");
            List<String> updatedAllergies = Arrays.asList("pollen", "cats");
            MedicalRecord updatedRecord = new MedicalRecord(
                "Test",
                "Person",
                "01/01/2000",
                updatedMedications,
                updatedAllergies
            );

            // When
            boolean result = medicalRecordRepository.updateMedicalRecord("Test Person", updatedRecord);

            // Then
            assertTrue(result);

            // Verify the update
            MedicalRecord saved = medicalRecordRepository.readPerson("Test Person");
            assertNotNull(saved);
            assertEquals(2, saved.medications().size());
            assertEquals(2, saved.allergies().size());
            assertTrue(saved.medications().contains("lisinopril:10mg"));
            assertTrue(saved.allergies().contains("cats"));
        }

        @Test
        void GIVEN_non_existent_person_THEN_return_false() {
            // Given
            MedicalRecord updatedRecord = new MedicalRecord(
                "Nonexistent",
                "Person",
                "01/01/2000",
                Arrays.asList(),
                Arrays.asList()
            );

            // When
            boolean result = medicalRecordRepository.updateMedicalRecord("Nonexistent Person", updatedRecord);

            // Then
            assertFalse(result);
        }

        @Test
        void GIVEN_existing_person_with_different_case_THEN_update_and_return_true() {
            // Given - First create a medical record
            MedicalRecord originalRecord = new MedicalRecord(
                "Another",
                "Test",
                "06/15/1995",
                Arrays.asList("med1:100mg"),
                Arrays.asList("allergy1")
            );
            medicalRecordRepository.createMedicalRecord(originalRecord);

            MedicalRecord updatedRecord = new MedicalRecord(
                "Another",
                "Test",
                "06/15/1995",
                Arrays.asList("med2:200mg"),
                Arrays.asList("allergy2")
            );

            // When - Update using different case
            boolean result = medicalRecordRepository.updateMedicalRecord("another test", updatedRecord);

            // Then
            assertTrue(result);

            // Verify the update
            MedicalRecord saved = medicalRecordRepository.readPerson("Another Test");
            assertNotNull(saved);
            assertTrue(saved.medications().contains("med2:200mg"));
            assertTrue(saved.allergies().contains("allergy2"));
        }

        @Test
        void GIVEN_valid_person_from_data_json_THEN_update_and_return_true() {
            // Given - Use an existing person from data.json
            String existingPerson = "Eric Cadigan";
            MedicalRecord originalRecord = medicalRecordRepository.readPerson(existingPerson);
            assertNotNull(originalRecord);

            MedicalRecord updatedRecord = new MedicalRecord(
                "Eric",
                "Cadigan",
                originalRecord.birthdate(),
                Arrays.asList("newmed:500mg"),
                Arrays.asList("newallergy")
            );

            // When
            boolean result = medicalRecordRepository.updateMedicalRecord(existingPerson, updatedRecord);

            // Then
            assertTrue(result);

            // Verify the update
            MedicalRecord saved = medicalRecordRepository.readPerson(existingPerson);
            assertNotNull(saved);
            assertTrue(saved.medications().contains("newmed:500mg"));

            // Restore original data
            medicalRecordRepository.updateMedicalRecord(existingPerson, originalRecord);
        }
    }

    @Nested
    @DisplayName("deleteMedicalRecord")
    class DeleteMedicalRecord {
        @Test
        void GIVEN_existing_person_THEN_delete_and_return_true() {
            // Given - First create a medical record
            MedicalRecord medicalRecord = new MedicalRecord(
                "Test",
                "Person",
                "01/01/2000",
                Arrays.asList(),
                Arrays.asList()
            );
            medicalRecordRepository.createMedicalRecord(medicalRecord);

            // When
            boolean result = medicalRecordRepository.deleteMedicalRecord("Test Person");

            // Then
            assertTrue(result);

            // Verify deletion
            MedicalRecord deleted = medicalRecordRepository.readPerson("Test Person");
            assertNull(deleted);
        }

        @Test
        void GIVEN_non_existent_person_THEN_return_false() {
            // Given
            String name = "Nonexistent Person";

            // When
            boolean result = medicalRecordRepository.deleteMedicalRecord(name);

            // Then
            assertFalse(result);
        }

        @Test
        void GIVEN_existing_person_with_different_case_THEN_delete_and_return_true() {
            // Given - First create a medical record
            MedicalRecord medicalRecord = new MedicalRecord(
                "Another",
                "Test",
                "06/15/1995",
                Arrays.asList(),
                Arrays.asList()
            );
            medicalRecordRepository.createMedicalRecord(medicalRecord);

            // When - Delete using different case
            boolean result = medicalRecordRepository.deleteMedicalRecord("ANOTHER TEST");

            // Then
            assertTrue(result);

            // Verify deletion
            MedicalRecord deleted = medicalRecordRepository.readPerson("Another Test");
            assertNull(deleted);
        }

        @Test
        void GIVEN_valid_person_from_data_json_THEN_delete_and_return_true() {
            // Given - Use an existing person from data.json
            String existingPerson = "Clive Ferguson";
            MedicalRecord originalRecord = medicalRecordRepository.readPerson(existingPerson);
            assertNotNull(originalRecord);

            // When
            boolean result = medicalRecordRepository.deleteMedicalRecord(existingPerson);

            // Then
            assertTrue(result);

            // Verify deletion
            MedicalRecord deleted = medicalRecordRepository.readPerson(existingPerson);
            assertNull(deleted);

            // Restore original data
            medicalRecordRepository.createMedicalRecord(originalRecord);
        }

        @Test
        void GIVEN_empty_name_THEN_return_false() {
            // Given
            String name = "";

            // When
            boolean result = medicalRecordRepository.deleteMedicalRecord(name);

            // Then
            assertFalse(result);
        }
    }
}
