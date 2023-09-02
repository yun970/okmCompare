package com.example.okmprice.repository;

import com.example.okmprice.model.Brands;
import com.example.okmprice.model.Products;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BrandsRepository extends JpaRepository<Brands,Integer> {
    @Transactional
    Brands findAllByName(String value);
}
