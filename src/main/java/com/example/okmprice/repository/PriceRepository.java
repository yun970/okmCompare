package com.example.okmprice.repository;

import com.example.okmprice.model.Price;
import com.example.okmprice.model.Products;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface PriceRepository extends JpaRepository<Price, UUID> {
    List<Price> findAllByProductNum(Integer value);

}
