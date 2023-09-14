package com.example.okmprice.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;

import java.util.Date;
import java.util.UUID;

@Entity
public class Price {
    public UUID getPriceId() {
        return priceId;
    }

    public void setPriceId(UUID priceId) {
        this.priceId = priceId;
    }

    public int getProductNum() {
        return productNum;
    }

    public void setProductNum(int productNum) {
        this.productNum = productNum;
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

    public Products getProducts() {
        return products;
    }

    public void setProducts(Products products) {
        this.products = products;
    }
    @Id
    private UUID priceId;
    private int productNum;
    private int productPrice;
    private Date createDate;
    @Column(name = "product_num")
    @ManyToOne
    private Products products;

}
