package com.example.okmprice.service;

import com.example.okmprice.OAuth.OAuthAttributes;
import com.example.okmprice.model.SiteUser;
import com.example.okmprice.repository.UserRepository;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    private final UserRepository userRepository;


    public CustomOAuth2UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }


    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2UserService<OAuth2UserRequest, OAuth2User> service = new DefaultOAuth2UserService();
        System.out.printf(userRequest.getAccessToken().getTokenValue());
        OAuth2User user = service.loadUser(userRequest);

        System.out.printf(userRequest.getAccessToken().getTokenValue());
        System.out.printf(" 유저네임 :  ",user.getAttributes().toString());

        Map<String, Object> attr = user.getAttributes();

        String registrationId = userRequest.getClientRegistration().getRegistrationId();

        OAuthAttributes attributes = OAuthAttributes.of(registrationId, attr);

        SiteUser _siteUser = userCheck(attributes);
        String email = _siteUser.getEmail();
//        List<GrantedAuthority> authorities = "USER_ROLE";
        return new DefaultOAuth2User(
                Collections.singleton(new SimpleGrantedAuthority("ROLE_USER")),
                attributes.getAttr(),
                attributes.getNameAttributeKey()
        );
    }

    private SiteUser userCheck(OAuthAttributes attributes){
         Optional<SiteUser> siteUser = userRepository.findByEmail(attributes.getEmail());
         if(!(siteUser.isPresent())){
             SiteUser user = new SiteUser();
             user.setEmail(attributes.getEmail());
             user.setUsername(attributes.getName());
             user.setPassword("asdfasdfasdf123123123");
             return userRepository.save(user);
         }

         return siteUser.get();
    }
}
