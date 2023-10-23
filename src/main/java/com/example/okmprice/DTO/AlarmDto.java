package com.example.okmprice.DTO;

import com.example.okmprice.model.Price;

public class AlarmDto {

    private String email;
    private Integer ProductNum;
    private Price price;

    public String getEmail() {
        return email;
    }

    public int getProductNum() {
        return ProductNum;
    }

    public Price getPrice() {
        return price;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setProductNum(Integer productNum) {
        ProductNum = productNum;
    }

    public void setPrice(Price price) {
        this.price = price;
    }
}
