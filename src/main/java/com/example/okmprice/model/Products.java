package com.example.okmprice.model;

import jakarta.persistence.*;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Entity
public class Products {
    @Id
    private int productNum;
    private int id;
    private String productName;
    private String productImg;
    private String productAddress;
    private Date recentlyDate;

    private UUID todayPrice;
    private UUID lowestPrice;
    private UUID yesterdayPrice;





//    @ManyToOne
//    private Brands brands;
//
//    @OneToMany(mappedBy = "products", cascade = CascadeType.REMOVE)
//    private List<Price> priceList;

    public UUID getTodayPrice() {
        return todayPrice;
    }

    public void setTodayPrice(UUID todayPrice) {
        this.todayPrice = todayPrice;
    }

    public UUID getLowestPrice() {
        return lowestPrice;
    }

    public void setLowestPrice(UUID lowestPrice) {
        this.lowestPrice = lowestPrice;
    }

    public UUID getYesterdayPrice() {
        return yesterdayPrice;
    }
    public void setYesterdayPrice(UUID yesterdayPrice) {
        this.yesterdayPrice = yesterdayPrice;
    }

    public int getProductNum() {
        return productNum;
    }

    public void setProductNum(int productNum) {
        this.productNum = productNum;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductImg() {
        return productImg;
    }

    public void setProductImg(String productImg) {
        this.productImg = productImg;
    }

    public String getProductAddress() {
        return productAddress;
    }

    public void setProductAddress(String productAddress) {
        this.productAddress = productAddress;
    }

    public Date getRecentlyDate() {
        return recentlyDate;
    }

    public void setRecentlyDate(Date recentlyDate) {
        this.recentlyDate = recentlyDate;
    }

}
