package me.moosecanswim.springroboresume.repositories;

import me.moosecanswim.springroboresume.model.Job;
import org.springframework.data.repository.CrudRepository;

public interface JobRepository extends CrudRepository<Job,Long> {
}
