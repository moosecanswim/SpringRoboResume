package me.moosecanswim.springroboresume.controller;

import me.moosecanswim.springroboresume.model.Person;
import me.moosecanswim.springroboresume.model.UserComponent;
import me.moosecanswim.springroboresume.service.PersonService;
import me.moosecanswim.springroboresume.service.PostedJobService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;

@Controller
@RequestMapping("/seeker")
public class SeekerController {
   @Autowired
    UserComponent userComponent;
   @Autowired
   PostedJobService postedJobService;
   @Autowired
    PersonService personService;

   @RequestMapping("/home")
    public String seekerHome(Principal p, Model toSend){
       Person tempP=personService.findPersonByUsername(p.getName());
       toSend.addAttribute("myPerson", tempP);
        toSend.addAttribute("allPostedJobs",postedJobService.findAll());
        Boolean hideNotification = true;
        if(tempP.getSkills()==null) {
            hideNotification=true;
        }else{
            hideNotification=false;
            toSend.addAttribute("postedJobsBySkill", postedJobService.findBySkills(tempP.getSkills()));
        }
       toSend.addAttribute("hideNotification",hideNotification);
        //add in a boolean that will allow me to hide the "posted jobs that match your skills section when there are none
        return "seeker/home";
    }

}
