package me.moosecanswim.springroboresume.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;

@Entity
public class Education  {
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Id
    @NotNull
    private long id;
    @NotNull
    private String school;
    @NotNull
    private String degree;
    private String graduationDate;

    public String getDegree() {
        return degree;
    }

    public void setDegree(String degree) {
        this.degree = degree;
    }

    public String getSchool() {
        return school;
    }

    public void setSchool(String school) {
        this.school = school;
    }


    public long getId() {
        return id;
    }

    public String getGraduationDate() {
        return graduationDate;
    }

    public void setGraduationDate(String graduationDate) {
       //says they are currently enrolled if there is no graduation date entered
        if(graduationDate.isEmpty()){
           this.graduationDate = "Currently Enrolled";
       }else {
           this.graduationDate = graduationDate;
       }
    }

    public void setId(long id) {
        this.id = id;
    }

    public boolean acceptEducation(){
        boolean output = true;
        if(getSchool().isEmpty()){
            output=false;
        }
        if(getDegree().isEmpty()){
            output=false;
        }
        return output;
    }

}
