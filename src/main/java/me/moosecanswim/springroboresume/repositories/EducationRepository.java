package me.moosecanswim.springroboresume.repositories;

import me.moosecanswim.springroboresume.model.Education;
import me.moosecanswim.springroboresume.model.Person;
import org.springframework.data.repository.CrudRepository;

import java.util.Set;

public interface EducationRepository extends CrudRepository<Education,Long> {

    Set<Education> findByPerson(Person person);
    Set<Education> findByPersonAndActive(Person person,Boolean in);
    long countByPerson(Person person);
    long countByPersonAndActive(Person person,Boolean in);
}
