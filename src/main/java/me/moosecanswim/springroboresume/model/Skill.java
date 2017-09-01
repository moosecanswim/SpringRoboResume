package me.moosecanswim.springroboresume.model;

import org.hibernate.validator.constraints.NotBlank;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
public class Skill {
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Id
    private long id;
    @NotBlank
    private String skillName;
    private String rating;
    @ManyToOne(fetch=FetchType.EAGER)
    @JoinColumn(name="personId")
    private Person person;

    private Boolean active;


    public Skill(){
        active=true;
    }


    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public Person getPerson() {
        return person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }
    public String getSkillName() {
        return skillName;
    }

    public void setSkillName(String skillName) {
        this.skillName = skillName;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public boolean acceptSkill(){
        boolean output=true;
        if(getSkillName().isEmpty()){
            output=false;
        }
        //add any more constraints here

        return output;

    }

    @Override
    public String toString() {
        return "Skill{" +
                "id=" + id +
                ", skill='" + skillName + '\'' +
                ", rating='" + rating + '\'' +
                ", person=" + person +
                '}';
    }
}
