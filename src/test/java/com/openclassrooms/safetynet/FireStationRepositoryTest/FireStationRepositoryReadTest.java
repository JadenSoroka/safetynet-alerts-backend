package com.openclassrooms.safetynet.FireStationRepositoryTest;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.openclassrooms.safetynet.domain.FireStation;
import com.openclassrooms.safetynet.repository.FireStationRepository;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@DisplayName("Fire Station Repository Read Tests")
public class FireStationRepositoryReadTest {

    @Autowired
    private FireStationRepository fireStationRepository;

    @Nested
    @DisplayName("readFireStation")
    class ReadFireStation {
        @Test
        void GIVEN_valid_address_THEN_return_fire_station() {
            // Given
            String address = "1509 Culver St";

            // When
            FireStation result = fireStationRepository.readFireStation(address);

            // Then
            assertNotNull(result);
            assertEquals("1509 Culver St", result.address());
            assertEquals("3", result.station());
        }

        @Test
        void GIVEN_invalid_address_THEN_return_null() {
            // Given
            String address = "999 Nonexistent St";

            // When
            FireStation result = fireStationRepository.readFireStation(address);

            // Then
            assertNull(result);
        }

        @Test
        void GIVEN_address_with_different_case_THEN_return_fire_station() {
            // Given
            String address = "1509 CULVER ST"; // Uppercase

            // When
            FireStation result = fireStationRepository.readFireStation(address);

            // Then
            assertNotNull(result);
            assertEquals("1509 Culver St", result.address());
            assertEquals("3", result.station());
        }

        @Test
        void GIVEN_another_valid_address_THEN_return_correct_fire_station() {
            // Given
            String address = "29 15th St";

            // When
            FireStation result = fireStationRepository.readFireStation(address);

            // Then
            assertNotNull(result);
            assertEquals("29 15th St", result.address());
            assertEquals("2", result.station());
        }

        @Test
        void GIVEN_address_with_mixed_case_THEN_return_fire_station() {
            // Given
            String address = "29 15TH st"; // Mixed case

            // When
            FireStation result = fireStationRepository.readFireStation(address);

            // Then
            assertNotNull(result);
            assertEquals("29 15th St", result.address());
            assertEquals("2", result.station());
        }

        @Test
        void GIVEN_empty_address_THEN_return_null() {
            // Given
            String address = "";

            // When
            FireStation result = fireStationRepository.readFireStation(address);

            // Then
            assertNull(result);
        }
    }
}
