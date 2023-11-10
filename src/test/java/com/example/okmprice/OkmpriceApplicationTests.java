package com.example.okmprice;


import com.example.okmprice.service.AlarmService;
import com.example.okmprice.service.PriceService;
import com.example.okmprice.service.ProductsService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class OkmpriceApplicationTests {

    @Autowired
    ProductsService productsService;
    @Autowired
    PriceService priceService;

    @Autowired
    AlarmService alarmService;

    @Test
    public void test(){
        var a = priceService.getAllPrice(111057);

        System.out.printf("asdfasdf");
    }

}
