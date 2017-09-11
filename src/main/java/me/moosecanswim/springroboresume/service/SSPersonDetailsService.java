package me.moosecanswim.springroboresume.service;

import me.moosecanswim.springroboresume.model.Person;
import me.moosecanswim.springroboresume.model.Role;
import me.moosecanswim.springroboresume.repositories.PersonRepository;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.HashSet;
import java.util.Set;

@Transactional
@Service
public class SSPersonDetailsService implements UserDetailsService {

    private PersonRepository personRepository;

    public SSPersonDetailsService(PersonRepository personRepository){
        this.personRepository=personRepository;
    }


    @Override
    public UserDetails loadUserByUsername(String username){
        try{
            System.out.println("Username entered is " +username);
            System.out.println("SSPersonDetailService: searching by username");

            Person tempPerson= personRepository.findByUsername(username);
            System.out.println("tempPerson is " + tempPerson.getUsername());
            if(tempPerson==null){
                System.out.println("SSPersonDetailService: user [" + username + "] not found with provided username");
                return null;
            }
            return new org.springframework.security.core.userdetails.User(tempPerson.getUsername(),tempPerson.getPassword(),getAuthorities(tempPerson));
        }
        catch(Exception e){
            System.out.println("SSPersonDetailsService: exception! " +e.toString());

            throw new UsernameNotFoundException("SSUserDetailService:User not found");
        }
    }
    private Set<GrantedAuthority> getAuthorities(Person aPerson){
        Set<GrantedAuthority> authorities=new HashSet<GrantedAuthority>();

        for(Role role:aPerson.getRoles()){
            GrantedAuthority grantedAuthority= new SimpleGrantedAuthority(role.getRole());
            authorities.add(grantedAuthority);
        }
        return authorities;
    }
}
