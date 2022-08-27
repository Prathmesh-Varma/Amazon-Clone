package com.example.testing.model;

import java.math.BigDecimal;

public class Product {

    private  int pId;
    private String pName;
    private BigDecimal pPrice;
    private String pDescription;
    private String pImagename;

    public Product(){ super();}

    public Product(int pId, String pName, BigDecimal pPrice, String pDescription, String pImagename){
        setpId(pId);
        setpName(pName);
        setpPrice(pPrice);
        setpDescription(pDescription);
        setpImagename(pImagename);
    }

    public int getpId() {
        return pId;
    }

    public String getpName() {
        return pName;
    }

    public BigDecimal getpPrice() {
        return pPrice;
    }

    public String getpDescription() {
        return pDescription;
    }

    public String getpImagename() {
        return pImagename;
    }

    public void setpId(int pId) {
        this.pId = pId;
    }

    public void setpName(String pName) {
        this.pName = pName;
    }

    public void setpPrice(BigDecimal pPrice) {
        this.pPrice = pPrice;
    }

    public void setpDescription(String pDescription) {
        this.pDescription = pDescription;
    }

    public void setpImagename(String pImagename) {
        this.pImagename = pImagename;
    }
}
