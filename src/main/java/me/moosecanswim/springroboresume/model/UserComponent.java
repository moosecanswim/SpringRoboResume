package me.moosecanswim.springroboresume.model;

import org.springframework.stereotype.Component;

@Component
public class UserComponent {

    private Person user;
    public Person getUser() {
        return user;
    }

    public void setUser(Person user) {
        this.user = user;
    }

    @Override
    public String toString(){
        return this.user.getFirstName();
    }
}
