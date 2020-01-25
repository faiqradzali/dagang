package com.example.myapplication;

import com.google.firebase.firestore.Exclude;

public class Note {
    private String documentID;
    private String currentDate;
    private String close;
    private String size;
    private String stock_name;
    private String type;

    public Note(){}

    @Exclude
    public String getDocumentID(){
        return documentID;
    }

    public void setDocumentID(String documentID) {
        this.documentID = documentID;
    }

    public Note(String currentDate, String close, String size, String stock_name, String type){
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
