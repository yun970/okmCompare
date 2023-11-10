package com.example.okmprice.model;

import jakarta.persistence.*;

import java.util.Date;
import java.util.UUID;

@Entity
public class Price {

    @Id
    private UUID priceId;
    //    private int productNum;
    private int productPrice;
    private Date createDate;
    private int productNum;

    public UUID getPriceId() {
        return priceId;
    }

    public void setPriceId(UUID priceId) {
        this.priceId = priceId;
    }

    public int getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(int productPrice) {
        this.productPrice = productPrice;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public int getProductNum() {
        return productNum;
    }

    public void setProductNum(int productNum) {
        this.productNum = productNum;
    }
}
