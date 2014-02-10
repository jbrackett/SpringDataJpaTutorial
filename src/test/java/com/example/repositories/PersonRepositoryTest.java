package com.example.repositories;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.List;

import javax.annotation.Resource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.example.AppConfig;
import com.example.domain.Person;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { AppConfig.class })
public class PersonRepositoryTest {

  @Resource
  private PersonRepository personRepository;

  @Test
  public void crud() {
    Person me = new Person("Josh", "Brackett");

    Person saved = personRepository.save(me);
    assertNotNull(saved);
    assertNotNull(saved.getId());

    Long id = saved.getId();
    Person got = personRepository.findOne(id);
    assertNotNull(got);
    assertEquals(saved, got);

    got.setFirstName("Joshua");
    Person updated = personRepository.save(got);
    assertNotNull(updated);
    assertEquals("Joshua", updated.getFirstName());

    personRepository.delete(id);
    Person deleted = personRepository.findOne(id);
    assertNull(deleted);
  }

  @Test
  public void getJohns() {
    List<Person> persons = personRepository.findByFirstName("John");
    assertNotNull(persons);

    assertEquals(2, persons.size());

    for (Person person : persons) {
      assertEquals("John", person.getFirstName());
    }
  }

  @Test
  public void getDoes() {
    List<Person> persons = personRepository.findByLastName("Doe");
    assertNotNull(persons);

    assertEquals(2, persons.size());

    for (Person person : persons) {
      assertEquals("Doe", person.getLastName());
    }
  }

  @Test
  public void getPartialMatch() {
    String partial = "mit";
    List<Person> persons = personRepository.findByLastNameContaining(partial);
    assertNotNull(persons);

    assertEquals(1, persons.size());

    for (Person person : persons) {
      assertTrue(person.getLastName().contains(partial));
    }
  }

}
