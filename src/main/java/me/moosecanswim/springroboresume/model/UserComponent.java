package me.moosecanswim.springroboresume.model;

import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;

@Component
@Scope(proxyMode= ScopedProxyMode.TARGET_CLASS,value="session")
public class UserComponent {

    private Person user;
    public UserComponent(){
        user=new Person();
    }

    public Person getUser() {
        return user;
    }


    public void setUser(Person user) {
        this.user = user;
    }

    @Override
    public String toString(){
        return this.user.getUsername();
    }
}
