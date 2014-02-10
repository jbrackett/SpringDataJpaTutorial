package com.example.repositories;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.example.domain.Person;

public interface PersonRepository extends CrudRepository<Person, Long> {

  List<Person> findByFirstName(String firstName);

  List<Person> findByLastName(String lastName);

  List<Person> findByLastNameContaining(String partialLastName);

}
