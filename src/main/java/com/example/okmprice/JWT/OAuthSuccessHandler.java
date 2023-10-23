package com.example.okmprice.JWT;


import com.example.okmprice.DTO.UserRole;
import com.example.okmprice.model.KakaoAccount;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Map;

import static com.example.okmprice.DTO.UserRole.USER;

@Component
public class OAuthSuccessHandler implements AuthenticationSuccessHandler {

    private final JwtService jwtService;

    public OAuthSuccessHandler(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        System.out.printf("OAuth2 로그인 성공\n");
        DefaultOAuth2User oAuth2User = (DefaultOAuth2User) authentication.getPrincipal();


            Map<String ,Object> kakaoAccount = (Map<String, Object>) oAuth2User.getAttributes().get("kakao_account");
            Map<String ,Object> kakaoProfile = (Map<String, Object>) kakaoAccount.get("profile");
            String email = (String) kakaoAccount.get("email");
            String usernmae= (String) kakaoProfile.get("nickname");

            UserInfo userInfo = new UserInfo(email,usernmae);

            String token =  jwtService.generateToken(userInfo);

            response.addHeader("Authorization","Bearer "+token);
            response.setStatus(HttpServletResponse.SC_OK);
            response.sendRedirect("/");







//        response.addHeader("Authorization", "Bearer "+accessToken);
//        response.sendRedirect("/");

    }
}
