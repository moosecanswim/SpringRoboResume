package me.moosecanswim.springroboresume.repositories;

import me.moosecanswim.springroboresume.model.Job;
import me.moosecanswim.springroboresume.model.Person;
import org.springframework.data.repository.CrudRepository;

import java.util.Set;

public interface JobRepository extends CrudRepository<Job,Long> {
    Set<Job> findByPerson(Person person);
    Set<Job> findByPersonAndActive(Person person,Boolean in);
    long countByPerson(Person person);
    long countByPersonAndActive(Person person,Boolean in);
}
