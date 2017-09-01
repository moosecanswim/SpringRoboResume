package me.moosecanswim.springroboresume.controller;

import me.moosecanswim.springroboresume.model.*;
import me.moosecanswim.springroboresume.repositories.EducationRepository;
import me.moosecanswim.springroboresume.repositories.JobRepository;
import me.moosecanswim.springroboresume.repositories.PersonRepository;
import me.moosecanswim.springroboresume.repositories.SkillRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

import static org.springframework.web.bind.annotation.RequestMethod.POST;

@Controller
public class MainController {
    @Autowired
    PersonRepository personRepository;
    @Autowired
    EducationRepository educationRepository;
    @Autowired
    JobRepository jobRepository;
    @Autowired
    SkillRepository skillRepository;
    @Autowired
    UserComponent userComponent;

    @RequestMapping("/login")
    public String login(){
        return "login";
    }


    //Add Person
    @GetMapping("/")
    public String index(Model toSend){
        //welcome to the roboresume 3000.  please enter a name and email to continue
        //click a button to continue
        toSend.addAttribute("newPerson", new Person());
        toSend.addAttribute("listOfPeople", personRepository.findAll());
        return "index";
    }
    @PostMapping("/")
    public String index(@Valid @ModelAttribute("newPerson") Person newPerson,
                        Model toSend,BindingResult result){

        if(result.hasErrors()){
            return"index";
        }

        userComponent.setUser(newPerson);
        personRepository.save(newPerson);
        return"startresume";
    }
    @RequestMapping("/selectUser/{id}")
    public String selectOldUser(@PathVariable("id") long oldUserId){
        System.out.println("User id is: " + oldUserId);
        userComponent.setUser(personRepository.findOne(oldUserId));
        return "redirect:/generateresume";
    }

    //Education
    @GetMapping("/addeducation")
    public String addEducation(Model toSend){
        Person myPerson = userComponent.getUser();
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

        return "educationForm";
    }

    //Save Education and move to Work
    //takes in the button perameter (value) and chooses to add or add another based on it
    @RequestMapping(value="/addeducation", method=POST)
    public String addOneEducation(@Valid @ModelAttribute("anEducation") Education anEducation,
                                  Model toSend, BindingResult result,@RequestParam(value="button") String todo){
        //refers to the acceptEducation method that will check to make sure the input counts (in the education class)
        //forces user to enter atleast one education (wont take a blank entry)
        Person myPerson=userComponent.getUser();
        toSend.addAttribute("isNew",false);
        if(!anEducation.acceptEducation()){
            return "redirect:/addeducation";
        }
        if(result.hasErrors()){
            return "educationForm";
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
            return "educationForm";
        }
        educationRepository.save(anEducation);
        return "redirect:/generateresume";
    }

    //Work
    @GetMapping("/addwork")
    public String addwork(Model toSend,@ModelAttribute("newPerson") Person newPerson){
        Person myPerson=userComponent.getUser();
        toSend.addAttribute("isNew",true);
        toSend.addAttribute("aJob", new Job());
        toSend.addAttribute("jobCount",(jobRepository.countByPersonAndActive(myPerson,true)+1));

        Boolean outOfBounds=false;
        if(jobRepository.countByPersonAndActive(myPerson,true)>9){
            outOfBounds=true;
        }
        toSend.addAttribute("outOfBounds",outOfBounds);
        return "workForm";
    }

    //Save Work and move to Skills
    @RequestMapping(value="/addwork", method=POST)
    public String addOneWork(@Valid @ModelAttribute("aJob") Job aJob,
                             Model toSend, BindingResult result,@RequestParam(value="button") String todo){
        Person myPerson = userComponent.getUser();
        toSend.addAttribute("isNew",false);
        if(result.hasErrors()){
            return "workForm";
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
    public String updateEducation(@Valid Job aJob, BindingResult result){
        //Person myPerson=userComponent.getUser();
        if(result.hasErrors()){
            return "workForm";
        }
        //personRepository.findOne(myPerson.getId()).addJob(aJob);
        jobRepository.save(aJob);
        return "redirect:/generateresume";
    }


    //Skills
    @GetMapping("/addskill")
    public String addskill(Model toSend,@ModelAttribute("newPerson") Person newPerson){
        Person myPerson=userComponent.getUser();
        toSend.addAttribute("isNew",true);
        toSend.addAttribute("aSkill", new Skill());

        toSend.addAttribute("skillCount", (skillRepository.countByPersonAndActive(myPerson,true)+1));
        Boolean outOfBounds=false;
        if(skillRepository.countByPersonAndActive(myPerson,true)>19){
            outOfBounds=true;
        }
        toSend.addAttribute("outOfBounds",outOfBounds);
        return "skillForm";
    }

    //Save skill and move to generate resume
    @RequestMapping(value="/addskill", method=POST)
    public String addOneSkill(@Valid @ModelAttribute("aSkill") Skill aSkill,
                              Model toSend, BindingResult result,@RequestParam(value="button") String todo){
        Person myPerson=userComponent.getUser();
        toSend.addAttribute("isNew",false);
        if(result.hasErrors()){
            return "skillForm";
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
            return "skillForm";
        }
        skillRepository.save(aSkill);
        return "redirect:/generateresume";
    }





    //Generate Resume
    @GetMapping("/generateresume")
    public String generateResume(Model toSend, @ModelAttribute("newPerson") Person newPerson){

        Person myPeep = userComponent.getUser();
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
                output= "educationForm";
                break;

            case "work":
                toSend.addAttribute("aJob",jobRepository.findOne(id));
                output= "workForm";
                break;
            case "skill":
                toSend.addAttribute("aSkill",skillRepository.findOne(id));
                output= "skillForm";
                break;
        }
        return output;

    }
    //actually archives education work and skill traits and removes it from the person's set
    @RequestMapping("/delete/{type}/{id}")
    public String delete(@PathVariable("type") String type,@PathVariable("id") long id){
        Person myPerson = userComponent.getUser();
        switch(type){
            case "education":
                Education tempEducation=educationRepository.findOne(id);

                myPerson.getEducations().remove(tempEducation);
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

                myPerson.getSkills().remove(tempSkill);
                tempSkill.setActive(false);
                skillRepository.save(tempSkill);
                break;
        }
        return "redirect:/generateresume";
    }




}
