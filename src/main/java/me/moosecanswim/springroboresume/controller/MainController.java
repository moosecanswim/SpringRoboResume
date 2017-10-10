package me.moosecanswim.springroboresume.controller;

import me.moosecanswim.springroboresume.model.*;
import me.moosecanswim.springroboresume.repositories.*;
import me.moosecanswim.springroboresume.service.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

import static org.springframework.web.bind.annotation.RequestMethod.POST;

@Controller
public class MainController {
    @Autowired
    PersonRepository personRepository;
    @Autowired
    PersonService personService;
    @Autowired
    EducationRepository educationRepository;
    @Autowired
    JobRepository jobRepository;
    @Autowired
    SkillRepository skillRepository;
    @Autowired
    CourseRepository courseRepository;
    @Autowired
    RoleRepository roleRepository;
//    @Autowired
//    UserComponent userComponent;

    @RequestMapping("/login")
    public String login(){
        //make sure to assign the userComponent here
        return "login";
    }


    //Welcome Home
    @GetMapping("/")
    public String index(Model toSend,Principal p){
        if(roleRepository.count()<1){
            addDefaults();
        }
//        if(p!=null){
//            userComponent.setUser(personRepository.findByUsername(p.getName()));
//            String uname = userComponent.getUser().getUsername();
//            System.out.println(uname);
//            toSend.addAttribute("username",uname);
//        }

        toSend.addAttribute("listOfPeople", personRepository.findAll());
//        return "index";
        return "welcome";
    }
    @RequestMapping("/processlogin")
    public String processLogin(Principal p){
        //could have an issue wh`en passing the roles in a string or if someone is assigned both roles
        System.out.println("the principal is " +p.getName());
        Person tempP = personService.findPersonByUsername(p.getName());
        System.out.println(String.format("the principal %s has (%d) roles",tempP.getUsername(),tempP.getRoles().size()));
        if(personService.personHasRole(tempP,"RECRUITER")){
            //send them to the admin home
            System.out.println("Going to Recruiter home");
            return "redirect:/recruiter/home";
        }
        else if(personService.personHasRole(tempP,"SEEKER")){
            //send them to teacher home
            System.out.println("Going to Seeker home");
            return "redirect:/generateresume";
        }
        System.out.println("Error, going home home");
        return "redirect:/";
    }

    @GetMapping("/register")
    public String newUser(Model toSend){
        toSend.addAttribute("isNew",true);
        toSend.addAttribute("newPerson", new Person());
        toSend.addAttribute("listOfPeople", personRepository.findAll());
        return "elements/formPerson";
    }
    @PostMapping("/register")
    public String newUser2(@Valid @ModelAttribute("newPerson") Person newPerson,
                           Model toSend,BindingResult result){

        if(result.hasErrors()){
            return"elements/formPerson";
        }
        //userComponent.setUser(newPerson);
        personRepository.save(newPerson);
        return"elements/startresume";
    }

    @RequestMapping("/updatePerson")
    public String updatePerson(@Valid Person newPerson,BindingResult result){
        if(result.hasErrors()){
            return "elements/formPerson";
        }
        personRepository.save(newPerson);
        return"redirect:/generateresume";
    }

    @RequestMapping("/selectUser/{id}")
    public String selectOldUser(@PathVariable("id") long oldUserId){
        System.out.println("User id is: " + oldUserId);
        //userComponent.setUser(personRepository.findOne(oldUserId));
        return "redirect:/generateresume";
    }

    //Education
    @GetMapping("/addeducation")
    public String addEducation(Model toSend, Principal p){
        Person myPerson = personService.findPersonByUsername(p.getName());
        System.out.println("Current user is " + myPerson.toString());
        toSend.addAttribute("isNew",true);
        toSend.addAttribute("anEducation", new Education());

        toSend.addAttribute("educationCount",(educationRepository.countByPersonAndActive(myPerson,true)+1));

        Boolean outOfBounds=false;
        if(educationRepository.countByPersonAndActive(myPerson,true)>9){
            outOfBounds=true;
        }
        System.out.println("education repo has " + educationRepository.countByPersonAndActive(myPerson,true) + " enteries");
        System.out.println(outOfBounds);
        toSend.addAttribute("outOfBounds",outOfBounds);

        return "elements/educationForm";
    }

