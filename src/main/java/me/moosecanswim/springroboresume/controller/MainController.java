package me.moosecanswim.springroboresume.controller;

import me.moosecanswim.springroboresume.model.Person;
import me.moosecanswim.springroboresume.repositories.EducationRepository;
import me.moosecanswim.springroboresume.repositories.JobRepository;
import me.moosecanswim.springroboresume.repositories.SkillRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class MainController {

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

    /**
     * click to add another education field then show confirmation page
     * display confirmation entry and allow user to enter a new job
     * buttons:add another education and next(to work experience)
     *
     * 1-10 educational experiences
     */


    @GetMapping("/addeducation")
    public String addEducation(){

        return "addeducation";
    }
    @PostMapping("/addeducation")
    public String confirmEducationAndAddMore(){

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
    public String addwork(){


        return "addwork";
    }
    @PostMapping("/addwork")
    public String confirmWorkAndAddMore(){

        return "addwork";
    }

    /***have fields that will accept a single skill
     * click to add another skill (this will show a confirmation of the previous
     * entry and have fields avalible to enter a new skill
     * buttons: add another skill and next(generate resume)
     *
     * 0-10 work experiences
     */

    @GetMapping("/addskill")
    public String addskill(){


        return "addskill";
    }
    @PostMapping("/addskill")
    public String confirmSkillAndAddMore(){

        return "addskill";
    }

    @GetMapping("/generateresume")
    public String generateResume(){
        //maybe have buttons to add more education, work, or skills

        return "generateresume";
    }


}
