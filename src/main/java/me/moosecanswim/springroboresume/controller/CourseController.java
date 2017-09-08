package me.moosecanswim.springroboresume.controller;

import me.moosecanswim.springroboresume.model.Course;
import me.moosecanswim.springroboresume.repositories.CourseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Controller
@RequestMapping("/course")
public class CourseController {
    @Autowired
    CourseRepository courseRepository;

    @GetMapping("/add")
    public String addCourse(Model toSend){
        toSend.addAttribute("isNew",true);
        toSend.addAttribute("aCourse",new Course());
        return "course/formCourse";
    }
    @PostMapping("/add")
    public String addCourse2(@Valid Course aCourse, @RequestParam("courseStartHours") String courseStartHours,
                             @RequestParam("courseStartMin") String courseStartMin, Model toSend,BindingResult result){
        if(result.hasErrors()){
            return "course/formCourse";
        }
        String tempTime= String.format("%s:%s",courseStartHours,courseStartMin);
        aCourse.setCourseStartTime(tempTime);
        courseRepository.save(aCourse);
        return "redirect:/course/showall";
    }

    @PostMapping("/update/{id}")
    public String updateCourse(@Valid Course aCourse,@RequestParam("courseStartHours") String courseStartHours,
                               @RequestParam("courseStartMin") String courseStartMin,BindingResult result){
        System.out.println("min take in:" + courseStartMin);
        if(result.hasErrors()){
            return "course/formCourse";
        }
        String tempTime= String.format("%s:%s",courseStartHours,courseStartMin);
        aCourse.setCourseStartTime(tempTime);
        System.out.println(aCourse.toString());
        courseRepository.save(aCourse);
        return "redirect:/course/showall";
    }

    @GetMapping("/showall")
    public String showCourses(Model toSend){
        toSend.addAttribute("listCourses",courseRepository.findByActive(true));
        return "course/listCourses";
    }
    @GetMapping("/coursepage/{id}")
    public String coursePage(@PathVariable("id")long id, Model toSend){
        //stuff
        Course aCourse=courseRepository.findOne(id);
        toSend.addAttribute("aCourse",aCourse);
        toSend.addAttribute("listOfPeople",aCourse.getCourseStudents());
        return "course/coursePage";
    }
    @RequestMapping("delete/{id}")
    public String delete(@PathVariable("id")long id){
        Course tempCourse=courseRepository.findOne(id);
        if(tempCourse==null){
            System.out.println("course does not exist (cannot delete)");
        }else{
            courseRepository.delete(tempCourse);
        }

        return"redirect:/course/showall";
    }
}
