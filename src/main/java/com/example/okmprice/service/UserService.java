package com.example.okmprice.service;

import com.example.okmprice.model.SiteUser;
import com.example.okmprice.repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public void accountCreate(String email, String password){
        SiteUser siteUser = new SiteUser();
        siteUser.setEmail(email);
        siteUser.setPassword(passwordEncoder.encode(password));
        userRepository.save(siteUser);
    }

    public void kakaoCreate(String email, Long id, String password){
        SiteUser siteUser = new SiteUser();
        siteUser.setEmail(email+id);
        siteUser.setPassword(passwordEncoder.encode(password));
        userRepository.save(siteUser);
    }

    public Optional<SiteUser> userFind(String email){
        return userRepository.findByEmail(email);
    }
}
