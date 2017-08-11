package me.moosecanswim.springroboresume.model;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Education  {
    @Id
    private long id;
    private String degree;
    private String school;
    private String graduatiouDate;

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

    public String getGraduatiouDate() {
        return graduatiouDate;
    }

    public void setGraduatiouDate(String graduatiouDate) {
        this.graduatiouDate = graduatiouDate;
    }

    public long getId() {
        return id;
    }
}
