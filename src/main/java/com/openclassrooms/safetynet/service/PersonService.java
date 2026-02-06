package com.openclassrooms.safetynet.service;

import org.springframework.stereotype.Service;

import com.openclassrooms.safetynet.domain.Person;
import com.openclassrooms.safetynet.repository.PersonRepository;

@Service
public class PersonService {
  PersonRepository personRepository;

  public PersonService(PersonRepository personRepository) {
    this.personRepository = personRepository;
  }

  public Person readPerson(String firstLastNameToMatch) {
    return personRepository.readPerson(firstLastNameToMatch);
  }

  public Person createPerson(Person person) throws Exception {
    return personRepository.createPerson(person);
  }

  public boolean updatePerson(String firstLastNameToMatch, Person personUpdates) throws Exception {
    return personRepository.updatePerson(firstLastNameToMatch, personUpdates);
  }

  public boolean removePerson(String firstLastNameToMatch) throws Exception {
    return personRepository.deletePerson(firstLastNameToMatch);
  }
}
