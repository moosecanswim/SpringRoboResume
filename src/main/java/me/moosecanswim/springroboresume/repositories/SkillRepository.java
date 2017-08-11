package me.moosecanswim.springroboresume.repositories;

import me.moosecanswim.springroboresume.model.Skill;
import org.springframework.data.repository.CrudRepository;

public interface SkillRepository extends CrudRepository<Skill,Long> {
}
