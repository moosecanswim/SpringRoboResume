package me.moosecanswim.springroboresume.repositories;

import me.moosecanswim.springroboresume.model.Course;
import me.moosecanswim.springroboresume.model.Person;
import org.springframework.data.repository.CrudRepository;

import java.util.Set;

public interface CourseRepository extends CrudRepository<Course,Long> {
    Set<Course> findByActive(Boolean in);
}
