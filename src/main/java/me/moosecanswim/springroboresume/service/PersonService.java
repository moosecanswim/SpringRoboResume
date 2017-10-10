package me.moosecanswim.springroboresume.service;


import me.moosecanswim.springroboresume.model.Person;
import me.moosecanswim.springroboresume.model.Role;
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
    public Person findPersonByUsername(String username){
        Person output=null;
        Person exPerson=personRepository.findByUsername(username);
        if(exPerson != null){
            output = exPerson;
        }else{
            throw new RuntimeException("the person does not exist");
        }
        return output;
    }

    //check to see if a person has a role
    public Boolean personHasRole(Person aPerson, String aRoleName){
        Boolean output = false;

        Person ePerson = personRepository.findOne(aPerson.getId());
        if(ePerson == null){
            throw new RuntimeException("person does not exist in person repository");
        }
        Role eRole = roleRepository.findByRole(aRoleName);
        if(eRole == null){
            throw new RuntimeException("Role " + aRoleName + "does not exist in role repository");
        }
        if(ePerson.getRoles().contains(eRole)){
            output = true;
        }
        return output;
    }
}
