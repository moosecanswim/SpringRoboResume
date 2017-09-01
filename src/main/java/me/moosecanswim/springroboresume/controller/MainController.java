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

        System.out.println("Current user is " + userComponent.getUser().toString());
        toSend.addAttribute("isNew",true);
        toSend.addAttribute("anEducation", new Education());

        toSend.addAttribute("educationCount",(educationRepository.count()+1));

        Boolean outOfBounds=false;
        if(educationRepository.count()>9){
            outOfBounds=true;
        }
        System.out.println("education repo has " + educationRepository.count() + " enteries");
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
        toSend.addAttribute("isNew",false);
        if(!anEducation.acceptEducation()){
            return "redirect:/addeducation";
        }
        if(result.hasErrors()){
            return "educationForm";
        }
        Boolean outOfBounds=false;
        if(educationRepository.count()>9){
            outOfBounds=true;
        }
        toSend.addAttribute("outOfBounds",outOfBounds);
        anEducation.setPerson(userComponent.getUser());
        personRepository.findOne(userComponent.getUser().getId()).addEducation(anEducation);
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
        toSend.addAttribute("isNew",true);
        toSend.addAttribute("aJob", new Job());
        toSend.addAttribute("jobCount",(jobRepository.count()+1));

        Boolean outOfBounds=false;
        if(jobRepository.count()>9){
            outOfBounds=true;
        }
        toSend.addAttribute("outOfBounds",outOfBounds);
        return "workForm";
    }

    //Save Work and move to Skills
    @RequestMapping(value="/addwork", method=POST)
    public String addOneWork(@Valid @ModelAttribute("aJob") Job aJob,
                             Model toSend, BindingResult result,@RequestParam(value="button") String todo){
        toSend.addAttribute("isNew",false);
        if(result.hasErrors()){
            return "workForm";
        }
        Boolean outOfBounds=false;
        if(jobRepository.count()>9){
            outOfBounds=true;
        }
        toSend.addAttribute("outOfBounds",outOfBounds);
        aJob.setPerson(userComponent.getUser());
        personRepository.findOne(userComponent.getUser().getId()).addJob(aJob);
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
        if(result.hasErrors()){
            return "workForm";
        }
        jobRepository.save(aJob);
        return "redirect:/generateresume";
    }


    //Skills
    @GetMapping("/addskill")
    public String addskill(Model toSend,@ModelAttribute("newPerson") Person newPerson){

        toSend.addAttribute("isNew",true);
        toSend.addAttribute("aSkill", new Skill());

        toSend.addAttribute("skillCount", (skillRepository.count()+1));
        Boolean outOfBounds=false;
        if(skillRepository.count()>19){
            outOfBounds=true;
        }
        toSend.addAttribute("outOfBounds",outOfBounds);
        return "skillForm";
    }

    //Save skill and move to generate resume
    @RequestMapping(value="/addskill", method=POST)
    public String addOneSkill(@Valid @ModelAttribute("aSkill") Skill aSkill,
                              Model toSend, BindingResult result,@RequestParam(value="button") String todo){
        toSend.addAttribute("isNew",false);
        if(result.hasErrors()){
            return "skillForm";
        }
        Boolean outOfBounds=false;
        if(skillRepository.count()>19){
            outOfBounds=true;
        }
        toSend.addAttribute("outOfBounds",outOfBounds);
        aSkill.setPerson(userComponent.getUser());
        personRepository.findOne(userComponent.getUser().getId()).addSkill(aSkill);
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


        long eduCount=educationRepository.countByPerson(myPeep);
        long jobCount=jobRepository.countByPerson(myPeep);
        long skillCount=skillRepository.countByPerson(myPeep);
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
        Iterable<Education> learnz = educationRepository.findByPerson(userComponent.getUser());
        toSend.addAttribute("myEducation", learnz);
        Iterable<Job> workz = jobRepository.findByPerson(userComponent.getUser());
        toSend.addAttribute("myWork", workz);
        Iterable<Skill> skillz = skillRepository.findByPerson(userComponent.getUser());
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

    @RequestMapping("/delete/{type}/{id}")
    public String delete(@PathVariable("type") String type,@PathVariable("id") long id){
        switch(type){
            case "education":
                educationRepository.delete(id);
            break;
            case "work":
                jobRepository.delete(id);
            break;
            case "skill":
                skillRepository.delete(id);
            break;
        }
        return "redirect:/generateresume";
    }




}
