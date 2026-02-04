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

  public Person findPersonByFirstLastName(String firstLastName) {
    return personRepository.findPersonsByFirstLastName(firstLastName);
  }

  public Person createPerson(Person person) throws Exception {
    return personRepository.savePerson(person);
  }

  public Person updatePersonInfo(Person personUpdates) throws Exception {
    return personRepository.updatePersonInfo(personUpdates);
  }

  public void removePerson(Person personToRemove) throws Exception {
    personRepository.removePerson(personToRemove);
  }
}