    //Save Education and move to Work
    //takes in the button perameter (value) and chooses to add or add another based on it
    @RequestMapping(value="/addeducation", method=POST)
    public String addOneEducation(@Valid @ModelAttribute("anEducation") Education anEducation,
                                  Model toSend, BindingResult result,@RequestParam(value="button") String todo, Principal p){
        //refers to the acceptEducation method that will check to make sure the input counts (in the education class)
        //forces user to enter atleast one education (wont take a blank entry)
        Person myPerson=personService.findPersonByUsername(p.getName());
        toSend.addAttribute("isNew",false);
        if(!anEducation.acceptEducation()){
            return "redirect:/addeducation";
        }
        if(result.hasErrors()){
            return "elements/educationForm";
        }
        Boolean outOfBounds=false;
        if(educationRepository.countByPersonAndActive(myPerson,true)>9){
            outOfBounds=true;
        }
        toSend.addAttribute("outOfBounds",outOfBounds);
        anEducation.setPerson(myPerson);
        personRepository.findOne(myPerson.getId()).addEducation(anEducation);
        educationRepository.save(anEducation);

        //this determines where we go
        switch(todo){
            case "Save":
                //save and move on
                return "redirect:/generateresume";

            case "AddAnother":
                //save and open a new one
                return "redirect:/addeducation";
            case "Update":
                return "redirect:/generateresume";
        }

        return "generateresume";//if i go there then i have an error
    }

    @RequestMapping("/updateEducation")
    public String updateEducation(@Valid Education anEducation, BindingResult result){
        if(result.hasErrors()){
            return "elements/educationForm";
        }
        educationRepository.save(anEducation);
        return "redirect:/generateresume";
    }

    //Work
    @GetMapping("/addwork")
    public String addwork(Model toSend,@ModelAttribute("newPerson") Person newPerson, Principal p){
        Person myPerson=personService.findPersonByUsername(p.getName());
        toSend.addAttribute("isNew",true);
        toSend.addAttribute("aJob", new Job());
        toSend.addAttribute("jobCount",(jobRepository.countByPersonAndActive(myPerson,true)+1));

        Boolean outOfBounds=false;
        if(jobRepository.countByPersonAndActive(myPerson,true)>9){
            outOfBounds=true;
        }
        toSend.addAttribute("outOfBounds",outOfBounds);
        return "elements/workForm";
    }

    //Save Work and move to Skills
    @RequestMapping(value="/addwork", method=POST)
    public String addOneWork(@Valid @ModelAttribute("aJob") Job aJob,
                             Model toSend, BindingResult result,@RequestParam(value="button") String todo, Principal p){
        Person myPerson = personService.findPersonByUsername(p.getName());
        toSend.addAttribute("isNew",false);
        if(result.hasErrors()){
            return "elements/workForm";
        }
        Boolean outOfBounds=false;
        if(jobRepository.countByPersonAndActive(myPerson,true)>9){
            outOfBounds=true;
        }
        toSend.addAttribute("outOfBounds",outOfBounds);
        aJob.setPerson(myPerson);
        personRepository.findOne(myPerson.getId()).addJob(aJob);
        jobRepository.save(aJob);

        //this determines where we go
        switch(todo){
            case "Save":
                //save and move on
                return "redirect:/generateresume";

            case "AddAnother":
                //save and open a new one
                return "redirect:/addwork";
            case "Update":
                return "redirect:/generateresume";
        }

        return "redirect:/generateresume";
    }

    @RequestMapping("/updateWork")
    public String updateEducation(@Valid Job aJob, BindingResult result, Principal p){
        //Person myPerson=personService.findPersonByUsername(p.getName());
        if(result.hasErrors()){
            return "elements/workForm";
        }
        //personRepository.findOne(myPerson.getId()).addJob(aJob);
        jobRepository.save(aJob);
        return "redirect:/generateresume";
    }


