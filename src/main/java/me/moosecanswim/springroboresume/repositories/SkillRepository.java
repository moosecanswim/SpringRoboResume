package me.moosecanswim.springroboresume.repositories;

import me.moosecanswim.springroboresume.model.Person;
import me.moosecanswim.springroboresume.model.PostedJob;
import me.moosecanswim.springroboresume.model.Skill;
import org.springframework.data.repository.CrudRepository;

import java.util.Set;

public interface SkillRepository extends CrudRepository<Skill,Long> {
    Set<Skill> findByPerson(Person person);
    Set<Skill> findByPersonAndActive(Person person,Boolean in);
    long countByPerson(Person person);
    long countByPersonAndActive(Person person,Boolean in);
    Set<Skill> findByPostedJob(PostedJob aPJ);
}
