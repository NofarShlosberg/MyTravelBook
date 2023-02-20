package com.example.finalproject.models;

public class Residing extends TravelDoc {
    private double price;
    private long checkIn;
    private long checkOut;
    private String location;
    private boolean prepaid;
    private String image;

    public Residing() {
    }

    public Residing(String id, String documentUrl, double price, long checkIn, long checkOut, String location, boolean prepaid, String image) {
        super(id, documentUrl);
        this.price = price;
        this.checkIn = checkIn;
        this.checkOut = checkOut;
        this.location = location;
        this.prepaid = prepaid;
        this.image = image;
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

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}