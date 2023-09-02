package com.example.okmprice.service;

import com.example.okmprice.model.SiteUser;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    private final AuthenticationManager authenticationManager;

    public AuthService(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    public SiteUser authenticate(SiteUser siteUser){
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(siteUser.getEmail(), siteUser.getPassword())
        );
        return siteUser;
    }
}
