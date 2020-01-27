package com.example.myapplication;

import com.google.firebase.firestore.Exclude;

public class LogObject {
    private String documentID;
    private String currentDate;
    private String close;
    private String size;
    private String stock_name;
    private String type;

    public LogObject(){}

    @Exclude
    public String getDocumentID(){
        return documentID;
    }

    public void setDocumentID(String documentID) {
        this.documentID = documentID;
    }

    public LogObject(String currentDate, String stock_name, String close, String size, String type){
        this.currentDate = currentDate;
        this.close=close;
        this.size=size;
        this.stock_name=stock_name;
        this.type=type;
    }

    public String getCurrentDate(){
        return currentDate;
    }

    public String getClose() {
        return close;
    }

    public String getType() {
        return type;
    }

    public String getSize() {
        return size;
    }

    public String getStock_name() {
        return stock_name;
    }

}
