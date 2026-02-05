package com.openclassrooms.safetynet.service;

import org.springframework.stereotype.Service;

import com.openclassrooms.safetynet.domain.MedicalRecord;
import com.openclassrooms.safetynet.repository.MedicalRecordRepository;

@Service
public class MedicalRecordService {
    MedicalRecordRepository medicalRecordRepository;

    public MedicalRecordService(MedicalRecordRepository medicalRecordRepository) {
        this.medicalRecordRepository = medicalRecordRepository;
    }

    public MedicalRecord createMedicalRecord(MedicalRecord newMedicalRecordRequest) {
        return medicalRecordRepository.createMedicalRecord(newMedicalRecordRequest);
    }

    public boolean updateMedicalRecord(String firstLastName, MedicalRecord updatedMedicalRecord) {
        String formattedFirstLastName = firstLastName.replace("_", " ").toLowerCase();
        return medicalRecordRepository.updateMedicalRecord(formattedFirstLastName, updatedMedicalRecord);
    }

    public boolean deleteMedicalRecord(String firstLastName) {
        String formattedFirstLastName = firstLastName.replace("_", " ").toLowerCase();
        return medicalRecordRepository.deleteMedicalRecord(formattedFirstLastName);
    }

}
