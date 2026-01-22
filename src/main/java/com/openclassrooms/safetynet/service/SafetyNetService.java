package com.openclassrooms.safetynet.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.openclassrooms.safetynet.domain.CoveredPerson;
import com.openclassrooms.safetynet.domain.FireStation;
import com.openclassrooms.safetynet.domain.Person;
import com.openclassrooms.safetynet.repository.SafetyNetRepository;

@Service
public class SafetyNetService {
  SafetyNetRepository safetyNetRepository;

  public SafetyNetService(SafetyNetRepository safetyNetRepository) {
    this.safetyNetRepository = safetyNetRepository;
  }

  public List<Person> findPersonsByLastName(String lastName) {
    return safetyNetRepository.findPersonsByLastName(lastName);
  }

  public List<String> getAllEmailsByCity(String city) {
    List<String> personsEmails = new ArrayList<>();

    List<Person> persons = safetyNetRepository.findPersonsByCity(city);
    for (Person person : persons) {
      personsEmails.add(person.getEmail());
    }
    return personsEmails;
  }

  public List<CoveredPerson> getAllPersonsByFireStationNumber(String stationNumber) {
    List<FireStation> stations = safetyNetRepository.findFireStationsByStationNumber(stationNumber);
    List<Person> persons = safetyNetRepository.findAllPersons();
    List<CoveredPerson> coveredPersons = new ArrayList<>();

    for (FireStation fireStation : stations) {
      for (Person person : persons) {
        if (fireStation.getAddress().equals(person.getAddress())) {
          coveredPersons.add(new CoveredPerson(
            person.getFirstName(),
            person.getLastName(),
            person.getAddress(),
            person.getPhone()
          ));
        }
      }
    }
    return coveredPersons;
  }
}
