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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

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

    @GetMapping("/")
    public String index(Model toSend){
    //welcome to the roboresume 3000.  please enter a name and email to continue
    //click a button to continue
        toSend.addAttribute("newPerson", new Person());

        return "index";
    }
    @PostMapping("/")
    public String index(@Valid @ModelAttribute("newPerson") Person newPerson,Model toSend,BindingResult result){
        System.out.println(result.toString());
        if(result.hasErrors()){
            return"index";
        }
        personRepository.save(newPerson);
        return"confirmperson";
    }



    /**
     * click to add another education field then show confirmation page
     * display confirmation entry and allow user to enter a new job
     * buttons:add another education and next(to work experience)
     *
     * 1-10 educational experiences
     */


    @GetMapping("/addeducation")
    public String addEducation(Model toSend){

        toSend.addAttribute("anEducation", new Education());

        return "addeducation";
    }

    @PostMapping("/addeducation")
    public String confirmEducationAndAddMore(@Valid @ModelAttribute("anEducation") Education anEducation,Model toSend, BindingResult result){
        if(result.hasErrors()){
            return "addeducation";
        }
        educationRepository.save(anEducation);

        return "confirmeducation";
    }



    /***have fields that will accept a single job
     * click to add another work experience (this will show a confirmation of the previous
     * entry and have fields avalible to enter a new job
     * buttons: add another and next(to skills)
     *
     * 0-10 work experiences
     */

    @GetMapping("/addwork")
    public String addwork(Model toSend,@ModelAttribute("newPerson") Person newPerson){

        toSend.addAttribute("aJob", new Job());
        return "addwork";
    }
    @PostMapping("/addwork")
    public String confirmWorkAndAddMore(@Valid @ModelAttribute("aJob") Job aJob, @ModelAttribute("newPerson") Person newPerson, BindingResult result){
        if(result.hasErrors()){
            return "addwork";
        }
        jobRepository.save(aJob);
        return "confirmwork";
    }

    /***have fields that will accept a single skill
     * click to add another skill (this will show a confirmation of the previous
     * entry and have fields avalible to enter a new skill
     * buttons: add another skill and next(generate resume)
     *
     * 0-10 work experiences
     */

    @GetMapping("/addskill")
    public String addskill(Model toSend,@ModelAttribute("newPerson") Person newPerson){
        toSend.addAttribute("aSkill", new Skill());

        return "addskill";
    }
    @PostMapping("/addskill")
    public String confirmSkillAndAddMore(@Valid @ModelAttribute("aSkill") Skill aSkill, @ModelAttribute("newPerson") Person newPerson, BindingResult result){
        if(result.hasErrors()) {
            return "addskill";
        }
        skillRepository.save(aSkill);
        return "confirmskill";
    }

    @GetMapping("/generateresume")
    public String generateResume(Model toSend, @ModelAttribute("newPerson") Person newPerson){
        //maybe have buttons to add more education, work, or skills

        Person myPeep = personRepository.findById(1);
        System.out.println("my friends name is " + myPeep.getName());

        toSend.addAttribute("myPerson",myPeep);


       /*
        Iterable<Education> learnz = educationRepository.findAll();
        Iterable<Job> workz = jobRepository.findAll();
        Iterable<Skill> skillz = skillRepository.findAll();
        */


        return "generateresume";
    }





}
