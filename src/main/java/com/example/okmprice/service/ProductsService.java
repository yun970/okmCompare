package com.example.okmprice.service;

import com.example.okmprice.model.Products;
import com.example.okmprice.repository.ProductsRepository;
import org.springframework.beans.factory.annotation.Autowired;
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

    public List<Products> searchLowestPrice(){
        return productRepository.findProductsLowestPrice();
    }
    public Optional<Products> searchProductByNumber(int keyword){
        return productRepository.findById(keyword);
    }


    public List<Products> searchCheapProducts(){
        return productRepository.findProductReductedPrice();
    }





}
