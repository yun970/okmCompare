package com.example.okmprice.controller;

import com.example.okmprice.DTO.AlarmDto;
import com.example.okmprice.model.Price;
import com.example.okmprice.service.AlarmService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class AlarmController {
    private final AlarmService alarmService;

    public AlarmController(AlarmService alarmService) {
        this.alarmService = alarmService;
    }
    @PostMapping("/v2")
    public String example(){
        return "asdf";
    }
    @PostMapping("/v1")
    public ResponseEntity create(@RequestBody AlarmDto alarmDto, Authentication authentication){

        System.out.printf(authentication.getName());
        alarmService.alarmRegistry(alarmDto);
        return ResponseEntity.ok().body("성공");
    }


}
