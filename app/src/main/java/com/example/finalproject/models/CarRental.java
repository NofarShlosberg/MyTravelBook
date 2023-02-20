package com.example.finalproject.models;

public class CarRental extends TravelDoc {

    private String car;
    private String rentalCompany;
    private double price;
    private String pickupLocation;
    private String returnLocation;

    public CarRental() {}

    public CarRental(String id, String documentUrl, String car, String rentalCompany, double price, String pickupLocation, String returnLocation) {
        super(id, documentUrl);
        this.car = car;
        this.rentalCompany = rentalCompany;
        this.price = price;
        this.pickupLocation = pickupLocation;
        this.returnLocation = returnLocation;
    }

    public String getCar() {
        return car;
    }

    public void setCar(String car) {
        this.car = car;
    }

    public String getRentalCompany() {
        return rentalCompany;
    }

    public void setRentalCompany(String rentalCompany) {
        this.rentalCompany = rentalCompany;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getPickupLocation() {
        return pickupLocation;
    }

    public void setPickupLocation(String pickupLocation) {
        this.pickupLocation = pickupLocation;
    }

    public String getReturnLocation() {
        return returnLocation;
    }

    public void setReturnLocation(String returnLocation) {
        this.returnLocation = returnLocation;
    }
}
