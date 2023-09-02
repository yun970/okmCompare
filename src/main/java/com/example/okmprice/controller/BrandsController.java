package com.example.okmprice.controller;

import com.example.okmprice.model.Brands;
import com.example.okmprice.service.BrandsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class BrandsController {

    private final BrandsService brandsService;
    @Autowired
    public BrandsController(BrandsService brandsService){
        this.brandsService = brandsService;
    }

    @GetMapping("/hello")
    public String hellopage(){
        return "chart";
    }


}

/*
    점프 투 스프링 처음부터 끝까지 다시 만들어보기
    이메일 확인 기능, 카카오톡 회원가입 추가
    로그인, 로그아웃 기능 추가
    사이트 테이블 추가 (오케이몰 이외의 사이트도 가격비교)
    Brands 테이블에 사이트 외래키로 추가
    Products 테이블에 최저가격, 최져가격 날짜, 최신 업데이트 날짜 속성 추가

*
 */
