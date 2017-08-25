package me.moosecanswim.springroboresume.controller;

import me.moosecanswim.springroboresume.model.Education;
import me.moosecanswim.springroboresume.model.Job;
import me.moosecanswim.springroboresume.model.Person;
import me.moosecanswim.springroboresume.model.Skill;
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

  //Add Person
    @GetMapping("/")
    public String index(Model toSend){
    //welcome to the roboresume 3000.  please enter a name and email to continue
    //click a button to continue
        toSend.addAttribute("newPerson", new Person());

        return "index";
    }
    @PostMapping("/")
    public String index(@Valid @ModelAttribute("newPerson") Person newPerson,Model toSend,BindingResult result){
        if(result.hasErrors()){
            return"index";
        }
        personRepository.save(newPerson);
        return"startresume";
    }


//Education
    @GetMapping("/addeducation")
    public String addEducation(Model toSend){
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

    @PostMapping("/addeducation")
    public String confirmEducationAndAddMore(@Valid @ModelAttribute("anEducation") Education anEducation,Model toSend, BindingResult result){
        toSend.addAttribute("isNew",false);
        //refers to the acceptEducation method that will check to make sure the input counts (in the education class)
        //forces user to enter atleast one education (wont take a blank entry)
        if(!anEducation.acceptEducation()){
            return "redirect:/addeducation";
        }
        if(result.hasErrors()){
            return "educationForm";
        }
        educationRepository.save(anEducation);

        return "redirect:/addwork";
    }





//Work
    @GetMapping("/addwork")
    public String addwork(Model toSend,@ModelAttribute("newPerson") Person newPerson){
        toSend.addAttribute("isNew",true);
        toSend.addAttribute("aJob", new Job());
        toSend.addAttribute("jobCount",(jobRepository.count()+1));

        Boolean outOfBounds=null;
        if(jobRepository.count()>10){
            outOfBounds=true;
        }

        return "workForm";
    }
    @PostMapping("/addwork")
    public String confirmWorkAndAddMore(@Valid @ModelAttribute("aJob") Job aJob, @ModelAttribute("newPerson") Person newPerson, BindingResult result,Model toSend){
        toSend.addAttribute("isNew",false);
        if(result.hasErrors()){
            return "workForm";
        }
        jobRepository.save(aJob);
        return "redirect:/addskill";
    }

//Skills
    @GetMapping("/addskill")
    public String addskill(Model toSend,@ModelAttribute("newPerson") Person newPerson){

        toSend.addAttribute("isNew",true);
        toSend.addAttribute("aSkill", new Skill());

        toSend.addAttribute("skillCount", (skillRepository.count()+1));
        Boolean outOfBounds=null;
        if(skillRepository.count()>20){
            outOfBounds=true;
        }
        return "skillForm";
    }
    @PostMapping("/addskill")
    public String confirmSkillAndAddMore(@Valid @ModelAttribute("aSkill") Skill aSkill, @ModelAttribute("newPerson") Person newPerson, BindingResult result, Model toSend){
        toSend.addAttribute("isNew",false);

        //refers to the acceptEducation method that will check to make sure the input counts (in the education class)
        //forces user to enter atleast one education (wont take a blank entry)
        if(!aSkill.acceptSkill()){
            return "resirect:/addskill";
        }

        if(result.hasErrors()) {
            return "skillForm";
        }
        skillRepository.save(aSkill);

        return "redirect:/generateresume";
    }

    //Generate Resume
    @GetMapping("/generateresume")
    public String generateResume(Model toSend, @ModelAttribute("newPerson") Person newPerson){

        Person myPeep = personRepository.findById(1);
        toSend.addAttribute("myPerson",myPeep);//for the name entry

        Boolean educationNeedsWork=false;
        Boolean workNeedsWork=false;
        Boolean skillsNeedsWork=false;
        //reinforcment to have minimum or it asks for you to enter a new info (no "work experience" because there is no minimum
        if(educationRepository.count()<1){
            educationNeedsWork=true;
        }
        //if we have a minimum work experience then we can add this in for conditional formatting
        //this just allows the link to add another job to apear when the table is empty
        if(jobRepository.count()<1){
            workNeedsWork=true;
        }
        if(skillRepository.count()<1){
            skillsNeedsWork=true;
        }
        System.out.println("education needs work " +educationNeedsWork);
        System.out.println("work needs work " +workNeedsWork);
        System.out.println("skills needs work " +skillsNeedsWork);


        toSend.addAttribute("educationNeedsWork",educationNeedsWork);
        toSend.addAttribute("workNeedsWork",workNeedsWork);
        toSend.addAttribute("skillsNeedsWork",skillsNeedsWork);


        //send all repositories
        Iterable<Education> learnz = educationRepository.findAll();
        toSend.addAttribute("myEducation", learnz);
        Iterable<Job> workz = jobRepository.findAll();
        toSend.addAttribute("myWork", workz);
        Iterable<Skill> skillz = skillRepository.findAll();
        toSend.addAttribute("mySkills",skillz);


        return "generateresume";
    }


//Additional Services (update and delete)
    @RequestMapping("/update/{type}/{id}")
    public String update(@PathVariable("type") String type, @PathVariable("id") long id, Model toSend){
        String output=null;
        toSend.addAttribute("anEducation" ,educationRepository.findOne(id));
        toSend.addAttribute("aJob",jobRepository.findOne(id));
        toSend.addAttribute("aSkill",skillRepository.findOne(id));

        toSend.addAttribute("isNew",false);

        switch(type){
            case "education":
                output= "educationForm";
                break;

            case "work":
                output= "workForm";
                break;
            case "skill":
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
