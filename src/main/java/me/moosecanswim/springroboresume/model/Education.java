package me.moosecanswim.springroboresume.model;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Education  {
    @Id
    private long id;
    private String school;
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
        this.graduationDate = graduationDate;
    }
}
