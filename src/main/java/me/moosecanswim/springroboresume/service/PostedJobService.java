package me.moosecanswim.springroboresume.service;

import me.moosecanswim.springroboresume.model.PostedJob;
import me.moosecanswim.springroboresume.model.UserComponent;
import me.moosecanswim.springroboresume.repositories.PostedJobRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PostedJobService {
    @Autowired
    PostedJobRepository postedJobRepository;

    @Autowired
    UserComponent userComponent;

    public void create(PostedJob pJob){
        postedJobRepository.save(pJob);
    }

}
