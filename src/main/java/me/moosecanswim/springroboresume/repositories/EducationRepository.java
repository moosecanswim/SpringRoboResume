package me.moosecanswim.springroboresume.repositories;

import me.moosecanswim.springroboresume.model.Education;
import org.springframework.data.repository.CrudRepository;

public interface EducationRepository extends CrudRepository<Education,Long> {
}
