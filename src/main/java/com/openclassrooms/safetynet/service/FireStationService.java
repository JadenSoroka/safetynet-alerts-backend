package com.openclassrooms.safetynet.service;

import org.springframework.stereotype.Service;

import com.openclassrooms.safetynet.domain.FireStation;
import com.openclassrooms.safetynet.repository.FireStationRepository;

@Service
public class FireStationService {
    FireStationRepository fireStationRepository;

    public FireStationService(FireStationRepository fireStationRepository) {
        this.fireStationRepository = fireStationRepository;
    }

    public void createFireStation(FireStation newFireStationRequest) {
        fireStationRepository.createFireStation(newFireStationRequest);
    }

    public boolean updateFireStation(String address, FireStation updatedFireStation) {
        String formattedAddress = address.replaceAll("_", " ").toLowerCase();
        boolean fireStationFound = fireStationRepository.updateFireStation(formattedAddress, updatedFireStation);
        return fireStationFound;
    }

    public boolean deleteFireStation(String address) {
        String formattedAddress = address.replaceAll("_", " ").toLowerCase();
        boolean fireStationFound = fireStationRepository.deleteFireStation(formattedAddress);
        return fireStationFound;
    }

}
