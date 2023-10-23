package com.example.okmprice.controller;

import com.example.okmprice.DTO.UserDTO;
import com.example.okmprice.model.KakaoUserInfo;
import com.example.okmprice.model.OAuthToken;
import com.example.okmprice.model.SiteUser;
import com.example.okmprice.service.CustomOAuth2UserService;
import com.example.okmprice.service.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;

@Controller
@RequestMapping("/user")
public class UserController {
    @Value(value = "${kakao.key}")
    String key;
    private final UserService userService;
    private final AuthenticationManager authenticationManager;

    private final CustomOAuth2UserService customOAuth2UserService;

    public UserController(UserService userService, AuthenticationManager authenticationManager, CustomOAuth2UserService customOAuth2UserService) {
        this.userService = userService;
        this.authenticationManager = authenticationManager;
        this.customOAuth2UserService = customOAuth2UserService;
    }

    @GetMapping(value={"","/"})
    public String main(Model model,@SessionAttribute(name = "userId", required = false) String userId){
        System.out.printf("홈페이지 도착");
        Optional<SiteUser> _loginUser = userService.userFind(userId);

        if((_loginUser.isPresent())){
            model.addAttribute("userId", _loginUser.get().getEmail());
            System.out.printf("세션 있음");
        }


        return "main";
    }
    @GetMapping("/signup")
    public String signup(UserDTO userDTO){
        return "signup";
    }

    @PostMapping("/signup")
    public String signup(@Valid UserDTO userDTO, BindingResult bindingResult){
        if (bindingResult.hasErrors()){
            return "signup";
        }

        if (!userDTO.getPassword1().equals(userDTO.getPassword2())){
            bindingResult.rejectValue("password2","passwordInCorrect","패스워드가 일치하지 않습니다.");
            return  "signup";
        }
        try{
            userService.accountCreate(userDTO.getEmail(), userDTO.getPassword1());
        }catch (DataIntegrityViolationException e){
            e.printStackTrace();
            bindingResult.reject("signupFailed","이미 등록된 사용자입니다.");
            return "signup";
        }catch (Exception e){
            e.printStackTrace();
            bindingResult.reject("signupFailed", e.getMessage());
                return "signup";
        }

        return "redirect:/";
    }

    @GetMapping("/login")
    public String loginPage(){
        return "login";
    }

//    @GetMapping("login/kakao")
//    public String kakaoLogin(@RequestParam String code, HttpServletRequest httpServletRequest, Model model){
//
//        RestTemplate restTemplate = new RestTemplate();
//        HttpHeaders httpHeaders = new HttpHeaders();
//        httpHeaders.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");
//        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
//        params.add("grant_type","authorization_code");
//        params.add("client_id","adefbec7c74f180cc2e07ed5468bb845");
//        params.add("redirect_uri","http://localhost:8080/user/login/kakao");
//        params.add("code",code);
//
//        HttpEntity<MultiValueMap<String ,String >> kakaoTokenRequest = new HttpEntity<>(params,httpHeaders);
//
//        ResponseEntity<String> response = restTemplate.exchange(
//                "https://kauth.kakao.com/oauth/token",
//                HttpMethod.POST,
//                kakaoTokenRequest,
//                String.class
//        );
//
//        ObjectMapper objectMapper = new ObjectMapper();
//
//        OAuthToken oAuthToken = null;
//        try {
//            oAuthToken = objectMapper.readValue(response.getBody(), OAuthToken.class);
//        } catch (JsonProcessingException e) {
//            e.printStackTrace();
//        }
//
//        RestTemplate restTemplate2 = new RestTemplate();
//        HttpHeaders httpHeaders2 = new HttpHeaders();
//        httpHeaders2.add("Content-type", "application/x-www-form-urlencoded");
//        httpHeaders2.add("Authorization","Bearer "+oAuthToken.getAccess_token());
//
//
//        HttpEntity<MultiValueMap<String ,String >> kakaoProfileRequest = new HttpEntity<>(httpHeaders2);
//
//        ResponseEntity<String> response2 = restTemplate2.exchange(
//                "https://kapi.kakao.com/v2/user/me",
//                HttpMethod.GET,
//                kakaoProfileRequest,
//                String.class);
//
//        KakaoUserInfo kakaoUserInfo = null;
//        try {
//            kakaoUserInfo = objectMapper.readValue(response2.getBody(), KakaoUserInfo.class);
//        } catch (JsonMappingException e) {
//            e.printStackTrace();
//        } catch (JsonProcessingException e) {
//            e.printStackTrace();
//        }
//
//        Optional<SiteUser> _user = userService.userFind(kakaoUserInfo.getKakao_account().getEmail()+kakaoUserInfo.getId());
//
//        if (!(_user.isPresent())){
//            userService.kakaoCreate(kakaoUserInfo.getKakao_account().getEmail(), kakaoUserInfo.getId(),key);
//            System.out.printf("회원가입 완료");
//        }
//        SiteUser user = _user.get();
//
//        httpServletRequest.getSession().invalidate();
//        HttpSession session = httpServletRequest.getSession(true);
//        session.setAttribute("userId",user.getEmail());
//        session.setMaxInactiveInterval(1800);
//        System.out.printf("세션 등록 완료");
//        return "redirect:/";
//    }

    @GetMapping("/logout")
    public String logout(HttpServletRequest request){
        HttpSession session = request.getSession(false);
        session.invalidate();

        return "redirect:/";
    }

}
