package me.moosecanswim.springroboresume.repositories;

import me.moosecanswim.springroboresume.model.PostedJob;
import org.springframework.data.repository.CrudRepository;

public interface PostedJobRepository extends CrudRepository<PostedJob,Long>{
}
