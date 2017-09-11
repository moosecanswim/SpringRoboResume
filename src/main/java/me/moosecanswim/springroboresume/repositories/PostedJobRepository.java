package me.moosecanswim.springroboresume.repositories;

import me.moosecanswim.springroboresume.model.Person;
import me.moosecanswim.springroboresume.model.PostedJob;
import org.springframework.data.repository.CrudRepository;

import java.util.Set;

public interface PostedJobRepository extends CrudRepository<PostedJob,Long>{
    Set<PostedJob> findByRecruiter(Person aPerson);
    Long countByRecruiter(Person aPerson);
}
