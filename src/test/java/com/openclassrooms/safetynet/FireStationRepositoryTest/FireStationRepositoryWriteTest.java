package com.openclassrooms.safetynet.FireStationRepositoryTest;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.openclassrooms.safetynet.domain.FireStation;
import com.openclassrooms.safetynet.repository.FireStationRepository;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@DisplayName("Fire Station Repository Write Tests")
public class FireStationRepositoryWriteTest {

    @Autowired
    private FireStationRepository fireStationRepository;

    @AfterEach
    void cleanup() {
        // Clean up any test data that may have been created
        fireStationRepository.deleteFireStation("123 Test St");
        fireStationRepository.deleteFireStation("456 New Test Ave");
    }

    @Nested
    @DisplayName("createFireStation")
    class CreateFireStation {
        @Test
        void GIVEN_new_fire_station_THEN_create_and_return_fire_station() {
            // Given
            FireStation newFireStation = new FireStation("123 Test St", "5");

            // When
            FireStation result = fireStationRepository.createFireStation(newFireStation);

            // Then
            assertNotNull(result);
            assertEquals("123 Test St", result.address());
            assertEquals("5", result.station());

            // Verify it was saved
            FireStation saved = fireStationRepository.readFireStation("123 Test St");
            assertNotNull(saved);
            assertEquals("123 Test St", saved.address());
            assertEquals("5", saved.station());
        }
    }

    @Nested
    @DisplayName("updateFireStation")
    class UpdateFireStation {
        @Test
        void GIVEN_existing_address_THEN_update_and_return_true() {
            // Given - First create a fire station
            FireStation originalFireStation = new FireStation("123 Test St", "5");
            fireStationRepository.createFireStation(originalFireStation);

            FireStation updatedFireStation = new FireStation("123 Test St", "6");

            // When
            boolean result = fireStationRepository.updateFireStation("123 Test St", updatedFireStation);

            // Then
            assertTrue(result);

            // Verify the update
            FireStation saved = fireStationRepository.readFireStation("123 Test St");
            assertNotNull(saved);
            assertEquals("123 Test St", saved.address());
            assertEquals("6", saved.station());
        }

        @Test
        void GIVEN_non_existent_address_THEN_return_false() {
            // Given
            FireStation updatedFireStation = new FireStation("999 Nonexistent St", "5");

            // When
            boolean result = fireStationRepository.updateFireStation("999 Nonexistent St", updatedFireStation);

            // Then
            assertFalse(result);
        }

        @Test
        void GIVEN_existing_address_with_different_case_THEN_update_and_return_true() {
            // Given - First create a fire station
            FireStation originalFireStation = new FireStation("456 New Test Ave", "7");
            fireStationRepository.createFireStation(originalFireStation);

            FireStation updatedFireStation = new FireStation("456 New Test Ave", "8");

            // When - Update using different case
            boolean result = fireStationRepository.updateFireStation("456 new test ave", updatedFireStation);

            // Then
            assertTrue(result);

            // Verify the update
            FireStation saved = fireStationRepository.readFireStation("456 New Test Ave");
            assertNotNull(saved);
            assertEquals("456 New Test Ave", saved.address());
            assertEquals("8", saved.station());
        }

        @Test
        void GIVEN_valid_address_from_data_json_THEN_update_and_return_true() {
            // Given - Use an existing address from data.json
            String existingAddress = "29 15th St";
            FireStation originalStation = fireStationRepository.readFireStation(existingAddress);
            assertNotNull(originalStation);
            String originalStationNumber = originalStation.station();

            FireStation updatedFireStation = new FireStation("29 15th St", "9");

            // When
            boolean result = fireStationRepository.updateFireStation(existingAddress, updatedFireStation);

            // Then
            assertTrue(result);

            // Verify the update
            FireStation saved = fireStationRepository.readFireStation(existingAddress);
            assertNotNull(saved);
            assertEquals("29 15th St", saved.address());
            assertEquals("9", saved.station());

            // Restore original data
            FireStation restoreStation = new FireStation("29 15th St", originalStationNumber);
            fireStationRepository.updateFireStation(existingAddress, restoreStation);
        }
    }

    @Nested
    @DisplayName("deleteFireStation")
    class DeleteFireStation {
        @Test
        void GIVEN_existing_address_THEN_delete_and_return_true() {
            // Given - First create a fire station
            FireStation fireStation = new FireStation("123 Test St", "5");
            fireStationRepository.createFireStation(fireStation);

            // When
            boolean result = fireStationRepository.deleteFireStation("123 Test St");

            // Then
            assertTrue(result);

            // Verify deletion
            FireStation deleted = fireStationRepository.readFireStation("123 Test St");
            assertNull(deleted);
        }

        @Test
        void GIVEN_non_existent_address_THEN_return_false() {
            // Given
            String address = "999 Nonexistent St";

            // When
            boolean result = fireStationRepository.deleteFireStation(address);

            // Then
            assertFalse(result);
        }

        @Test
        void GIVEN_existing_address_with_different_case_THEN_delete_and_return_true() {
            // Given - First create a fire station
            FireStation fireStation = new FireStation("456 New Test Ave", "7");
            fireStationRepository.createFireStation(fireStation);

            // When - Delete using different case
            boolean result = fireStationRepository.deleteFireStation("456 new test ave");

            // Then
            assertTrue(result);

            // Verify deletion
            FireStation deleted = fireStationRepository.readFireStation("456 New Test Ave");
            assertNull(deleted);
        }

        @Test
        void GIVEN_valid_address_from_data_json_THEN_delete_and_return_true() {
            // Given - Use an existing address from data.json
            String existingAddress = "951 LoneTree Rd";
            FireStation originalStation = fireStationRepository.readFireStation(existingAddress);
            assertNotNull(originalStation);

            // When
            boolean result = fireStationRepository.deleteFireStation(existingAddress);

            // Then
            assertTrue(result);

            // Verify deletion
            FireStation deleted = fireStationRepository.readFireStation(existingAddress);
            assertNull(deleted);

            // Restore original data
            fireStationRepository.createFireStation(originalStation);
        }

        @Test
        void GIVEN_empty_address_THEN_return_false() {
            // Given
            String address = "";

            // When
            boolean result = fireStationRepository.deleteFireStation(address);

            // Then
            assertFalse(result);
        }
    }
}
