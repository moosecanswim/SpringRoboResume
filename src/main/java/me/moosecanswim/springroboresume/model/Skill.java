package me.moosecanswim.springroboresume.model;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Skill {
    @Id
    private long id;
    private String skill;
    private String rating;

    public String getSkill() {
        return skill;
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
}
