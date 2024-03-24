package ru.veselov.springcourse.FirstSecurityApp.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import ru.veselov.springcourse.FirstSecurityApp.services.PersonDetailsService;

import java.util.Collections;

//compare password and username from form with data in DB
@Component
public class AuthProviderImpl implements AuthenticationProvider {

    private final PersonDetailsService personDetailsService;

    @Autowired
    public AuthProviderImpl(PersonDetailsService personDetailsService) {
        this.personDetailsService = personDetailsService;
    }

    //spring passes "authentication" object by itself(automatically). Object has Username and password from the view
    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String username = authentication.getName();//from the view

        UserDetails personDetails = personDetailsService.loadUserByUsername(username);

        String password = authentication.getCredentials().toString(); //get password from the view

        if (!password.equals(personDetails.getPassword()))
            throw new BadCredentialsException("Incorrect password 1"); //spring security will catch

        return new UsernamePasswordAuthenticationToken(personDetails, password,
                Collections.emptyList()); //return authentication object with principal - (personDetails),
        // password and list of user rights
    } //UsernamePasswordAuthenticationToken - implements Authentication. This object will be put into session


    //for which object this provider works
    @Override
    public boolean supports(Class<?> aClass) {
        return true;
    }
}
