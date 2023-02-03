package com.example.finalproject.models;

import java.util.ArrayList;

// this class represents a travel
public class Travel extends FirebaseModel {
    private String destination;

    private long travelDate;
    private long returnDate;

    private ArrayList<Ticket> flightTickets;
    private ArrayList<Residing> residing;

    // optional field
    private CartRental carRental;

    private Insurance insurance;

    private String recommendations;


    public Travel() { }

    public Travel(String id, String destination, long travelDate, long returnDate, ArrayList<Ticket> flightTickets, ArrayList<Residing> residing, CartRental carRental, Insurance insurance, String recommendations) {
        super(id);
        this.destination = destination;
        this.travelDate = travelDate;
        this.returnDate = returnDate;
        this.flightTickets = flightTickets;
        this.residing = residing;
        this.carRental = carRental;
        this.insurance = insurance;
        this.recommendations = recommendations;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public long getTravelDate() {
        return travelDate;
    }

    public void setTravelDate(long travelDate) {
        this.travelDate = travelDate;
    }

    public long getReturnDate() {
        return returnDate;
    }

    public void setReturnDate(long returnDate) {
        this.returnDate = returnDate;
    }

    public ArrayList<Ticket> getFlightTickets() {
        return flightTickets;
    }

    public void setFlightTickets(ArrayList<Ticket> flightTickets) {
        this.flightTickets = flightTickets;
    }

    public ArrayList<Residing> getResiding() {
        return residing;
    }

    public void setResiding(ArrayList<Residing> residing) {
        this.residing = residing;
    }

    public CartRental getCarRental() {
        return carRental;
    }

    public void setCarRental(CartRental carRental) {
        this.carRental = carRental;
    }

    public Insurance getInsurance() {
        return insurance;
    }

    public void setInsurance(Insurance insurance) {
        this.insurance = insurance;
    }

    public String getRecommendations() {
        return recommendations;
    }

    public void setRecommendations(String recommendations) {
        this.recommendations = recommendations;
    }
}
