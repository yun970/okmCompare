package com.example.okmprice.repository;

import com.example.okmprice.model.Products;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductsRepository extends JpaRepository<Products, Integer>{


//    List<Products> findAllByProductNameContains(String keyword);
//    List<Products> findAllById(int keyword);

    Page<Products> findAllById(int keyword, Pageable pageable);

    Page<Products> findAllByProductNameContains(String keyword, Pageable pageable);

    @Query("select p " +
            "from Products p " +
            "where p.lowestPrice = p.todayPrice AND recentlyDate = CURDATE() AND p.todayPrice <> p.yesterdayPrice")
    List<Products> findProductsLowestPrice();

    @Query("SELECT p " +
            "FROM Products p " +
            "JOIN Price p_yesterday ON p_yesterday.priceId = p.yesterdayPrice " +
            "JOIN Price p_today ON p_today.priceId = p.todayPrice " +
            "WHERE p_yesterday.productPrice > p_today.productPrice")
    List<Products> findProductReductedPrice();
}