    //Skills
    @GetMapping("/addskill")
    public String addskill(Model toSend,@ModelAttribute("newPerson") Person newPerson, Principal p){
        Person myPerson=personService.findPersonByUsername(p.getName());
        toSend.addAttribute("isNew",true);
        toSend.addAttribute("aSkill", new Skill());

        toSend.addAttribute("skillCount", (skillRepository.countByPersonAndActive(myPerson,true)+1));
        Boolean outOfBounds=false;
        if(skillRepository.countByPersonAndActive(myPerson,true)>19){
            outOfBounds=true;
        }
        toSend.addAttribute("outOfBounds",outOfBounds);
        return "elements/skillForm";
    }

    //Save skill and move to generate resume
    @RequestMapping(value="/addskill", method=POST)
    public String addOneSkill(@Valid @ModelAttribute("aSkill") Skill aSkill,
                              Model toSend, BindingResult result,@RequestParam(value="button") String todo, Principal p){
        Person myPerson=personService.findPersonByUsername(p.getName());
        toSend.addAttribute("isNew",false);
        if(result.hasErrors()){
            return "elements/skillForm";
        }
        Boolean outOfBounds=false;
        if(skillRepository.countByPersonAndActive(myPerson,true)>19){
            outOfBounds=true;
        }
        toSend.addAttribute("outOfBounds",outOfBounds);
        aSkill.setPerson(myPerson);
        personRepository.findOne(myPerson.getId()).addSkill(aSkill);
        skillRepository.save(aSkill);
        //this determines where we go
        switch(todo){
            case "Save":
                //save and move on
                return "redirect:/generateresume";

            case "AddAnother":
                //save and open a new one
                return "redirect:/addskill";
            case "Update":
                return "redirect:/generateresume";
        }

        return "redirect:/generateresume";
    }

    @RequestMapping("/updateSkill")
    public String updateEducation(@Valid Skill aSkill, BindingResult result){
        System.out.println(aSkill.toString());
        if(result.hasErrors()){
            return "elements/skillForm";
        }
        skillRepository.save(aSkill);
        return "redirect:/generateresume";
    }





    //Generate Resume
    @GetMapping("/generateresume")
    public String generateResume(Principal p,Model toSend, @ModelAttribute("newPerson") Person newPerson){

        Person myPeep = personService.findPersonByUsername(p.getName());
        toSend.addAttribute("myPerson",myPeep);//for the name entry


        long eduCount=educationRepository.countByPersonAndActive(myPeep,true);
        long jobCount=jobRepository.countByPersonAndActive(myPeep,true);
        long skillCount=skillRepository.countByPersonAndActive(myPeep,true);
        toSend.addAttribute("eduCount",eduCount);
        toSend.addAttribute("jobCount",jobCount);
        toSend.addAttribute("skillCount",skillCount);


        //This will monitor the minimum needed (and add a link on the generate page if more are needed)
        Boolean educationNeedsWork=false;
        if(eduCount<1){
            educationNeedsWork=true;
        }
        Boolean workNeedsWork=false; //this allows me to enagle a css verification on generate resume.  currently true and false have the same outcome
        if(jobCount<1){
            workNeedsWork=true;
        }
        Boolean skillsNeedsWork=false;
        if(skillCount<1){
            skillsNeedsWork=true;
        }

        toSend.addAttribute("educationNeedsWork",educationNeedsWork);
        toSend.addAttribute("workNeedsWork",workNeedsWork);
        toSend.addAttribute("skillsNeedsWork",skillsNeedsWork);


        //This will be used to hide the add buttons once we've met the max
        Boolean tooMuchEducation=false;
        if(eduCount>9){
            tooMuchEducation=true;
        }
        Boolean tooMuchWork=false;
        if(jobCount>9){
            tooMuchWork=true;
        }
        Boolean tooMuchSkills=false;
        if(skillCount>19){//should be 19 (4 for testing)
            tooMuchSkills=true;
        }
        toSend.addAttribute("tooMuchEducation",tooMuchEducation);
        toSend.addAttribute("tooMuchWork",tooMuchWork);
        toSend.addAttribute("tooMuchSkills",tooMuchSkills);



        //send all repositories
        Iterable<Education> learnz = educationRepository.findByPersonAndActive(myPeep,true);
        toSend.addAttribute("myEducation", learnz);
        Iterable<Job> workz = jobRepository.findByPersonAndActive(myPeep,true);
        toSend.addAttribute("myWork", workz);
        Iterable<Skill> skillz = skillRepository.findByPersonAndActive(myPeep,true);
        toSend.addAttribute("mySkills",skillz);


        return "generateresume";
    }


