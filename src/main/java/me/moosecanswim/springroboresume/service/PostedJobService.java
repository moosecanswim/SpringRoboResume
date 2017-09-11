package me.moosecanswim.springroboresume.service;

import me.moosecanswim.springroboresume.model.Person;
import me.moosecanswim.springroboresume.model.PostedJob;
import me.moosecanswim.springroboresume.model.UserComponent;
import me.moosecanswim.springroboresume.repositories.PersonRepository;
import me.moosecanswim.springroboresume.repositories.PostedJobRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class PostedJobService {
    @Autowired
    PostedJobRepository postedJobRepository;
    @Autowired
    PersonRepository personRepository;

    @Autowired
    UserComponent userComponent;

    public void create(PostedJob pJob){
        postedJobRepository.save(pJob);
    }
    public Set<PostedJob> showByRecruiter(Person aPerson){
        Person tempPerson = personRepository.findById(aPerson.getId());
        return postedJobRepository.findByRecruiter(tempPerson);
    }
    public long countByRecruiter(Person aPerson){
        Person tempPerson = personRepository.findById(aPerson.getId());
        return postedJobRepository.countByRecruiter(tempPerson);
    }
    public PostedJob findById(long id){
        PostedJob aPJ = postedJobRepository.findOne(id);
        if(aPJ == null){
            System.out.println("findByID: posted job not found");
        }
        return aPJ;
    }
    public void updateJob(PostedJob aPJ){
        PostedJob ePJ = postedJobRepository.findOne(aPJ.getId());
        if(ePJ == null){
            System.out.println("updateJob: posted job not found");
        }
        else{
            ePJ.setTitle(aPJ.getTitle());
            ePJ.setEmployer(aPJ.getEmployer());
            ePJ.setLowSalary(aPJ.getLowSalary());
            ePJ.setHighSalary(aPJ.getHighSalary());
            ePJ.setDescription(aPJ.getDescription());
            System.out.println("updateJob: Sucessfully updated a posted job");
            postedJobRepository.save(ePJ);
        }
    }
    public void toggleStatus(long id){
        PostedJob ePJ = postedJobRepository.findOne(id);
        if(ePJ == null){
            System.out.println("toggleStatus: posted job not found");
        }
        else{
            if(ePJ.getEnabled()==true) {
                ePJ.setEnabled(false);
                System.out.println("updateJob: Sucessfully updated a posted job's status to false");
            }
            else{
                ePJ.setEnabled(true);
                System.out.println("updateJob: Sucessfully updated a posted job's status to true");
            }
        }
        postedJobRepository.save(ePJ);
    }

}
