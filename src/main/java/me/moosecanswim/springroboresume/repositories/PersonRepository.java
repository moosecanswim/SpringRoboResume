package me.moosecanswim.springroboresume.repositories;

import me.moosecanswim.springroboresume.model.Person;
import org.springframework.data.repository.CrudRepository;

public interface PersonRepository extends CrudRepository<Person,Long> {
}