    //Additional Services (update and delete)
    @RequestMapping("/update/{type}/{id}")
    public String update(@PathVariable("type") String type, @PathVariable("id") long id, Model toSend){
        String output=null;

        toSend.addAttribute("isNew",false);

        switch(type){
            case "education":
                toSend.addAttribute("anEducation" ,educationRepository.findOne(id));
                output= "elements/educationForm";
                break;

            case "work":
                toSend.addAttribute("aJob",jobRepository.findOne(id));
                output= "elements/workForm";
                break;
            case "skill":
                toSend.addAttribute("aSkill",skillRepository.findOne(id));
                output= "elements/skillForm";
                break;
            case "person":
                toSend.addAttribute("newPerson",personRepository.findOne(id));
                output="elements/formPerson";
                break;
            case "course":
                Course aCourse=courseRepository.findOne(id);
                String tempTime=aCourse.getCourseStartTime();
                String hour=tempTime.substring(0,1);
                String min=tempTime.substring(3,4);
                toSend.addAttribute("courseStartHour",hour);
                toSend.addAttribute("courseStartMin",min);
                toSend.addAttribute("aCourse",aCourse);
                output="course/formCourse";
                break;
        }
        return output;

    }
    //actually archives education work and skill traits and removes it from the person's set
    @RequestMapping("/delete/{type}/{id}")
    public String delete(@PathVariable("type") String type,@PathVariable("id") long id,Principal p){
        Person myPerson = personService.findPersonByUsername(p.getName());
        switch(type){
            case "education":
                Education tempEducation=educationRepository.findOne(id);

                //myPerson.getEducations().remove(tempEducation);
                tempEducation.setActive(false);
                educationRepository.save(tempEducation);
                break;
            case "work":
                Job tempJob=jobRepository.findOne(id);

                //myPerson.getJobs().remove(tempJob);
                tempJob.setActive(false);
                jobRepository.save(tempJob);
                break;
            case "skill":
                System.out.println("deleteing skill");
                Skill tempSkill=skillRepository.findOne(id);

                //myPerson.getSkills().remove(tempSkill);
                tempSkill.setActive(false);
                skillRepository.save(tempSkill);
                break;
        }
        return "redirect:/generateresume";
    }
    @RequestMapping("/assignSeeker")
    public String assignSeeker(Principal p){
        Person tempUser = personRepository.findByUsername(p.getName());
        tempUser.addRole(roleRepository.findByRole("SEEKER"));
        personRepository.save(tempUser);
        return "redirect:/seeker/home";
    }
    @RequestMapping("/assignRecruiter")
    public String assignRecruiter(Principal p){
        //assign the user to be a recruiter
        Person tempUser = personRepository.findByUsername(p.getName());
        tempUser.addRole(roleRepository.findByRole("RECRUITER"));
        personRepository.save(tempUser);
        return "redirect:/recruiter/home";
    }

    public void addDefaults(){
        Role role1 = new Role();
        role1.setId(1);
        role1.setRole("ADMIN");

        Role role2 = new Role();
        role2.setId(2);
        role2.setRole("SEEKER");

        Role role3 = new Role();
        role3.setId(3);
        role3.setRole("RECRUITER");

        roleRepository.save(role1);
        roleRepository.save(role2);
        roleRepository.save(role3);

    }


}
