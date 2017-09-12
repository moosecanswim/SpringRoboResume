package me.moosecanswim.springroboresume.service;


import me.moosecanswim.springroboresume.model.Person;
import me.moosecanswim.springroboresume.repositories.PersonRepository;
import me.moosecanswim.springroboresume.repositories.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;

@Service
public class PersonService {
    @Autowired
    PersonRepository personRepository;
    @Autowired
    RoleRepository roleRepository;
    @Autowired
    public PersonService(PersonRepository userRepository){
        this.personRepository=userRepository;
    }
    public Person create(Person aPerson) {
        Person existingUser = personRepository.findByUsername(aPerson.getUsername());

        if (existingUser != null) {
            throw new RuntimeException("Record already exists!");
        }
        System.out.println("UserService: adding new user " + aPerson.toString());
        aPerson.addRole(roleRepository.findByRole("USER"));
        return personRepository.save(aPerson);
    }

    public void saveSeeker(Person aPerson){
        aPerson.addRole(roleRepository.findByRole("SEEKER"));
        aPerson.setActive(true);
        personRepository.save(aPerson);
    }
    public void saveAdmin(Person aPerson){
        aPerson.addRole(roleRepository.findByRole("RECRUITER"));
        aPerson.setActive(true);
        personRepository.save(aPerson);
    }

    public void updateUser(Person aPerson){
        Person existingUser= personRepository.findOne(aPerson.getId());

        if(existingUser==null){
            System.out.println("PersonService: User does not exist to modify");
        }

        existingUser.setFirstName(aPerson.getFirstName());
        existingUser.setLastName(aPerson.getLastName());
        existingUser.setEmail(aPerson.getEmail());
        existingUser.setUsername(aPerson.getUsername());

        System.out.println("updated user ("+existingUser.getId()+" ) "+existingUser.getUsername());
        personRepository.save(existingUser);
    }

}
