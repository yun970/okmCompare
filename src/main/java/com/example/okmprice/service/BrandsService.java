package com.example.okmprice.service;

import com.example.okmprice.model.Brands;
import com.example.okmprice.model.Products;
import com.example.okmprice.repository.BrandsRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BrandsService {

    private final BrandsRepository brandsRepository;


    public BrandsService(BrandsRepository brandsRepository){
        this.brandsRepository = brandsRepository;
    }

    public Brands searchBrandsName(String value){
        Brands result = brandsRepository.findAllByName(value);
        if (result != null && result.getName() != null)
            return result;
        else{
            return null;
        }
    }

    public Brands searchBrandsId(int id){
        Optional<Brands> rs = brandsRepository.findById(id);
        return rs.get();
    }




}
