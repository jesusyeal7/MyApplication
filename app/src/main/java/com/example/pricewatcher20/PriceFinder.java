package com.example.pricewatcher20;

import java.util.Random;

public class PriceFinder {

    public float findPrice(){
        float price = (float) 0.00;
        Random random = new Random();
        price =  random.nextInt(99 - 1 + 1) + 1000;;

        float floatingnumber = (float) price;
        return floatingnumber;
    }
}
