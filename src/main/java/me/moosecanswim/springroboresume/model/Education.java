package me.moosecanswim.springroboresume.model;

import org.hibernate.validator.constraints.NotBlank;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
public class Education  {
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Id
    private long id;
    @NotBlank
    private String school;
    @NotBlank
    private String degree;
    private String graduationDate;


    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "person_id")
    private Person person;

    private Boolean active;

    public Education(){
        active=true;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

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
    public Person getPerson() {
        return person;
    }

    public void setPerson(Person person) {
        this.person = person;
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
