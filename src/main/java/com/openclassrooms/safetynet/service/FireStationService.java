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

    public FireStation readFireStation(String addressToMatch) {
        return fireStationRepository.readFireStation(addressToMatch);
    }

    public FireStation createFireStation(FireStation newFireStationRequest) {
        return fireStationRepository.createFireStation(newFireStationRequest);
    }

    public boolean updateFireStation(String addressToMatch, FireStation updatedFireStation) {
        return fireStationRepository.updateFireStation(addressToMatch, updatedFireStation);
    }

    public boolean deleteFireStation(String addressToMatch) {
        return fireStationRepository.deleteFireStation(addressToMatch);
    }

    

}
