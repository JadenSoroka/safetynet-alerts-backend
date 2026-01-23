package com.openclassrooms.safetynet.service;

import java.sql.Date;
import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Service;

import com.openclassrooms.safetynet.SafetyNetApplication;
import com.openclassrooms.safetynet.domain.CoveredPersonDTO;
import com.openclassrooms.safetynet.domain.FireStation;
import com.openclassrooms.safetynet.domain.FireStationResponseDTO;
import com.openclassrooms.safetynet.domain.InfoPersonDTO;
import com.openclassrooms.safetynet.domain.MedicalRecord;
import com.openclassrooms.safetynet.domain.Person;
import com.openclassrooms.safetynet.repository.SafetyNetRepository;

@Service
public class SafetyNetService {
  SafetyNetRepository safetyNetRepository;

  public SafetyNetService(SafetyNetRepository safetyNetRepository) {
    this.safetyNetRepository = safetyNetRepository;
  }
  
  public FireStationResponseDTO getAllPersonsByFireStationNumber(String stationNumber) {
    List<FireStation> stations = safetyNetRepository.findFireStationsByStationNumber(stationNumber);
    List<Person> persons = safetyNetRepository.findAllPersons();
    List<CoveredPersonDTO> coveredPersons = new ArrayList<>();
    int adultCount = 0;
    int childCount = 0;
    
    for (FireStation fireStation : stations) {
      for (Person person : persons) {
        if (fireStation.address().equals(person.address())) {
          // Add person to list of people covered by that fire station
          coveredPersons.add(new CoveredPersonDTO(
            person.firstName(),
            person.lastName(),
            person.address(),
            person.phone()
          ));

          // Get that person's medical records to find age
          MedicalRecord medicalRecord = safetyNetRepository.findMedicalRecordsByFirstAndLastName(person.firstName(), person.lastName());

          // Calculate age and increment respective counter
          DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM-dd-yyyy");
          LocalDate birthDate = LocalDate.parse(medicalRecord.birthdate().replace('/', '-'), formatter);
          LocalDate currentDate = LocalDate.now();
          int age = Period.between(birthDate, currentDate).getYears();
          if (age >= 18) {
            adultCount++;
          } else {
            childCount++;
          }
        }
      }
    }

    FireStationResponseDTO fireStationResponseDTO = new FireStationResponseDTO(coveredPersons, adultCount, childCount);
    return fireStationResponseDTO;
  }

  public Set<String> findPhoneNumbersByFireStation(String stationNumber) {
    FireStationResponseDTO fireStationResponseDTO = getAllPersonsByFireStationNumber(stationNumber);
    Set<String> phoneNumbers = new HashSet<>();
    for (CoveredPersonDTO person : fireStationResponseDTO.coveredPersons()) {
      phoneNumbers.add(person.phoneNumber());
    }

    return phoneNumbers;
  }

  public List<InfoPersonDTO> findPersonsByLastName(String lastName) {
    List<Person> persons = safetyNetRepository.findPersonsByLastName(lastName);
    List<InfoPersonDTO> infoPersons = new ArrayList<>();

    for (Person person : persons) {
      MedicalRecord records = safetyNetRepository.findMedicalRecordsByFirstAndLastName(person.firstName(), person.lastName());
      InfoPersonDTO currentInfoPerson = new InfoPersonDTO(person.firstName(), person.lastName(), person.email(), records.medications(), records.allergies());
      infoPersons.add(currentInfoPerson);
    }
    return infoPersons;
  }

  public List<String> getAllEmailsByCity(String city) {
    List<String> personsEmails = new ArrayList<>();

    List<Person> persons = safetyNetRepository.findPersonsByCity(city);
    for (Person person : persons) {
      personsEmails.add(person.email());
    }
    return personsEmails;
  }

}
