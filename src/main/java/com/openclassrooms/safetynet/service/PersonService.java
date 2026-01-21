package com.openclassrooms.safetynet.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.openclassrooms.safetynet.domain.Person;
import com.openclassrooms.safetynet.repository.PersonRepository;

@Service
public class PersonService {
  PersonRepository personRepository;

  public PersonService(PersonRepository personRepository) {
    this.personRepository = personRepository;
  }

  public List<Person> findPersonByLastName(String lastName) {
    return personRepository.findByLastName(lastName);
  }

  public List<String> findAllEmails(String city) {
    List<String> personsEmails = new ArrayList<>();

    List<Person> persons = personRepository.findByCity(city);
    for (Person person : persons) {
      personsEmails.add(person.getEmail());
    }
    return personsEmails;
  }
}
