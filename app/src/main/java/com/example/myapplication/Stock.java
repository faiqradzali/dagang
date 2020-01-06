package com.example.myapplication;

public class Stock {
    private String stockName;
    private String open;
    private String high;
    private String low;
    private String close;
    private String volume;

    public Stock (String stockName, String open, String high, String low, String close, String volume) {
        this.stockName = stockName;
        this.open = open;
        this.high = high;
        this.low = low;
        this.close = close;
        this.volume = volume;
    }

}
