package me.moosecanswim.springroboresume;

import me.moosecanswim.springroboresume.repositories.PersonRepository;
import me.moosecanswim.springroboresume.service.SSPersonDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {
    @Autowired
    SSPersonDetailsService personDetailService;
    @Autowired
    PersonRepository personRepository;
    @Override
    public UserDetailsService userDetailsServiceBean()throws Exception{
        return new SSPersonDetailsService(personRepository);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception{
        http
                .authorizeRequests()
                .antMatchers("/","/css/**", "/js/**", "/font-awesome/**", "/fonts/**","/register/**")
                .permitAll()
//                .antMatchers("/")
//                .access("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
                .antMatchers("/admin").access("hasRole('ROLE_ADMIN')")
                .anyRequest().authenticated()
                .antMatchers("/recruiter").access("hasRole('ROLE_RECRUITER')")
                .anyRequest().authenticated()
                .antMatchers("/seeker").access("hasRole('ROLE_SEEKER')")
                .anyRequest().authenticated()
                .and()
                .formLogin().loginPage("/login").permitAll()
                .and()
                .logout()
                .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                .logoutSuccessUrl("/login").permitAll().permitAll();

        http.csrf().disable();
    }
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception{

        auth.inMemoryAuthentication()
                .withUser("user").password("password").roles("USER","SEEKER","RECRUITER")
                .and()
                .withUser("dave").password("begreat").roles("ADMIN")
                .and()
                .withUser("newuser").password("newuserpa$$").roles("USER");

        auth.userDetailsService(userDetailsServiceBean());
    }
}
