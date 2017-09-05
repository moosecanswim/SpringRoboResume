package me.moosecanswim.springroboresume.model;

import javax.persistence.*;
import java.util.Set;

@Entity
public class Course {
    @Id @GeneratedValue(strategy= GenerationType.AUTO)
    private long id;
    private String courseName;
    private String courseDescription;
    private int courseCreditHours;
    private String courseDays;
    private String courseStartTime;


    private Boolean active;

    @ManyToMany(mappedBy="courses")
    private Set<Person> courseStudents;

    public Course(){
        active=true;
    }


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public String getCourseDescription() {
        return courseDescription;
    }

    public void setCourseDescription(String courseDescription) {
        this.courseDescription = courseDescription;
    }

    public int getCourseCreditHours() {
        return courseCreditHours;
    }

    public void setCourseCreditHours(int courseCreditHours) {
        this.courseCreditHours = courseCreditHours;
    }

    public String getCourseDays() {
        return courseDays;
    }

    public void setCourseDays(String courseDays) {
        this.courseDays = courseDays;
    }

    public String getCourseStartTime() {
        return courseStartTime;
    }

    public void setCourseStartTime(String courseStartTime) {
        this.courseStartTime = courseStartTime;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public Set<Person> getCourseStudents() {
        return courseStudents;
    }

    public void setCourseStudents(Set<Person> courseStudents) {
        this.courseStudents = courseStudents;
    }

    @Override
    public String toString() {
        return "Course{" +
                "id=" + id +
                ", courseName='" + courseName + '\'' +
                ", courseDescription='" + courseDescription + '\'' +
                ", courseCreditHours=" + courseCreditHours +
                ", courseDays='" + courseDays + '\'' +
                ", courseStartTime='" + courseStartTime + '\'' +
                ", active=" + active +
                ", courseStudents=" + courseStudents +
                '}';
    }
}
