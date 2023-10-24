package com.example.okmprice.service;

import com.example.okmprice.model.Products;
import com.example.okmprice.repository.ProductsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductsService {

    private final ProductsRepository productRepository;

    @Autowired
    public ProductsService(ProductsRepository productRepository){
        this.productRepository = productRepository;
    }

    public Page<Products> searchProductsByName(String keyword, int page){
        Pageable pageable = PageRequest.of(page, 21);
        return productRepository.findAllByProductNameContains(keyword, pageable);
    }
    public Page<Products> searchProductsByNumber(int keyword, int page){
        Pageable pageable = PageRequest.of(page, 21);
        return productRepository.findAllById(keyword, pageable);
    }

    public Optional<Products> searchProductByNumber(int keyword){

        return productRepository.findById(keyword);
    }

    @Cacheable(cacheNames = "myCache")
    public List<Products> searchLowestPrice(){
        System.out.println("캐시 적용 전");
        var lowest =  productRepository.findProductsLowestPrice();
        return lowest;
    }
    @Cacheable(cacheNames = "myCache2")
    public List<Products> searchCheapProducts(){
        System.out.println("캐시 적용 전");
        var cheap = productRepository.findProductReductedPrice();
        return cheap;
    }

    @CacheEvict(value = "myCache", allEntries = true)
    public void cacheEvict1(){
        System.out.println("cacheEvict1 실행");
    }

    @CacheEvict(value = "myCache2", allEntries = true)
    public void cacheEvict2(){
        System.out.println("cacheEvict2 실행");
    }



}
