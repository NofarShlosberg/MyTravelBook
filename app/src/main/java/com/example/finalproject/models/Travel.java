package com.example.finalproject.models;

import com.google.firebase.auth.FirebaseAuth;

// this class represents a travel
public class Travel extends FirebaseModel {

    private String creatorId;
    private String destination;

    private long travelDate;
    private long returnDate;

    private Ticket flightTickets;
    private Residing residing;

    // optional field
    private CarRental carRental;

    private Insurance insurance;
    private String recommendations;

    public Travel() {
    }

    public Travel(String id, String creatorId, String destination, long travelDate, long returnDate, Ticket flightTickets, Residing residing, CarRental carRental, Insurance insurance, String recommendations) {
        super(id);
        this.creatorId = creatorId;
        this.destination = destination;
        this.travelDate = travelDate;
        this.returnDate = returnDate;
        this.flightTickets = flightTickets;
        this.residing = residing;
        this.carRental = carRental;
        this.insurance = insurance;
        this.recommendations = recommendations;
    }

    public Travel(String id, String destination, long travelDate, long returnDate, Ticket flightTickets, Residing residing, CarRental carRental, Insurance insurance, String recommendations) {
        super(id);
        this.creatorId = FirebaseAuth.getInstance().getUid();
        this.destination = destination;
        this.travelDate = travelDate;
        this.returnDate = returnDate;
        this.flightTickets = flightTickets;
        this.residing = residing;
        this.carRental = carRental;
        this.insurance = insurance;
        this.recommendations = recommendations;
    }

    public String getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(String creatorId) {
        this.creatorId = creatorId;
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

    public Ticket getFlightTickets() {
        return flightTickets;
    }

    public void setFlightTickets(Ticket flightTickets) {
        this.flightTickets = flightTickets;
    }

    public Residing getResiding() {
        return residing;
    }

    public void setResiding(Residing residing) {
        this.residing = residing;
    }

    public CarRental getCarRental() {
        return carRental;
    }

    public void setCarRental(CarRental carRental) {
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

