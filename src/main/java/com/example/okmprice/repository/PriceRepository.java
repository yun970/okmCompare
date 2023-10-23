package com.example.okmprice.repository;

import com.example.okmprice.model.Price;
import com.example.okmprice.model.Products;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface PriceRepository extends JpaRepository<Price, UUID> {
    List<Price> findAllByProductNum(Integer value);
    Price findByProductNum(Integer value);


    Price findFirstByProductNumOrderByCreateDateDesc(Integer productNum);
}
