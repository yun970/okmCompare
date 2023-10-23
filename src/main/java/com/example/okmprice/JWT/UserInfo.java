package com.example.okmprice.JWT;

import com.example.okmprice.model.SiteUser;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

import javax.management.relation.Role;
import java.util.*;

public class UserInfo{

    public UserInfo(String email, String username) {
        this.email = email;
        this.username = username;
    }

    private String email;
    private String username;

    public String getEmail() {
        return email;
    }

    public String getUsername() {
        return username;
    }
}
