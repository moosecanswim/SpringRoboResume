package me.moosecanswim.springroboresume.model;


import javax.persistence.*;
import java.util.Collection;
import java.util.HashSet;

@Entity
public class Role {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private long id;
    private String role;
    @ManyToMany(mappedBy="roles",fetch= FetchType.EAGER)
    private Collection<Person> users;

    public Role(){
        setUsers(new HashSet<Person>());
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public Collection<Person> getUsers() {
        return users;
    }

    public void setUsers(Collection<Person> users) {
        this.users = users;
    }

    @Override
    public String toString() {
        return "Role:" + role+"("+id+")";
    }
}
