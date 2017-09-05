package me.moosecanswim.springroboresume.controller;

import me.moosecanswim.springroboresume.model.Course;
import me.moosecanswim.springroboresume.repositories.CourseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.Valid;

@Controller
public class CourseController {
    @Autowired
    CourseRepository courseRepository;

    @GetMapping("/addCourse")
    public String addCourse(Model toSend){
        toSend.addAttribute("isNew",true);
        toSend.addAttribute("aCourse",new Course());
        return "formCourse";
    }
    @PostMapping("/addCourse")
    public String addCourse2(@Valid Course aCourse, @RequestParam("courseStartHours") String courseStartHours,
                             @RequestParam("courseStartMin") String courseStartMin, Model toSend,BindingResult result){
        if(result.hasErrors()){
            return "formCourse";
        }
        String tempTime= String.format("%s:%s",courseStartHours,courseStartMin);
        aCourse.setCourseStartTime(tempTime);
        courseRepository.save(aCourse);
        return "redirect:/showCourses";
    }

    @PostMapping("/updateCourse")
    public String updateCourse(@Valid Course aCourse,@RequestParam("courseStartHours") String courseStartHours,
                               @RequestParam("courseStartMin") String courseStartMin,BindingResult result){
        System.out.println("min take in:" + courseStartMin);
        if(result.hasErrors()){
            return "formCourse";
        }
        String tempTime= String.format("%s:%s",courseStartHours,courseStartMin);
        aCourse.setCourseStartTime(tempTime);
        System.out.println(aCourse.toString());
        courseRepository.save(aCourse);
        return "redirect:/showCourses";
    }

    @GetMapping("/showCourses")
    public String showCourses(Model toSend){
        toSend.addAttribute("listCourses",courseRepository.findByActive(true));
        return "listCourses";
    }
    @GetMapping("/coursePage/{id}")
    public String coursePage(@PathVariable("id")long id, Model toSend){
        //stuff
        Course aCourse=courseRepository.findOne(id);
        toSend.addAttribute("aCourse",aCourse);
        toSend.addAttribute("listOfPeople",aCourse.getCourseStudents());
        return "coursePage";
    }
}
