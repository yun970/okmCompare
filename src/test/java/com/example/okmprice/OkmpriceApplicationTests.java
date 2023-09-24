package com.example.okmprice;

import com.example.okmprice.model.Brands;
import com.example.okmprice.model.Products;
import com.example.okmprice.service.BrandsService;
import com.example.okmprice.service.ProductsService;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.parameters.P;

import java.util.List;

@SpringBootTest
class OkmpriceApplicationTests {

    @Autowired
    ProductsService productsService;
    @Test
    public void test(){
        List<Products> res1 = productsService.searchLowestPrice();
        List<Products> res3 = productsService.searchCheapProducts();

        productsService.cacheEvict1();
        productsService.cacheEvict2();

        List<Products> res6 = productsService.searchLowestPrice();
        List<Products> res8 = productsService.searchLowestPrice();
        System.out.printf("asdf");
    }

}
