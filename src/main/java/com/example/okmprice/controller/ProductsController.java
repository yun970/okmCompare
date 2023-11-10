package com.example.okmprice.controller;

import com.example.okmprice.model.Brands;
import com.example.okmprice.model.Products;
import com.example.okmprice.model.SiteUser;
import com.example.okmprice.service.BrandsService;
import com.example.okmprice.service.ProductsService;
import com.example.okmprice.service.UserService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Controller
@SessionAttributes("userId")
public class ProductsController {

    private final ProductsService  productsService;
    private final BrandsService brandsService;
    private final UserService userService;

    public ProductsController(ProductsService productsService, BrandsService brandsService, UserService userService){
        this.productsService = productsService;
        this.brandsService = brandsService;
        this.userService = userService;
    }

    @GetMapping("/chart")
    public String chart(){
        return "chart";
    }

    @GetMapping("/search")
    public String searchProduct(@RequestParam("keyword") String keyword, Model model, @RequestParam(value="page", defaultValue="0") int page){

        Brands rs = brandsService.searchBrandsName(keyword);
        if(rs != null){
            Page<Products> result = productsService.searchProductsByNumber(rs.getId(), page);
            model.addAttribute("paging", result);
            model.addAttribute("keyword",keyword);
            return "product_search";
        }
        model.addAttribute("paging", productsService.searchProductsByName(keyword, page));
        model.addAttribute("keyword",keyword);
        return "product_search";
    }
    @GetMapping("/")
    public String reductedPrice(Authentication authentication, Model model, HttpServletResponse response){
        System.out.printf("홈페이지 도착");
        model.addAttribute("authentication", authentication);

        if(authentication != null){
            String token = response.getHeader("Authorization");
            System.out.printf("\n성공\n");
        }


        List<Products> result1 = productsService.searchLowestPrice();
        List<Products> result2 = productsService.searchCheapProducts();
        Collections.shuffle(result1);
        Collections.shuffle(result2);
        model.addAttribute("result1", result1);
        model.addAttribute("result2", result2);
        return "main";
    }
    @GetMapping("/product/test")
    @ResponseBody
    public List<Products> testApi(){
        var result = productsService.searchCheapProducts();
        return result;
    }
    @GetMapping("/view")
    public String ProductPage(@RequestParam int num, Model model, Authentication authentication){
        Optional<Products> product = productsService.searchProductByNumber(num);
        model.addAttribute("result1", product.get());
        model.addAttribute("result2", brandsService.searchBrandsId(product.get().getId()));
        model.addAttribute("authentication", authentication);

        return "itemPage";
    }


    @GetMapping("/cacheEvict")
    public ResponseEntity cacheEvict(){
        productsService.cacheEvict1();
        productsService.cacheEvict2();

        return ResponseEntity.ok().body("성공");
    }


}
