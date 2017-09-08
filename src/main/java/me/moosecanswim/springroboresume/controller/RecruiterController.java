package me.moosecanswim.springroboresume.controller;

import me.moosecanswim.springroboresume.model.Person;
import me.moosecanswim.springroboresume.model.PostedJob;
import me.moosecanswim.springroboresume.model.UserComponent;
import me.moosecanswim.springroboresume.service.PostedJobService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;

@Controller
@RequestMapping("/recruiter")
public class RecruiterController {
    @Autowired
    UserComponent userComponent;
    @Autowired
    PostedJobService postedJobService;

    @GetMapping("/home")
    public String recruiterHome(Model toSend){

        return "recruiter/home";
    }
    @GetMapping("/addjob")
    public String addJob(Model toSend){
        toSend.addAttribute("aPostedJob",new PostedJob());
        return "recruiter/postedJobForm";
    }
    @PostMapping("/addjob")
    public String porcessJob(@Valid PostedJob postedJob, BindingResult result){
        Person myPerson=userComponent.getUser();
        if(result.hasErrors()){
            return "recruiter/postedJobForm";
        }
        postedJob.setRecruiter(myPerson);
        postedJobService.create(postedJob);
        return "redirect:/recruiter/home";
    }

}
