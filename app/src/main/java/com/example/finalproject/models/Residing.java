package com.example.finalproject.models;

import java.util.ArrayList;

public class Residing extends TravelDoc {
    private double price;
    private long checkIn;
    private long checkOut;
    private String location;
    private boolean prepaid;
    private ArrayList<String> images;

    public Residing() {
    }

    public Residing(String id, String documentUrl, double price, long checkIn, long checkOut, String location, boolean prepaid, ArrayList<String> images) {
        super(id, documentUrl);
        this.price = price;
        this.checkIn = checkIn;
        this.checkOut = checkOut;
        this.location = location;
        this.prepaid = prepaid;
        this.images = images;
    }


    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public long getCheckIn() {
        return checkIn;
    }

    public void setCheckIn(long checkIn) {
        this.checkIn = checkIn;
    }

    public long getCheckOut() {
        return checkOut;
    }

    public void setCheckOut(long checkOut) {
        this.checkOut = checkOut;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public boolean isPrepaid() {
        return prepaid;
    }

    public void setPrepaid(boolean prepaid) {
        this.prepaid = prepaid;
    }

    public ArrayList<String> getImages() {
        return images;
    }

    public void setImages(ArrayList<String> images) {
        this.images = images;
    }
}
