package com.openclassrooms.safetynet.service;

import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.openclassrooms.safetynet.domain.ChildPersonDTO;
import com.openclassrooms.safetynet.domain.CoveredPersonDTO;
import com.openclassrooms.safetynet.domain.FirePersonDTO;
import com.openclassrooms.safetynet.domain.FireResponseDTO;
import com.openclassrooms.safetynet.domain.FireStation;
import com.openclassrooms.safetynet.domain.FireStationPersonDTO;
import com.openclassrooms.safetynet.domain.InfoPersonDTO;
import com.openclassrooms.safetynet.domain.MedicalRecord;
import com.openclassrooms.safetynet.domain.Person;
import com.openclassrooms.safetynet.domain.FloodPersonDTO;
import com.openclassrooms.safetynet.domain.FloodResponseDTO;
import com.openclassrooms.safetynet.repository.SafetyNetRepository;

@Service
public class SafetyNetService {
  SafetyNetRepository safetyNetRepository;

  public SafetyNetService(SafetyNetRepository safetyNetRepository) {
    this.safetyNetRepository = safetyNetRepository;
  }

  public int getAge(String firstName, String lastName) {
    // Get that person's medical records to find age
    MedicalRecord medicalRecord = safetyNetRepository.findMedicalRecordsByFirstAndLastName(firstName, lastName);

    // Calculate age and increment respective counter
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM-dd-yyyy");
    LocalDate birthDate = LocalDate.parse(medicalRecord.birthdate().replace('/', '-'), formatter);
    LocalDate currentDate = LocalDate.now();
    int age = Period.between(birthDate, currentDate).getYears();
    return age;
  }
  
  public FireStationPersonDTO getAllPersonsByFireStationNumber(String stationNumber) {
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

          int age = getAge(person.firstName(), person.lastName());

          if (age >= 18) {
            adultCount++;
          } else {
            childCount++;
          }
        }
      }
    }

    FireStationPersonDTO fireStationResponseDTO = new FireStationPersonDTO(coveredPersons, adultCount, childCount);
    return fireStationResponseDTO;
  }

  public Set<String> getPhoneNumbersByFireStation(String stationNumber) {
    FireStationPersonDTO fireStationResponseDTO = getAllPersonsByFireStationNumber(stationNumber);
    Set<String> phoneNumbers = new HashSet<>();
    for (CoveredPersonDTO person : fireStationResponseDTO.coveredPersons()) {
      phoneNumbers.add(person.phoneNumber());
    }

    return phoneNumbers;
  }

  public List<InfoPersonDTO> findPersonsByLastName(String lastName) {
    List<Person> persons = safetyNetRepository.findPersonsByLastName(lastName);
    if (persons.isEmpty()) {
      return List.of();
    }
    List<InfoPersonDTO> infoPersons = new ArrayList<>();
    InfoPersonDTO currentInfoPerson;

    for (Person person : persons) {
      MedicalRecord records = safetyNetRepository.findMedicalRecordsByFirstAndLastName(person.firstName(), person.lastName());
      if (records == null) {
        currentInfoPerson = new InfoPersonDTO(person.firstName(), person.lastName(), person.email(), List.of(), List.of());
      } else {
        currentInfoPerson = new InfoPersonDTO(person.firstName(), person.lastName(), person.email(), records.medications(), records.allergies());
      }
      infoPersons.add(currentInfoPerson);
    }
    return infoPersons;
  }

  public FireResponseDTO getPersonsAndFireStationByAddress(String address) {
    List<Person> persons = safetyNetRepository.findAllPersons();
    FireStation fireStation = safetyNetRepository.findFireStationByAddress(address);
    if (fireStation == null) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No fire station found at address '" + address + "'");
    }

    List<FirePersonDTO> firePersons = new ArrayList<>();
    
    for (Person person : persons) {
      if (person.address().equals(address)) {
        // Get that person's medical records to find age
        MedicalRecord medicalRecord = safetyNetRepository.findMedicalRecordsByFirstAndLastName(person.firstName(), person.lastName());

        // Create person DTO for /fire endpoint
        FirePersonDTO currentFirePerson = new FirePersonDTO(person.firstName(), person.lastName(), person.phone(), medicalRecord.medications(), medicalRecord.allergies());
        firePersons.add(currentFirePerson);
      }
    }
    FireResponseDTO fireResponseDTO = new FireResponseDTO(fireStation.station(), firePersons);

    return fireResponseDTO;
  }

  public List<String> getAllEmailsByCity(String city) {
    List<String> personsEmails = new ArrayList<>();

    List<Person> persons = safetyNetRepository.findPersonsByCity(city);
    for (Person person : persons) {
      personsEmails.add(person.email());
    }
    return personsEmails;
  }

  public List<FloodResponseDTO> getPersonsByStationNumbers(List<String> stationNumbers) {
    List<FloodResponseDTO> floodResponseDTO = new ArrayList<>();
    
    for (String stationNumber : stationNumbers) {
      Map<String, List<FloodPersonDTO>> floodResponse = new HashMap<>();
      // For each station number sent in the request, get the full station details
      List<FireStation> fireStations = safetyNetRepository.findFireStationsByStationNumber(stationNumber);

      for (FireStation fireStation : fireStations) {
        // For each set of station details, get all the people who are covered by that station  
        List<FloodPersonDTO> floodPersons = new ArrayList<>();
        List<Person> persons = safetyNetRepository.findPersonsByAddress(fireStation.address());

        for (Person person : persons) {
          // For each person covered by that station, get their age, medical records, assign their info to the floodPersonDTO, and add them to the list of floodPersons
          int age = getAge(person.firstName(), person.lastName());
          MedicalRecord medicalRecord = safetyNetRepository.findMedicalRecordsByFirstAndLastName(person.firstName(), person.lastName());
          FloodPersonDTO floodPersonDTO = new FloodPersonDTO(person.firstName(), person.lastName(), person.phone(), age, medicalRecord.medications(), medicalRecord.allergies());
          floodPersons.add(floodPersonDTO);
        }

        // Map each list of flood persons to their address
        floodResponse.put(fireStation.address(), floodPersons);
      }
      floodResponseDTO.add(new FloodResponseDTO(stationNumber, floodResponse));
    }
    return floodResponseDTO;
  }

  public List<ChildPersonDTO> getChildrenAndFamiliesByAddress(String address) {
    List<ChildPersonDTO> childDTOList = new ArrayList<>();
    List<Person> personsAtHousehold = safetyNetRepository.findPersonsByAddress(address);
    
    for (Person person : personsAtHousehold) {
      int age = getAge(person.firstName(), person.lastName());
      if (age < 18) {
        List<String> familyMembers = personsAtHousehold.stream()
          .filter(p -> !(p.firstName().equals(person.firstName()) && p.lastName().equals(person.lastName())))
          .map(p -> p.firstName() + " " + p.lastName())
          .collect(Collectors.toList());
        ChildPersonDTO childPersonDTO = new ChildPersonDTO(person.firstName(), person.lastName(), age, familyMembers);
        childDTOList.add(childPersonDTO);
      }
    }

    return childDTOList;
  }

}
