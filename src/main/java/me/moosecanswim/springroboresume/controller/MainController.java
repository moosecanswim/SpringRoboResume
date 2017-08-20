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
        return"startresume";
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

        return "educationForm";
    }

    @PostMapping("/addeducation")
    public String confirmEducationAndAddMore(@Valid @ModelAttribute("anEducation") Education anEducation,Model toSend, BindingResult result){
        if(result.hasErrors()){
            return "educationForm";
        }
        educationRepository.save(anEducation);

//        return "confirmeducation";
        return "redirect:/addwork";
    }


    @GetMapping("/addwork")
    public String addwork(Model toSend,@ModelAttribute("newPerson") Person newPerson){

        toSend.addAttribute("aJob", new Job());
        return "workForm";
    }
    @PostMapping("/addwork")
    public String confirmWorkAndAddMore(@Valid @ModelAttribute("aJob") Job aJob, @ModelAttribute("newPerson") Person newPerson, BindingResult result){
        if(result.hasErrors()){
            return "workForm";
        }
        jobRepository.save(aJob);
//        return "confirmwork";
        return "redirect:/addskill";
    }


    @GetMapping("/addskill")
    public String addskill(Model toSend,@ModelAttribute("newPerson") Person newPerson){
        toSend.addAttribute("aSkill", new Skill());

        return "skillForm";
    }
    @PostMapping("/addskill")
    public String confirmSkillAndAddMore(@Valid @ModelAttribute("aSkill") Skill aSkill, @ModelAttribute("newPerson") Person newPerson, BindingResult result){
        if(result.hasErrors()) {
            return "skillForm";
        }
        skillRepository.save(aSkill);
//        return "confirmskill";
        return "redirect:/generateresume";
    }

    @GetMapping("/generateresume")
    public String generateResume(Model toSend, @ModelAttribute("newPerson") Person newPerson){
        //maybe have buttons to add more education, work, or skills

        Person myPeep = personRepository.findById(1);
        System.out.println("my friends name is " + myPeep.getName());

        toSend.addAttribute("myPerson",myPeep);



        Iterable<Education> learnz = educationRepository.findAll();
        toSend.addAttribute("myEducation", learnz);
        Iterable<Job> workz = jobRepository.findAll();
        toSend.addAttribute("myWork", workz);
        Iterable<Skill> skillz = skillRepository.findAll();
        toSend.addAttribute("mySkills",skillz);


        return "generateresume";
    }

    @RequestMapping("/update/{type}/{id}")
    public String update(@PathVariable("type") String type, @PathVariable("id") long id, Model toSend){
        String output=null;
        toSend.addAttribute("anEducation" ,educationRepository.findOne(id));
        toSend.addAttribute("aJob",jobRepository.findOne(id));
        toSend.addAttribute("aSkill",skillRepository.findOne(id));

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
