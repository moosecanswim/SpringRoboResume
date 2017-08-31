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
    private String skill;
    private String rating;

    public String getSkill() {
        return skill;
    }
    @ManyToOne(fetch=FetchType.EAGER)
    @JoinColumn(name="personId")
    private Person person;

    public Person getPerson() {
        return person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }

    public void setSkill(String skill) {
        this.skill = skill;
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
        if(getSkill().isEmpty()){
            output=false;
        }
        //add any more constraints here

        return output;

    }

}
