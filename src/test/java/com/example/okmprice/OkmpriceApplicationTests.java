package com.example.okmprice;


import com.example.okmprice.service.AlarmService;
import com.example.okmprice.service.ProductsService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class OkmpriceApplicationTests {

    @Autowired
    ProductsService productsService;

    @Autowired
    AlarmService alarmService;

    @Test
    public void test(){
        var asdf1 = productsService.searchLowestPrice();
        var asdf2 = productsService.searchLowestPrice();
    }

}
