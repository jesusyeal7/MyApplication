package com.example.pricewatcher20;

import android.os.Parcel;
import android.os.Parcelable;

public class Item implements Parcelable {


    private String article;
    private float initialP;
    private float currentP;
    private PriceFinder findPrice;
    private String link;
    private double percent;


    public Item(){
        article = "";
        findPrice = new PriceFinder();
        initialP = (float) 0.00;
        currentP = (float) 0.00;
    }
    public Item(String item, float startPrice,String url) {
        article = item;
        initialP = startPrice;
        link = url;
        findPrice = new PriceFinder();
    }

    protected Item(Parcel in) {
        article = in.readString();
        initialP = in.readFloat();
        currentP = in.readFloat();
        link = in.readString();
    }

    public static final Creator<Item> CREATOR = new Creator<Item>() {
        @Override
        public Item createFromParcel(Parcel in) {
            return new Item(in);
        }

        @Override
        public Item[] newArray(int size) {
            return new Item[size];
        }
    };

    public double getStartPrice() {
        return initialP;
    }

    public double getCurrentPrice() {
        return currentP;
    }

    public void setStartPrice(float start) {
        initialP = start;
    }

    public void setCurrentPrice(){
        currentP = findPrice.findPrice();
    }

    public void setItem(String item) {
        article = item;
    }

    public String getItem(){
        return article;
    }

    public String getUrl(){
        return link;
    }

    public void setUrl(String surl){
        link=surl;
    }


    private void calculatePercent(){
    percent = (((double)currentP - (double)initialP)/ (double)initialP)*100;
}

    public double getPercent(){

        calculatePercent();

        return percent;

    }
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(article);
        dest.writeFloat(initialP);
        dest.writeFloat(currentP);
        dest.writeString(link);
    }
}
