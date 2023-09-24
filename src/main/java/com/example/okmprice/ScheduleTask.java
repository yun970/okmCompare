package com.example.okmprice;

import com.example.okmprice.service.ProductsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class ScheduleTask {
    
    @Autowired
    ProductsService productsService;
    @Scheduled(cron = "0 17 * * *")
    public void task(){
        productsService.cacheEvict1();
        productsService.cacheEvict2();
        System.out.printf("스케줄링 완료");
    }
}
