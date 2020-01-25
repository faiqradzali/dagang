package com.example.myapplication;

public class PortfolioObject {

        private String stock;
        private String lotSize;
        private String buyPrice;
        private String close;

    public PortfolioObject(String stock, String lotSize, String buyPrice, String close) {
        this.stock = stock;
        this.lotSize = lotSize;
        this.buyPrice = buyPrice;
        this.close = close;
    }

    public String getStock() {
        return stock;
    }

    public void setStock(String stock) {
        this.stock = stock;
    }

    public String getLotSize() {
        return lotSize;
    }

    public void setLotSize(String lotSize) {
        this.lotSize = lotSize;
    }

    public String getBuyPrice() {
        return buyPrice;
    }

    public void setBuyPrice(String buyPrice) {
        this.buyPrice = buyPrice;
    }

    public String getClose() {
        return close;
    }

    public void setClose(String close) {
        this.close = close;
    }
}
