package com.example.okmprice.service;

import com.example.okmprice.controller.PriceController;
import com.example.okmprice.model.Price;
import com.example.okmprice.repository.PriceRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PriceService {
    
    private final PriceRepository priceRepository;
    
    public PriceService(PriceRepository priceRepository){
        this.priceRepository = priceRepository;
    }

    public List<Price> getAllPrice(Integer value) {
        return priceRepository.findAllByProductNum(value);
    }
    public Price getPrice(Integer value){ return priceRepository.findByProductNum(value);}
}
