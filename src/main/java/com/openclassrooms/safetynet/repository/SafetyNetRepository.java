package com.openclassrooms.safetynet.repository;

import java.util.List;

import com.openclassrooms.safetynet.domain.FireStation;
import com.openclassrooms.safetynet.domain.MedicalRecord;
import com.openclassrooms.safetynet.domain.Person;

public interface SafetyNetRepository {
  List<Person> findAllPersons();
  List<Person> findPersonsByLastName(String lastName);
  List<Person> findPersonsByCity(String city);
  List<Person> findPersonsByAddress(String address);
  List<FireStation> findFireStationsByStationNumber(String address);
  MedicalRecord findMedicalRecordsByFirstAndLastName(String firstName, String lastName);
}
