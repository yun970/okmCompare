package com.example.okmprice.service;

import com.example.okmprice.model.Brands;
import com.example.okmprice.model.Products;
import com.example.okmprice.repository.BrandsRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BrandsService {

    private final BrandsRepository brandsRepository;


    public BrandsService(BrandsRepository brandsRepository){
        this.brandsRepository = brandsRepository;
    }

    public Brands searchBrands(String value){
        Brands result = brandsRepository.findAllByName(value);
        if (result != null && result.getName() != null)
            return result;
        else{
            return null;
        }
    }




}
