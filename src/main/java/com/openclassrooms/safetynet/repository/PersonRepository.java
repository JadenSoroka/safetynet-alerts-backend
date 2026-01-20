package com.openclassrooms.safetynet.repository;

import java.util.List;

import com.openclassrooms.safetynet.domain.Person;

public interface PersonRepository {
  List<Person> findByLastName(String lastName);
}
