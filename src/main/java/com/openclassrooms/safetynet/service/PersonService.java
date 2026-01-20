package com.openclassrooms.safetynet.service;

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
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'findAllEmails'");
  }
}
