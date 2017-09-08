package me.moosecanswim.springroboresume.model;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
public class PostedJob {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private long id;
    @ManyToOne(fetch=FetchType.EAGER)
    @JoinTable(name="recruiter_id")
    private Person recruiter;
    private String title;
    private String employer;
    private int lowSalary;
    private int highSalary;
    private String description;
    @OneToMany(mappedBy="postedJob",cascade=CascadeType.ALL,fetch=FetchType.EAGER)
    private Set<Skill> requiredSkills;
    private Boolean enabled;

    public PostedJob(){
        setRequiredSkills(new HashSet<Skill>());
        setEnabled(true);
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getEmployer() {
        return employer;
    }

    public void setEmployer(String employer) {
        this.employer = employer;
    }

    public int getLowSalary() {
        return lowSalary;
    }

    public void setLowSalary(int lowSalary) {
        this.lowSalary = lowSalary;
    }

    public int getHighSalary() {
        return highSalary;
    }

    public void setHighSalary(int highSalary) {
        this.highSalary = highSalary;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Set<Skill> getRequiredSkills() {
        return requiredSkills;
    }

    public void setRequiredSkills(Set<Skill> requiredSkills) {
        this.requiredSkills = requiredSkills;
    }

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    public Person getRecruiter() {
        return recruiter;
    }

    public void setRecruiter(Person recruiter) {
        this.recruiter = recruiter;
    }
}
