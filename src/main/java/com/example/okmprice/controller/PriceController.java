package com.example.okmprice.controller;

import com.example.okmprice.model.Price;
import com.example.okmprice.service.PriceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class PriceController {

    private final PriceService priceService;

    @Autowired
    public PriceController(PriceService priceService){
        this.priceService = priceService;
    }

    @GetMapping("/datalist/{num}")
    public List<Price> itemPage(@PathVariable("num") Integer num) {
        return priceService.getPrice(num);
    }




}
