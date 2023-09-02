package com.example.okmprice;

import com.example.okmprice.model.Brands;
import com.example.okmprice.model.Products;
import com.example.okmprice.service.BrandsService;
import com.example.okmprice.service.ProductsService;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
class OkmpriceApplicationTests {

	@Autowired
	ProductsService productsService;
	@Autowired
	BrandsService brandsService;
	@Test
	void contextLoads() {
		List<Products> p = productsService.searchLowestPrice();
		for(Products li : p){
			System.out.printf(li.getProductNum()+" "+li.getBrands()+"\n");
		}
	}

	@Test
	void test(){
		List<Products> p = productsService.searchCheapProducts();
		for(Products li : p){
			System.out.printf(li.getProductNum()+" "+li.getBrands()+"\n");
		}
	}

	@Test
	@Transactional
	void test2(){
		Brands brands=brandsService.searchBrands("니들스");
		System.out.printf(brands.getName()+"\n");
		System.out.printf(brands.getProducts().toString());
	}

}
