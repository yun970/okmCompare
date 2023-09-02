package com.example.okmprice.controller;

import com.example.okmprice.model.Brands;
import com.example.okmprice.model.Products;
import com.example.okmprice.model.SiteUser;
import com.example.okmprice.service.BrandsService;
import com.example.okmprice.service.ProductsService;
import com.example.okmprice.service.UserService;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.bind.annotation.SessionAttributes;

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

        Brands rs = brandsService.searchBrands(keyword);
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
    public String reductedPrice(Model model,@SessionAttribute(name = "userId", required = false) String userId){
        System.out.printf("홈페이지 도착");
        Optional<SiteUser> _loginUser = userService.userFind(userId);

        if((_loginUser.isPresent())){
            model.addAttribute("userId", _loginUser.get().getEmail());
            System.out.printf("세션 있음");
        }


        List<Products> result1 = productsService.searchLowestPrice();
        List<Products> result2 = productsService.searchCheapProducts();
        Collections.shuffle(result1);
        Collections.shuffle(result2);
        model.addAttribute("result1", result1);
        model.addAttribute("result2", result2);
        return "main";
    }

    @GetMapping("/view")
    public String ProductPage(@RequestParam int num, Model model){
        Optional<Products> product = productsService.searchProductByNumber(num);
        model.addAttribute("result", product.get());
        return "itemPage";
    }


}
